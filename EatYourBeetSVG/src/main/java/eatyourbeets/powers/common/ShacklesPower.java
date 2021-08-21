package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class ShacklesPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(ShacklesPower.class);

    public ShacklesPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.loadRegion("shackle");
        this.powerIcon = this.region48;

        Initialize(amount, PowerType.DEBUFF, false);
    }

    @Override
    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.POWER_SHACKLE, 0.95F, 1.05f);
    }

    @Override
    protected void onAmountChanged(int previousAmount, int difference)
    {
        GameActions.Top.StackPower(new StrengthPower(owner, -difference));

        super.onAmountChanged(previousAmount, difference);
    }

    @Override
    public void duringTurn()
    {
        super.duringTurn();

        final AbstractMonster m = JUtils.SafeCast(owner, AbstractMonster.class);
        if (m != null && !GameUtilities.IsAttacking(m.intent))
        {
            GameActions.Top.RemovePower(owner, this);
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        RemovePower();
    }
}