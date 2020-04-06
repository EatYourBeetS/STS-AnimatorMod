package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EntanglePower;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class TheUnnamedHatPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(TheUnnamedHatPower.class);

    public TheUnnamedHatPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + amount + desc[1];
    }

    @Override
    public void onDeath()
    {
        super.onDeath();

        GameActions.Bottom.ApplyPower(null, new EntanglePower(player));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        int healAmount = 0;
        for (AbstractMonster m : GameUtilities.GetAllEnemies(true))
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
