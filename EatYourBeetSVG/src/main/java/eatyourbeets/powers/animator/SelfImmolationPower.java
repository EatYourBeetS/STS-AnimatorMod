package eatyourbeets.powers.animator;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SelfImmolationPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(SelfImmolationPower.class);

    public SelfImmolationPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        if (this.amount >= 9999)
        {
            this.amount = 9999;
        }
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;

        updateDescription();
    }

    @Override
    public void onGainedBlock(float blockAmount) {
        ApplyDebuff(MathUtils.floor(blockAmount * 2));
    }

    @Override
    public int onPlayerGainedBlock(float blockAmount)
    {
        ApplyDebuff(MathUtils.floor(blockAmount * 2));
        return super.onPlayerGainedBlock(blockAmount);
    }

    @Override
    public int onPlayerGainedBlock(int blockAmount)
    {
        ApplyDebuff(blockAmount * 2);
        return super.onPlayerGainedBlock(blockAmount);
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();
        GameActions.Bottom.ReducePower(this, 1);
    }

    private void ApplyDebuff(int amount) {
        if (amount > 0) {
            for (AbstractCreature cr : GameUtilities.GetAllCharacters(true)) {
                GameActions.Bottom.DealDamageAtEndOfTurn(owner, cr, amount, AttackEffects.CLAW);
            }
        }
    }
}
