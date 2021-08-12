package eatyourbeets.relics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.cards.animator.enchantments.Enchantment;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.powers.animator.EnchantmentPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public abstract class EnchantableRelic extends AnimatorRelic// implements CustomSavable<Integer> NOTE: I do not implement this here because CustomSavable patch does not check abstract classes
{
    public Enchantment enchantment;

    public EnchantableRelic(String id, RelicTier tier, LandingSound sfx)
    {
        this(id, tier, sfx, null);
    }

    public EnchantableRelic(String id, RelicTier tier, LandingSound sfx, Enchantment enchantment)
    {
        super(id, tier, sfx);

        if (enchantment != null)
        {
            ApplyEnchantment(enchantment);
        }
    }

    public Integer onSave()
    {
        return (enchantment != null) ? (enchantment.index * 10 + enchantment.upgradeIndex) : 0;
    }

    public void onLoad(Integer index)
    {
        if (index != null && index > 0)
        {
            JUtils.LogInfo(this, "onLoad:" + index);

            ApplyEnchantment(Enchantment.GetCard(index / 10, index % 10));
        }
    }

    @Override
    public void renderTip(SpriteBatch sb)
    {
        super.renderTip(sb);

        if (enchantment != null)
        {
            enchantment.drawScale = enchantment.targetDrawScale = 0.8f;
            enchantment.current_x = enchantment.target_x = InputHelper.mX + (((InputHelper.mX > (Settings.WIDTH * 0.5f)) ? -1.505f : 1.505f) * EYBCardTooltip.BOX_W);
            enchantment.current_y = enchantment.target_y = InputHelper.mY - (AbstractCard.IMG_HEIGHT * 0.5f);
            GR.UI.AddPostRender(enchantment::render);
        }
    }

    public void ApplyEnchantment(Enchantment enchantment)
    {
        this.enchantment = enchantment;
        RefreshTexture();
    }

    public CardGroup CreateUpgradeGroup()
    {
        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        if (enchantment == null)
        {
            for (Enchantment e : Enchantment.GetCards())
            {
                group.group.add(e.makeCopy());
            }
        }
        else if (enchantment.canUpgrade())
        {
            group.group.addAll(enchantment.GetUpgrades());
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

        if (enchantment != null)
        {
            GameActions.Bottom.ApplyPower(new EnchantmentPower(this, player, 1)).ShowEffect(false, true);
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        final EnchantableRelic copy = (EnchantableRelic) super.makeCopy();
        if (enchantment != null)
        {
            copy.ApplyEnchantment((Enchantment) enchantment.makeStatEquivalentCopy());
        }

        return copy;
    }
}
