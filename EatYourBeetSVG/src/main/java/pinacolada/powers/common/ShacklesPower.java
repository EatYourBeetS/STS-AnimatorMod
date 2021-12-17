package pinacolada.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import pinacolada.effects.SFX;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class ShacklesPower extends PCLPower
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
        PCLActions.Top.StackPower(new StrengthPower(owner, -difference));

        super.onAmountChanged(previousAmount, difference);
    }

    @Override
    public void duringTurn()
    {
        super.duringTurn();

        final AbstractMonster m = PCLJUtils.SafeCast(owner, AbstractMonster.class);
        if (m != null && !PCLGameUtilities.IsAttacking(m.intent))
        {
            PCLActions.Top.RemovePower(owner, this);
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        RemovePower();
    }
}