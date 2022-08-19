package eatyourbeets.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.daily.mods.Binary;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.cards.animator.colorless.uncommon.QuestionMark;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.interfaces.listeners.OnAddingToCardRewardListener;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.WeightedList;

import java.util.ArrayList;

public abstract class AnimatorReward extends CustomReward
{
    public static String CreateFullID(Class<? extends AnimatorReward> type)
    {
        return GR.Animator.CreateID(type.getSimpleName());
    }

    public AnimatorReward(String id, String text, RewardType type)
    {
        this(new Texture(GR.GetRewardImage(id)), text, type);
    }

    public AnimatorReward(Texture rewardImage, String text, RewardType type)
    {
        super(rewardImage, text, type);
    }

    public ArrayList<AbstractCard> GenerateCardReward(CardSeries series)
    {
        final RewardContext context = new RewardContext(series);
        final WeightedList<AbstractCard> randomPool = new WeightedList<>();
        if (series != null && series != CardSeries.ANY)
        {
            AddCards(AbstractDungeon.srcCommonCardPool, randomPool, context);
            AddCards(AbstractDungeon.srcUncommonCardPool, randomPool, context);
            AddCards(AbstractDungeon.srcRareCardPool, randomPool, context);
        }
        else
        {
            AddCards(AbstractDungeon.srcColorlessCardPool, randomPool, context);
        }

        final ArrayList<AbstractCard> result = new ArrayList<>();
        while (result.size() < context.rewardSize && randomPool.Size() > 0)
        {
            final AbstractCard card = randomPool.Retrieve(AbstractDungeon.cardRng);
            if (card instanceof OnAddingToCardRewardListener && ((OnAddingToCardRewardListener) card).ShouldCancel()
            || (card instanceof EYBCard && ((EYBCard) card).cardData.ShouldCancel()))
            {
                continue;
            }

            context.AddCard(card.makeCopy(), result);
        }

        if (result.size() > 0)
        {
            AnimatorUltrarareGenerator.TryAdd(result, context.series);
        }
        else
        {
            context.AddCard(new QuestionMark(), result);
        }

        return result;
    }

    private void AddCards(CardGroup pool, WeightedList<AbstractCard> cards, RewardContext context)
    {
        final CardSeries series = context.series;
        for (AbstractCard c : pool.group)
        {
            AnimatorCard card = JUtils.SafeCast(c, AnimatorCard.class);
            if (card != null && (series.Equals(card.series) || CardSeries.ANY.equals(series)))
            {
                if (CardSeries.ANY.equals(series)) // colorless
                {
                    cards.Add(card, card.rarity == AbstractCard.CardRarity.UNCOMMON ? 8 : 2);
                }
                else if (series.equals(card.series))
                {
                    int weight = context.GetRarityWeight(card.rarity);
                    if (weight > 0)
                    {
                        cards.Add(card, weight);
                    }
                }
            }
        }
    }

    private static class RewardContext
    {
        public CardSeries series;
        public int rewardSize;
        public int rareCardChance;
        public int uncommonCardChance;
        public int commonCardChance;

        public RewardContext(CardSeries series)
        {
            this.series = series;
            this.rewardSize = 3;

            if (GameUtilities.GetAscensionLevel() < 12)
            {
                this.rareCardChance = 3;
                this.uncommonCardChance = 37;
                this.commonCardChance = 60;
            }
            else
            {
                this.rareCardChance = 2;
                this.uncommonCardChance = 33;
                this.commonCardChance = 65;
            }

            for (AbstractRelic relic : AbstractDungeon.player.relics)
            {
                this.rewardSize = relic.changeNumberOfCardsInReward(rewardSize);
                this.uncommonCardChance = relic.changeUncommonCardRewardChance(uncommonCardChance);
                this.rareCardChance = relic.changeRareCardRewardChance(rareCardChance);
            }

            if (ModHelper.isModEnabled(Binary.ID))
            {
                this.rewardSize -= 1;
            }
        }

        public int GetRarityWeight(AbstractCard.CardRarity rarity)
        {
            switch (rarity)
            {
                case COMMON:
                    return commonCardChance;

                case UNCOMMON:
                    return uncommonCardChance;

                case RARE:
                    return rareCardChance;

                default:
                    return 0;
            }
        }

        public void AddCard(AbstractCard card, ArrayList<AbstractCard> rewards)
        {
            for (AbstractRelic relic : AbstractDungeon.player.relics)
            {
                relic.onPreviewObtainCard(card);
            }

            rewards.add(card);
        }
    }
}