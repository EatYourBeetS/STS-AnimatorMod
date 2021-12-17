package pinacolada.relics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.actions.pileSelection.SelectFromPile;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.cards.pcl.enchantments.Enchantment;
import pinacolada.powers.common.ExitStancePower;
import pinacolada.powers.special.EnchantmentPower;
import pinacolada.resources.GR;
import pinacolada.utilities.*;

public abstract class PCLEnchantableRelic extends PCLRelic // implements CustomSavable<Integer> NOTE: I do not implement this here because CustomSavable patch does not check abstract classes
{
    public static final int MAX_CHOICES = 3;
    public static final int MAX_UPGRADES_PER_PATH = 100;
    public Enchantment enchantment;

    public static void RefreshDescription()
    {
        PCLEnchantableRelic enchantable = PCLGameUtilities.GetRelic(PCLEnchantableRelic.class);
        if (enchantable != null)
        {
            if (enchantable.tips.size() > 0)
            {
                enchantable.tips.get(0).description = enchantable.GetFullDescription();
            }
        }
    }

    public PCLEnchantableRelic(String id, RelicTier tier, LandingSound sfx)
    {
        this(id, tier, sfx, null);
    }

    public PCLEnchantableRelic(String id, RelicTier tier, LandingSound sfx, Enchantment enchantment)
    {
        super(id, tier, sfx);

        if (enchantment != null)
        {
            ApplyEnchantment(enchantment);
        }
    }

    public Integer onSave()
    {
        return (enchantment != null) ? (enchantment.index * MAX_UPGRADES_PER_PATH + enchantment.auxiliaryData.form) : 0;
    }

    public void onLoad(Integer index)
    {
        if (index != null && index > 0)
        {
            PCLJUtils.LogInfo(this, "onLoad:" + index);

            ApplyEnchantment(Enchantment.GetCard(index / MAX_UPGRADES_PER_PATH, index % MAX_UPGRADES_PER_PATH));
        }
    }

    @Override
    public void renderTip(SpriteBatch sb)
    {
        super.renderTip(sb);

        if (enchantment != null)
        {
            enchantment.drawScale = enchantment.targetDrawScale = 0.8f;
            enchantment.current_x = enchantment.target_x = InputHelper.mX + (((InputHelper.mX > (Settings.WIDTH * 0.5f)) ? -1.505f : 1.505f) * PCLCardTooltip.BOX_W);
            enchantment.current_y = enchantment.target_y = InputHelper.mY - (AbstractCard.IMG_HEIGHT * 0.5f);
            GR.UI.AddPostRender(enchantment::render);
        }
    }

    public void ApplyEnchantment(Enchantment enchantment)
    {
        this.enchantment = enchantment;
        //RefreshTexture();
    }

    public CardGroup CreateUpgradeGroup()
    {
        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        final RandomizedList<AbstractCard> possiblePicks = new RandomizedList<>();

        if (enchantment == null)
        {
            possiblePicks.AddAll(PCLJUtils.Map(Enchantment.GetCards(), PCLCard::makeCopy));
        }
        else if (enchantment.canUpgrade())
        {
            for (AbstractCard e : enchantment.GetUpgrades()) {
                possiblePicks.Add(e);
            }
        }

        for (int i = 0; i < MAX_CHOICES; i++) {
            group.group.add(possiblePicks.Retrieve(rng));
        }

        return group;
    }

    public int GetEnchantmentLevel()
    {
        return enchantment == null ? 0 : enchantment.upgraded ? 2 : 1;
    }

    public void RefreshTexture()
    {
        if (enchantment == null)
        {
            setTexture(GR.GetTexture(GR.GetRelicImage(relicId)));
        }
        else
        {
            setTexture(GR.GetTexture(GR.GetRelicImage(relicId + "_" + enchantment.index)));
        }
    }

    @Override
    public void atBattleStartPreDraw()
    {
        super.atBattleStartPreDraw();

        PCLActions.Bottom.ApplyPower(new ExitStancePower(player, 1)).ShowEffect(false, true);
        if (enchantment != null)
        {
            PCLActions.Bottom.ApplyPower(new EnchantmentPower(this, player, 1)).ShowEffect(false, true);
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        final PCLEnchantableRelic copy = (PCLEnchantableRelic) super.makeCopy();
        if (enchantment != null)
        {
            copy.ApplyEnchantment((Enchantment) enchantment.makeStatEquivalentCopy());
        }

        return copy;
    }

    @Override
    public void onEquip()
    {
        super.onEquip();
        RefreshDescription();
        SetCounter(0);
    }

    @Override
    public void update()
    {
        super.update();

        if (hb.hovered && PCLInputManager.RightClick.IsJustPressed())
        {
            if (AbstractDungeon.isScreenUp) {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }
            stopPulse();
            Use();
        }
    }

    public void Use()
    {
        if (counter > 0 && GetEnchantmentLevel() < 2)
        {
            PCLGameEffects.Queue.Callback(new SelectFromPile(name, 1, CreateUpgradeGroup())
                    .HideTopPanel(true)
                    .CancellableFromPlayer(false)
                    .AddCallback(selection -> {
                        if (selection.size() > 0) {
                            Enchantment e = (Enchantment) selection.get(0);
                            ApplyEnchantment(e);
                            flash();
                        }
                    }));
            AddCounter(-1);
        }
    }

    @Override
    public void onVictory()
    {
        super.onVictory();
        if (PCLGameUtilities.InBossRoom() && GetEnchantmentLevel() < 2)
        {
            AddCounter(1);
            flash();
        }
    }

    @Override
    public boolean canSpawn()
    {
        return AbstractDungeon.player.chosenClass == GR.PCL.PlayerClass;
    }

    public String GetFullDescription()
    {
        return FormatDescription(0) + " NL  NL " + DESCRIPTIONS[1];
    }
}
