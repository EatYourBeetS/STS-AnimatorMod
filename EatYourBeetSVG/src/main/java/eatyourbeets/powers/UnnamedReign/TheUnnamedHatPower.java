package eatyourbeets.powers.UnnamedReign;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EntanglePower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;

public class TheUnnamedHatPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(TheUnnamedHatPower.class.getSimpleName());

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
            GameActionsHelper.ApplyPower(null, p, new EntanglePower(p));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
        {
            if (m.currentHealth < m.maxHealth)
            {
                m.heal(amount);
            }
        }
    }
}
