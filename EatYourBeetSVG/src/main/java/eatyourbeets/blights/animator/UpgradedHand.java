package eatyourbeets.blights.animator;

import basemod.BaseMod;
import eatyourbeets.blights.AnimatorBlight;

public class UpgradedHand extends AnimatorBlight
{
    public static final String ID = CreateFullID(UpgradedHand.class);

    public UpgradedHand()
    {
        this(1);
    }

    public UpgradedHand(int turns)
    {
        super(ID, turns);
    }

    @Override
    public void onEquip()
    {
        BaseMod.MAX_HAND_SIZE += this.counter;
    }

    public void addAmount(int amount)
    {
        this.counter += amount;
        BaseMod.MAX_HAND_SIZE += amount;
    }

    public void reset()
    {
        BaseMod.MAX_HAND_SIZE -= this.counter;
        this.counter = 0;
    }
}