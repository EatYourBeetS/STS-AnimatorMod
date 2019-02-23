package eatyourbeets.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class AlbedoPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(AlbedoPower.class.getSimpleName());

    private int stacks;

    public AlbedoPower(AbstractPlayer owner, int resistance)
    {
        super(owner, POWER_ID);
        this.amount = resistance;
        this.stacks = 1;

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount)
    {
        this.stacks += 1;
        this.amount += (stackAmount / this.stacks);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType)
    {
        return damage * ((100 - this.amount) / 100f);
    }
}
