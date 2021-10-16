package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.powers.CommonPower;

public class GenesisPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(GenesisPower.class);

    public GenesisPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void onEnergyRecharge()
    {
        AbstractDungeon.player.gainEnergy(this.amount);
        this.flash();
    }
}
