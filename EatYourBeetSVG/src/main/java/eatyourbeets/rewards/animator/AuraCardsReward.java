package eatyourbeets.rewards.animator;

import basemod.BaseMod;
import basemod.abstracts.CustomReward;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardSave;
import eatyourbeets.cards.animator.auras.Aura1;
import eatyourbeets.cards.animator.auras.Aura2;
import eatyourbeets.cards.animator.auras.Aura3;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.rewards.AnimatorReward;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class AuraCardsReward extends AnimatorReward
{
    public static final String ID = CreateFullID(AuraCardsReward.class);

    public AuraCardsReward()
    {
        super(ID, GR.Animator.Strings.Rewards.AuraEffects, GR.Enums.Rewards.AURA_CARDS);

        this.cards = GenerateCardReward();
    }

    public static void TryAddReward(AbstractPlayer p, ArrayList<RewardItem> rewards)
    {
        if (AbstractDungeon.actNum == 1 && GameUtilities.InBossRoom())
        {
            AuraCardsReward reward = new AuraCardsReward();
            if (reward.cards.size() > 0)
            {
                rewards.add(reward);
            }
        }
    }

    @Override
    public boolean claimReward()
    {
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD)
        {
            AbstractDungeon.cardRewardScreen.open(this.cards, this, TEXT[4]);
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }

        this.isDone = false;
        return false;
    }

    protected ArrayList<AbstractCard> GenerateCardReward()
    {
        final ArrayList<AbstractCard> cards = new ArrayList<>();
        final AbstractPlayer p = CombatStats.RefreshPlayer();

        cards.add(new Aura1());
        cards.add(new Aura2());
        cards.add(new Aura3());

        for (AbstractCard c1 : p.masterDeck.group)
        {
            for (int i = 0; i < cards.size(); i++)
            {
                if (cards.get(i).cardID.equals(c1.cardID))
                {
                    cards.remove(i);
                    break;
                }
            }
        }

        return cards;
    }

    public static class Serializer implements BaseMod.LoadCustomReward, BaseMod.SaveCustomReward
    {
        @Override
        public CustomReward onLoad(RewardSave rewardSave)
        {
            return new AuraCardsReward();
        }

        @Override
        public RewardSave onSave(CustomReward customReward)
        {
            AuraCardsReward reward = JUtils.SafeCast(customReward, AuraCardsReward.class);
            if (reward != null)
            {
                return new RewardSave(reward.type.toString(), ID);
            }

            return null;
        }
    }
}