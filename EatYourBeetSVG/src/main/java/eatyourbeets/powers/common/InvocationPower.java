package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.cards.animator.ultrarare.SummoningRitual;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.*;

public class InvocationPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(InvocationPower.class);
    public static final int MAX_CHARGE = 12;
    public static final int THRESHOLD_STEP = 3;

    protected int threshold = THRESHOLD_STEP;
    protected int charge = 0;

    public InvocationPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.ATTACK_FLAME_BARRIER, 0.75f, 0.85f, 0.85f);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, THRESHOLD_STEP, MAX_CHARGE);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        ObtainCard();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        if (enabled && amount > 0)
        {
            charge = Mathf.Min(MAX_CHARGE, charge + amount);

            boolean playSound = false;
            while (charge >= threshold)
            {
                ObtainCard();
                playSound = true;

                if (threshold >= MAX_CHARGE)
                {
                    threshold = MAX_CHARGE;
                    SetEnabled(false);
                    return;
                }
                else
                {
                    threshold += THRESHOLD_STEP;
                }
            }

            if (playSound)
            {
                flash();
            }
            else
            {
                flashWithoutSound();
            }
        }
    }

    @Override
    protected ColoredString GetSecondaryAmount(Color c)
    {
        return new ColoredString(charge, Colors.Lerp(Color.LIGHT_GRAY, Settings.PURPLE_COLOR, charge / (float)MAX_CHARGE, c.a));
    }

    @Override
    public AbstractPower makeCopy()
    {
        final InvocationPower copy = new InvocationPower(owner, amount);
        copy.charge = charge;
        copy.threshold = threshold;
        return copy;
    }

    protected void ObtainCard()
    {
        if (charge >= MAX_CHARGE)
        {
            if (CombatStats.TryActivateLimited(POWER_ID))
            {
                GameActions.Bottom.MakeCardInHand(new SummoningRitual());
            }
        }
        else
        {
            GameActions.Top.MakeCardInDrawPile(new Crystallize())
            .SetDestination(CardSelection.Top)
            .ShowEffect(true);
        }
    }
}
