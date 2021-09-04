package eatyourbeets.ui.animator.cardReward;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rewards.RewardItem;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.interfaces.listeners.OnAddingToCardRewardListener;
import eatyourbeets.relics.animator.AbstractMissingPiece;
import eatyourbeets.relics.animator.CursedGlyph;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class AnimatorCardRewardBonus extends GUIElement
{
    private static final CursedGlyph CURSED_GLYPH = new CursedGlyph();

    private final AnimatorStrings.Rewards REWARDS = GR.Animator.Strings.Rewards;
    private final ArrayList<CardRewardBundle> bundles = new ArrayList<>();
    private RewardItem rewardItem;

    public AnimatorCardRewardBonus()
    {
        this(null);
    }

    public AnimatorCardRewardBonus(RewardItem rewardItem)
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
            final AbstractCard replacement = GR.Common.Dungeon.GetRandomRewardCard(cards, true, false);
            if (replacement != null)
            {
                GameUtilities.CopyVisualProperties(replacement, card);
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
        if (card instanceof AnimatorCard_UltraRare)
        {
            return GetCursedRelicBundle(card);
        }
        else if (card instanceof AnimatorCard)
        {
            for (AnimatorRuntimeLoadout series : GR.Animator.Dungeon.Loadouts)
            {
                if (series.promoted && series.bonus < 8)
                {
                    if (series.Cards.containsKey(card.cardID) && GameUtilities.GetMasterDeckCopies(card.cardID).isEmpty())
                    {
                        if (series.bonus % 2 == 0)
                        {
                            return GetGoldBundle(card, series.bonus >= 4 ? 20 : 10);
                        }
                        else
                        {
                            return GetMaxHPBundle(card, series.bonus >= 6 ? 2 : 1);
                        }
                    }
                }
            }
        }

        return null;
    }

    private CardRewardBundle GetCursedRelicBundle(AbstractCard card)
    {
        return new CardRewardBundle(card, c -> GameEffects.Queue.ObtainRelic(new CursedGlyph()))
                .SetIcon(CURSED_GLYPH.img, -AbstractCard.RAW_W * 0.45f, -AbstractCard.RAW_H * 0.52f)
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

    private void ReceiveGold(CardRewardBundle bundle)
    {
        for (AnimatorRuntimeLoadout series : GR.Animator.Dungeon.Loadouts)
        {
            if (series.Cards.containsKey(bundle.card.cardID))
            {
                CardCrawlGame.sound.play("GOLD_GAIN");
                AbstractDungeon.player.gainGold(bundle.amount);
                series.bonus += 1;

                AbstractMissingPiece.RefreshDescription();
                JUtils.LogInfo(this, "Obtained Gold Bonus (+" + bundle.amount + "): " + bundle.card.cardID);
                return;
            }
        }
    }

    private void ReceiveMaxHP(CardRewardBundle bundle)
    {
        for (AnimatorRuntimeLoadout series : GR.Animator.Dungeon.Loadouts)
        {
            if (series.Cards.containsKey(bundle.card.cardID))
            {
                AbstractDungeon.player.increaseMaxHp(bundle.amount, true);
                series.bonus += 1;

                AbstractMissingPiece.RefreshDescription();
                JUtils.LogInfo(this, "Obtained Max HP Bonus (+" + bundle.amount + "): " + bundle.card.cardID);
                return;
            }
        }
    }
}
