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

    private int baseEvadePercent = 35;
    private int decayPerTurn = 5;
    private int maxEvadePercent = 50;

    public PhasingPower(AbstractCreature owner, int timesToStack)
    {
        super(owner, POWER_ID);

        this.amount = baseEvadePercent;
        this.type = PowerType.BUFF;
        this.priority = 99;

        for (int i=0; i<timesToStack-1; i++)
        {
            stackPower(1);
        }

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount)
    {
        baseEvadePercent = Math.min(maxEvadePercent, baseEvadePercent + 5);
        this.amount = baseEvadePercent;
        this.fontScale = 8f;
        this.updateDescription();
    }

    public void playApplyPowerSfx()
    {
        CardCrawlGame.sound.playA("ORB_PLASMA_CHANNEL", 2);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        if (enabled && amount > 0)
        {
            reducePower(decayPerTurn);

            if (amount <= 0)
            {
                GameActions.Bottom.RemovePower(owner, owner, this);
            }
        }

        this.updateDescription();
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount)
    {
        if (info.type == DamageInfo.DamageType.NORMAL)
        {
            Random phaseRNG = AbstractDungeon.miscRng;

            if (phaseRNG.random(100) < amount)
            {
                //Phased!
                GameActions.Bottom.SFX("ORB_PLASMA_CHANNEL", 1.5f);
                flashWithoutSound();
                info.output = damageAmount = 0;
            }
        }

        return super.onAttackedToChangeDamage(info, damageAmount);
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + amount + desc[1] + baseEvadePercent + desc[2];
    }
}
