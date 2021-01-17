package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public class PhasingPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(PhasingPower.class);

    int evadePercent = 0;

    public PhasingPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        this.type = PowerType.BUFF;

        updateEvadePercent();
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);
        updateEvadePercent();
        this.updateDescription();
    }

    public void playApplyPowerSfx()
    {
        CardCrawlGame.sound.playA("ORB_PLASMA_CHANNEL", 2);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.type == DamageInfo.DamageType.NORMAL)
        {
            Random phaseRNG = AbstractDungeon.miscRng;

            if (phaseRNG.random(100) < evadePercent)
            {
                //Phased!
                GameActions.Bottom.SFX("ORB_FROST_Evoke", 1.5f);

                return 0;
            }
        }

        return damageAmount;
    }

    private void updateEvadePercent()
    {
        evadePercent = Math.min(30 + (amount * 5), 50);
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + evadePercent + desc[1];
    }
}
