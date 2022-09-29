package eatyourbeets.relics.animator;

import eatyourbeets.relics.AnimatorRelic;

public class NeowsComplaint extends AnimatorRelic
{
    public static final String ID = CreateFullID(NeowsComplaint.class);
    public static final int TOTAL_REWARDS = 2;
    public static final int CARD_BONUS = 2;

    public NeowsComplaint()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.MAGICAL);

        setCounter(TOTAL_REWARDS);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, TOTAL_REWARDS);
    }

    @Override
    public int changeNumberOfCardsInReward(int numberOfCards)
    {
        int amount = super.changeNumberOfCardsInReward(numberOfCards);
        if (counter > 0)
        {
            AddCounter(-1);
            amount += CARD_BONUS;
            flash();
        }

        return amount;
    }

    @Override
    public void setCounter(int counter)
    {
        super.setCounter(counter);

        SetEnabled(counter > 0);
    }
}