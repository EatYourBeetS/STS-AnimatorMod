package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EntanglePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.powers.AnimatorPower;
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

        AbstractPlayer p = AbstractDungeon.player;
        if (!p.hasPower(EntanglePower.POWER_ID))
        {
            GameActions.Bottom.ApplyPower(null, p, new EntanglePower(p), 1);
        }
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
