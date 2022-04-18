package eatyourbeets.rewards.animator;

import basemod.BaseMod;
import basemod.abstracts.CustomReward;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.rewards.RewardSave;
import eatyourbeets.resources.GR;
import eatyourbeets.rewards.AnimatorReward;
import eatyourbeets.utilities.JUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.StringJoiner;

public class SpecialCardReward extends AnimatorReward
{
    public static final String ID = CreateFullID(SpecialCardReward.class);

    public SpecialCardReward(ArrayList<AbstractCard> cards)
    {
        this(TEXT[2], cards);
    }

    public SpecialCardReward(String text, ArrayList<AbstractCard> cards)
    {
        super(ID, text, GR.Enums.Rewards.SPECIAL_CARDS);

        this.cards = cards;
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

    public static class Serializer implements BaseMod.LoadCustomReward, BaseMod.SaveCustomReward
    {
        @Override
        public CustomReward onLoad(RewardSave rewardSave)
        {
            final ArrayList<AbstractCard> cards = new ArrayList<>();
            if (StringUtils.isNotEmpty(rewardSave.id))
            {
                final String[] data = Deserialize(rewardSave.id);
                for (int i = 1; i < data.length; i++)
                {
                    final String cardID = JUtils.SplitString("+", data[i])[0];
                    final AbstractCard card = CardLibrary.getCopy(cardID, data[i].endsWith("+") ? 1 : 0, 0);
                    cards.add(card);
                }

                return new SpecialCardReward(data[0], cards);
            }

            return new SpecialCardReward("Error", cards);
        }

        @Override
        public RewardSave onSave(CustomReward customReward)
        {
            final SpecialCardReward reward = JUtils.SafeCast(customReward, SpecialCardReward.class);
            if (reward != null)
            {
                return new RewardSave(reward.type.toString(), Serialize(reward.text, reward.cards));
            }

            return null;
        }

        public String[] Deserialize(String data)
        {
            return JUtils.SplitString("|", data);
        }

        public String Serialize(String text, ArrayList<AbstractCard> cards)
        {
            final StringJoiner sj = new StringJoiner("|");
            sj.add(text);
            for (AbstractCard c : cards)
            {
                sj.add(c.upgraded ? (c.cardID + "+") : c.cardID);
            }

            return sj.toString();
        }
    }
}