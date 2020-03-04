package eatyourbeets.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.daily.mods.Binary;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.interfaces.subscribers.OnAddingToCardReward;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.WeightedList;

import java.util.ArrayList;

public abstract class AnimatorReward extends CustomReward
{
    public static String CreateFullID(String id)
    {
        return GR.Animator.CreateID(id);
    }

    public static float GetUltraRareChance()
    {
        float bonus = 1;
        int level = GR.Animator.Data.SpecialTrophies.Trophy1;
        if (level > 0)
        {
            bonus += level / (level + 100f);
        }

        return 4f * bonus;
    }

    public AnimatorReward(String id, String text, RewardType type)
    {
        super(new Texture(GR.GetRewardImage(id)), text, type);
    }

    public AnimatorReward(Texture rewardImage, String text, RewardType type)
    {
        super(rewardImage, text, type);
    }

    public ArrayList<AbstractCard> GenerateCardReward(Synergy synergy)
    {
        RewardContext context = new RewardContext(synergy);
        WeightedList<AbstractCard> randomPool = new WeightedList<>();
        if (synergy != null && synergy != Synergies.ANY)
        {
            AddCards(AbstractDungeon.srcCommonCardPool, randomPool, context);
            AddCards(AbstractDungeon.srcUncommonCardPool, randomPool, context);
            AddCards(AbstractDungeon.srcRareCardPool, randomPool, context);
        }
        else
        {
            AddCards(AbstractDungeon.srcColorlessCardPool, randomPool, context);
        }

        ArrayList<AbstractCard> result = new ArrayList<>();
        while (result.size() < context.rewardSize && randomPool.Size() > 0)
        {
            AbstractCard card = randomPool.Retrieve(AbstractDungeon.cardRng);
            if (card instanceof OnAddingToCardReward && ((OnAddingToCardReward) card).ShouldCancel(this))
            {
                continue;
            }

            context.AddCard(card.makeCopy(), result);
        }

        if (result.size() > 0)
        {
            AddUltraRare(result, context.synergy);
        }

        return result;
    }

    private void AddCards(CardGroup pool, WeightedList<AbstractCard> cards, RewardContext context)
    {
        Synergy synergy = context.synergy;

        for (AbstractCard c : pool.group)
        {
            AnimatorCard card = JavaUtilities.SafeCast(c, AnimatorCard.class);
            if (card != null && (synergy.Equals(card.synergy) || Synergies.ANY.equals(synergy)))
            {
                if (Synergies.ANY.equals(synergy)) // colorless
                {
                    cards.Add(card, card.rarity == AbstractCard.CardRarity.UNCOMMON ? 8 : 2);
                }
                else if (synergy.equals(card.synergy))
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

    private void AddUltraRare(ArrayList<AbstractCard> cards, Synergy synergy)
    {
        int currentLevel = UnlockTracker.getUnlockLevel(GR.Enums.Characters.THE_ANIMATOR);
        if (currentLevel <= 2 || AbstractDungeon.floorNum < 8 || AbstractDungeon.floorNum > 36)
        {
            return;
        }

        float chances = GetUltraRareChance();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
        {
            if (c instanceof AnimatorCard_UltraRare)
            {
                if (synergy.ID == ((AnimatorCard_UltraRare) c).synergy.ID)
                {
                    return; // No duplicates
                }
                else
                {
                    chances *= 0.5f;
                }
            }
        }

        float roll = AbstractDungeon.cardRng.random(100f);
        if (roll > chances)
        {
            return;
        }

        AnimatorLoadout loadout = GR.Animator.Data.GetByName(synergy.Name);
        if (loadout != null)
        {
            AbstractCard ultraRare = loadout.GetUltraRare();
            cards.remove(0);
            cards.add(ultraRare);
        }
    }

    private static class RewardContext
    {
        public Synergy synergy;
        public int rewardSize;
        public int rareCardChance;
        public int uncommonCardChance;
        public int commonCardChance;

        public RewardContext(Synergy synergy)
        {
            this.synergy = synergy;
            this.rewardSize = 3;
            this.rareCardChance = 3;
            this.uncommonCardChance = 37;
            this.commonCardChance = 60;

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