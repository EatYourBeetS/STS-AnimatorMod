package pinacolada.ui.cardReward;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rewards.RewardItem;
import eatyourbeets.interfaces.listeners.OnAddingToCardRewardListener;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCard_UltraRare;
import pinacolada.effects.SFX;
import pinacolada.effects.card.PermanentUpgradeEffect;
import pinacolada.relics.pcl.AbstractMissingPiece;
import pinacolada.relics.pcl.CursedGlyph;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLStrings;
import pinacolada.resources.pcl.misc.PCLRuntimeLoadout;
import pinacolada.ui.GUIElement;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class PCLCardRewardBonus extends GUIElement
{
    private static final CursedGlyph CURSED_GLYPH = new CursedGlyph();

    private final PCLStrings.Rewards REWARDS = GR.PCL.Strings.Rewards;
    private final ArrayList<CardRewardBundle> bundles = new ArrayList<>();
    private RewardItem rewardItem;

    public PCLCardRewardBonus()
    {
        this(null);
    }

    public PCLCardRewardBonus(RewardItem rewardItem)
    {
        this.rewardItem = rewardItem;
    }

    public void Open(RewardItem rewardItem, ArrayList<AbstractCard> cards)
    {
        this.rewardItem = rewardItem;
        this.bundles.clear();

        final ArrayList<AbstractCard> toRemove = new ArrayList<>();
        for (AbstractCard card : cards)
        {
            if (card instanceof OnAddingToCardRewardListener && ((OnAddingToCardRewardListener) card).ShouldCancel())
            {
                toRemove.add(card);
                continue;
            }

            Add(card);
        }

        for (AbstractCard card : toRemove)
        {
            final AbstractCard replacement = GR.PCL.Dungeon.GetRandomRewardCard(cards, true, false);
            if (replacement != null)
            {
                PCLGameUtilities.CopyVisualProperties(replacement, card);
                cards.remove(card);
                cards.add(replacement);
                if (rewardItem.cards != cards)
                {
                    rewardItem.cards.remove(card);
                    rewardItem.cards.add(replacement);
                }

                Add(replacement);
            }
        }
    }

    public void Close()
    {
        rewardItem = null;
        bundles.clear();
    }

    public void Update()
    {
        for (CardRewardBundle cardRewardBundle : bundles)
        {
            cardRewardBundle.Update();
        }
    }

    public void Render(SpriteBatch sb)
    {
        for (CardRewardBundle cardRewardBundle : bundles)
        {
            cardRewardBundle.Render(sb);
        }
    }

    public void OnCardObtained(AbstractCard hoveredCard)
    {
        for (CardRewardBundle cardRewardBundle : bundles)
        {
            if (cardRewardBundle.card == hoveredCard)
            {
                cardRewardBundle.Acquired();
            }
        }
    }

    public void Add(AbstractCard card)
    {
        CardRewardBundle cardRewardBundle = GetBundle(card);
        if (cardRewardBundle != null)
        {
            bundles.add(cardRewardBundle);
        }
    }

    public void Remove(AbstractCard card)
    {
        for (int i = 0; i < bundles.size(); i++)
        {
            if (bundles.get(i).card == card)
            {
                bundles.remove(i);
                return;
            }
        }
    }

    private CardRewardBundle GetBundle(AbstractCard card)
    {
        if (card instanceof PCLCard_UltraRare)
        {
            return GetCursedRelicBundle(card);
        }
        else if (card instanceof PCLCard && !GR.PCL.Dungeon.IsUnnamedReign())
        {
            for (PCLRuntimeLoadout series : GR.PCL.Dungeon.Loadouts)
            {
                if (MathUtils.randomBoolean(0.25f) && series.bonus < 8)
                {
                    if (series.GetCardPoolInPlay().containsKey(card.cardID) && PCLGameUtilities.GetMasterDeckCopies(card.cardID).isEmpty())
                    {
                        switch (series.bonus % 3)
                        {
                            case 0: return GetGoldBundle(card, series.bonus >= 4 ? 20 : 10);
                            case 1: return GetMaxHPBundle(card, series.bonus >= 6 ? 2 : 1);
                            case 2: return GetUpgradeBundle(card);
                        }
                    }
                }
            }
        }

        return null;
    }

    private CardRewardBundle GetCursedRelicBundle(AbstractCard card)
    {
        return new CardRewardBundle(card, c -> PCLGameEffects.Queue.ObtainRelic(new CursedGlyph()))
                .SetIcon(CURSED_GLYPH.img, -AbstractCard.RAW_W * 0.45f, -AbstractCard.RAW_H * 0.52f)
                .SetTooltip(CURSED_GLYPH.name, CURSED_GLYPH.description)
                .SetText(REWARDS.CursedRelic, Settings.RED_TEXT_COLOR, -AbstractCard.RAW_W * 0.10f, -AbstractCard.RAW_H * 0.54f);
    }

    private CardRewardBundle GetGoldBundle(AbstractCard card, int gold)
    {
        return new CardRewardBundle(card, this::ReceiveGold).SetAmount(gold)
                .SetIcon(ImageMaster.UI_GOLD, -AbstractCard.RAW_W * 0.45f, -AbstractCard.RAW_H * 0.545f)
                .SetText(REWARDS.GoldBonus(gold), Color.WHITE, -AbstractCard.RAW_W * 0.165f, -AbstractCard.RAW_H * 0.54f);
    }

    private CardRewardBundle GetMaxHPBundle(AbstractCard card, int maxHP)
    {
        return new CardRewardBundle(card, this::ReceiveMaxHP).SetAmount(maxHP)
                .SetIcon(ImageMaster.TP_HP, -AbstractCard.RAW_W * 0.45f, -AbstractCard.RAW_H * 0.545f)
                .SetText(REWARDS.MaxHPBonus(maxHP), Color.WHITE, -AbstractCard.RAW_W * 0.165f, -AbstractCard.RAW_H * 0.54f);
    }

    private CardRewardBundle GetUpgradeBundle(AbstractCard card)
    {
        return new CardRewardBundle(card, this::ReceiveUpgrade).SetAmount(1)
                .SetIcon(ImageMaster.TP_ASCENSION, -AbstractCard.RAW_W * 0.45f, -AbstractCard.RAW_H * 0.545f)
                .SetText(REWARDS.CommonUpgrade, Color.WHITE, -AbstractCard.RAW_W * 0.01f, -AbstractCard.RAW_H * 0.54f);
    }

    private void ReceiveGold(CardRewardBundle bundle)
    {
        for (PCLRuntimeLoadout series : GR.PCL.Dungeon.Loadouts)
        {
            if (series.GetCardPoolInPlay().containsKey(bundle.card.cardID))
            {
                SFX.Play(SFX.GOLD_GAIN);
                AbstractDungeon.player.gainGold(bundle.amount);
                series.bonus += 1;

                AbstractMissingPiece.RefreshDescription();
                PCLJUtils.LogInfo(this, "Obtained Gold Bonus (+" + bundle.amount + "): " + bundle.card.cardID);
                return;
            }
        }
    }

    private void ReceiveMaxHP(CardRewardBundle bundle)
    {
        for (PCLRuntimeLoadout series : GR.PCL.Dungeon.Loadouts)
        {
            if (series.GetCardPoolInPlay().containsKey(bundle.card.cardID))
            {
                AbstractDungeon.player.increaseMaxHp(bundle.amount, true);
                series.bonus += 1;

                AbstractMissingPiece.RefreshDescription();
                PCLJUtils.LogInfo(this, "Obtained Max HP Bonus (+" + bundle.amount + "): " + bundle.card.cardID);
                return;
            }
        }
    }

    private void ReceiveUpgrade(CardRewardBundle bundle)
    {
        for (PCLRuntimeLoadout series : GR.PCL.Dungeon.Loadouts)
        {
            if (series.GetCardPoolInPlay().containsKey(bundle.card.cardID))
            {
                PCLGameEffects.TopLevelQueue.Add(new PermanentUpgradeEffect()).SetFilter(c -> AbstractCard.CardRarity.COMMON.equals(c.rarity));
                series.bonus += 1;

                AbstractMissingPiece.RefreshDescription();
                PCLJUtils.LogInfo(this, "Obtained Common Upgrade Bonus (+" + bundle.amount + "): " + bundle.card.cardID);
                return;
            }
        }
    }
}
