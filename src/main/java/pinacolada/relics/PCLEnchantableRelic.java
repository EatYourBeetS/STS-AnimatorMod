package pinacolada.relics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.interfaces.listeners.OnReceiveRewardsListener;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.utilities.WeightedList;
import pinacolada.actions.pileSelection.SelectFromPile;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.cards.pcl.enchantments.Enchantment;
import pinacolada.powers.common.ExitStancePower;
import pinacolada.powers.special.EnchantmentPower;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.misc.PCLRuntimeLoadout;
import pinacolada.rewards.pcl.MissingPieceReward;
import pinacolada.utilities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static pinacolada.ui.seriesSelection.PCLLoadoutsContainer.MINIMUM_SERIES;

public abstract class PCLEnchantableRelic extends PCLRelic implements OnReceiveRewardsListener // implements CustomSavable<Integer> NOTE: I do not implement this here because CustomSavable patch does not check abstract classes
{
    protected transient AbstractRoom lastRoom = null;
    protected boolean showAffinities = false;
    public final boolean allowColorless;
    public final int seriesChoices;
    public static final int MAX_ENCHANTMENT_CHOICES = 3;
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

    public PCLEnchantableRelic(String id, RelicTier tier, LandingSound sfx, int seriesChoices, boolean allowColorless)
    {
        this(id, tier, sfx, seriesChoices, allowColorless, null);
    }

    public PCLEnchantableRelic(String id, RelicTier tier, LandingSound sfx, int seriesChoices, boolean allowColorless, Enchantment enchantment)
    {
        super(id, tier, sfx);
        this.seriesChoices = seriesChoices;
        this.allowColorless = allowColorless;

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
    public void renderCounter(SpriteBatch sb, boolean inTopPanel)
    {
        super.renderCounter(sb, inTopPanel);
        if (showAffinities)
        {
            GR.UI.CardAffinities.TryRender(sb);
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
        if (enchantment == null)
        {
            final RandomizedList<AbstractCard> possiblePicks = new RandomizedList<>();
            possiblePicks.AddAll(PCLJUtils.Map(Enchantment.GetCards(), PCLCard::makeCopy));
            for (int i = 0; i < MAX_ENCHANTMENT_CHOICES; i++) {
                group.group.add(possiblePicks.Retrieve(rng));
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

        if (showAffinities)
        {
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD)
            {
                GR.UI.CardAffinities.TryUpdate();
            }
            else
            {
                showAffinities = false;
            }
        }
    }

    public void Use()
    {
        if (counter > 0 && GetEnchantmentLevel() < 2)
        {
            PCLGameEffects.Queue.Callback(new SelectFromPile(name, 1, CreateUpgradeGroup())
                    .CancellableFromPlayer(true)
                    .AddCallback(selection -> {
                        if (selection.size() > 0) {
                            Enchantment e = (Enchantment) selection.get(0);
                            ApplyEnchantment(e);
                            flash();
                            AddCounter(-1);
                            Use();
                        }
                    }));
        }
    }

    @Override
    public void onVictory()
    {
        super.onVictory();
        if (PCLGameUtilities.InBossRoom() && GetEnchantmentLevel() < 2)
        {
            AddCounter(1);
            Use();
            flash();
        }
    }

    @Override
    public boolean canSpawn()
    {
        return AbstractDungeon.player.chosenClass == GR.PCL.PlayerClass;
    }

    @Override
    public void OnReceiveRewards(ArrayList<RewardItem> rewards, boolean canReceive)
    {
        if (RewardsAllowed())
        {
            this.flash();
            lastRoom = PCLGameUtilities.GetCurrentRoom();

            int startingIndex = -1;
            for (int i = 0; i < rewards.size(); i++)
            {
                RewardItem reward = rewards.get(i);
                if (reward.type == RewardItem.RewardType.CARD)
                {
                    startingIndex = i;
                    rewards.remove(startingIndex);
                    break;
                }
            }

            if (startingIndex >= 0)
            {
                AddSynergyRewards(rewards, startingIndex, seriesChoices);
            }
        }
    }

    private void AddSynergyRewards(ArrayList<RewardItem> rewards, int startingIndex, int seriesChoices)
    {
        WeightedList<CardSeries> synergies = CreateWeightedList();

        rewards.add(startingIndex, new MissingPieceReward(CardSeries.ANY));
        for (int i = 1; i <= seriesChoices; i++)
        {
            CardSeries series = synergies.Retrieve(EYBCard.rng);
            if (series != null)
            {
                rewards.add(startingIndex + i, new MissingPieceReward(series));
            }
        }

        GR.UI.CardAffinities.Open(player.masterDeck.group);
        showAffinities = true;
    }

    private WeightedList<CardSeries> CreateWeightedList()
    {
        final WeightedList<CardSeries> list = new WeightedList<>();
        final Map<CardSeries, List<AbstractCard>> synergyListMap = CardSeries.GetCardsBySynergy(player.masterDeck.group);

        if (GR.PCL.Dungeon.Loadouts.isEmpty())
        {
            GR.PCL.Dungeon.AddAllLoadouts();
        }

        for (PCLRuntimeLoadout series : GR.PCL.Dungeon.Loadouts)
        {
            if (series.GetCardPoolInPlay().size() >= MINIMUM_SERIES)
            {
                CardSeries s = series.Loadout.Series;

                int weight = 2;
                if (synergyListMap.containsKey(s))
                {
                    int size = synergyListMap.get(s).size();
                    if (size >= 3 && size <= 10)
                    {
                        weight += 2;
                    }
                }

                PCLJUtils.LogInfo(this, s.LocalizedName + " : " + weight);
                list.Add(s, weight);
            }
        }

        if (allowColorless)
        {
            list.Add(CardSeries.COLORLESS, 1 + (GR.PCL.Dungeon.Loadouts.size() / 2));
        }

        return list;
    }

    public String GetFullDescription()
    {
        return (counter > 0) ? (FormatDescription(0) + " NL  NL " + DESCRIPTIONS[1]) : FormatDescription(0);
    }

    private boolean RewardsAllowed()
    {
        final AbstractRoom room = PCLGameUtilities.GetCurrentRoom();
        return room != null && lastRoom != room && room.rewardAllowed
                && !(room instanceof MonsterRoomBoss)
                && (room instanceof MonsterRoom || room.eliteTrigger); // || (room instanceof EventRoom && room.combatEvent));
    }
}
