package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import eatyourbeets.cards.animator.special.TacticalRetreat;
import eatyourbeets.cards.animator.status.Overheat;
import eatyourbeets.interfaces.listeners.OnTryApplyPowerListener;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class UltimateShapePower extends AnimatorPower implements OnTryApplyPowerListener
{
    public static final String POWER_ID = CreateFullID(UltimateShapePower.class);

    protected boolean tacticalRetreat = false;

    public UltimateShapePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public boolean TryApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source, AbstractGameAction action)
    {
        if (target == owner && GameUtilities.IsCommonDebuff(power))
        {
            action.amount += power.amount;
            power.amount += power.amount;
            flashWithoutSound();
        }

        return true;
    }

    @Override
    public void atEndOfRound()
    {
        super.atEndOfRound();

        GameActions.Bottom.MakeCardInDiscardPile(new Overheat())
        .Repeat(owner.currentHealth < amount ? 3 : 1);
        flashWithoutSound();

        if (!tacticalRetreat)
        {
            final int turn = CombatStats.TurnCount(true);
            if (GameUtilities.GetHealthPercentage(owner) < 0.6f || turn > 18)
            {
                GameActions.Bottom.MakeCardInHand(new TacticalRetreat());
                tacticalRetreat = true;
            }
            else if (turn > 14)
            {
                GameActions.Bottom.StackPower(new GainStrengthPower(owner, 2))
                .ShowEffect(false, true)
                .IgnoreArtifact(true);
            }
        }
    }
}
