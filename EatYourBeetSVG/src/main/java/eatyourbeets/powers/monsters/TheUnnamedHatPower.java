package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class TheUnnamedHatPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(TheUnnamedHatPower.class);
    public static final int DEBUFF_AMOUNT_1 = 8;
    public static final int DEBUFF_AMOUNT_2 = 2;

    public TheUnnamedHatPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, DEBUFF_AMOUNT_1, DEBUFF_AMOUNT_2);
    }

    @Override
    public void onDeath()
    {
        super.onDeath();

        GameActions.Last.Callback(() ->
        {
            if (GameUtilities.IsPlayerTurn(true))
            {
                GameActions.Bottom.GainTemporaryStats(-DEBUFF_AMOUNT_1, -DEBUFF_AMOUNT_1, -DEBUFF_AMOUNT_1);
            }
            else
            {
                GameActions.Bottom.GainTemporaryStats(-DEBUFF_AMOUNT_2, -DEBUFF_AMOUNT_2, -DEBUFF_AMOUNT_2);
            }
        });
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        int healAmount = 0;
        for (AbstractMonster m : GameUtilities.GetEnemies(true))
        {
            TheUnnamedHatPower power = GameUtilities.GetPower(m, TheUnnamedHatPower.class);
            if (power != null)
            {
                healAmount += power.amount;
            }
        }

        if (healAmount > 0 && owner.currentHealth < owner.maxHealth)
        {
            GameActions.Bottom.Heal(owner, owner, healAmount);
        }
    }
}
