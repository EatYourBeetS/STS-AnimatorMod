package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.powers.replacement.AnimatorFrailPower;
import eatyourbeets.powers.replacement.AnimatorLockOnPower;
import eatyourbeets.powers.replacement.AnimatorVulnerablePower;
import eatyourbeets.powers.replacement.AnimatorWeakPower;
import eatyourbeets.utilities.ColoredString;

import java.util.UUID;

public class DesecrationPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(DesecrationPower.class);
    public static final int MULTIPLIER = 6;
    public static final int MULTIPLIER2 = 2;
    private static UUID battleID;
    public int charge;
    private int totalMultiplier;
    private int totalMultiplier2;


    public DesecrationPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        this.charge = 0;

        Initialize(amount);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        UpdatePercentage();
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, GetLargeMultiplier(this.charge), GetSmallMultiplier(this.charge));
    }

    @Override
    protected ColoredString GetSecondaryAmount(Color c)
    {
        return new ColoredString(charge, Color.WHITE, c.a);
    }

    @Override
    public void onRemove()
    {
        this.charge = 0;
        UpdatePercentage();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        if (power.type == PowerType.DEBUFF && !power.ID.equals(GainStrengthPower.POWER_ID) &&
                source == this.owner && !target.hasPower(ArtifactPower.POWER_ID))
        {
            this.charge += amount;
            UpdatePercentage();
            this.flash();
        }
    }

    @Override
    protected void onAmountChanged(int previousAmount, int difference)
    {
        super.onAmountChanged(previousAmount, difference);
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        this.charge /= 4;
        UpdatePercentage();
    }

    private int GetLargeMultiplier(int charge) {
        return MULTIPLIER * charge;
    }

    private int GetSmallMultiplier(int charge) {
        return MULTIPLIER2 * charge;
    }

    public void UpdatePercentage()
    {
        //Undo the previous changes made by this power
        AnimatorLockOnPower.AddEnemyModifier(-this.totalMultiplier);
        AnimatorVulnerablePower.AddEnemyModifier(-this.totalMultiplier);
        AnimatorWeakPower.AddEnemyModifier(-this.totalMultiplier2);
        AnimatorFrailPower.AddEnemyModifier(-this.totalMultiplier2);

        this.totalMultiplier = GetLargeMultiplier(this.charge);
        this.totalMultiplier2 = GetSmallMultiplier(this.charge);

        AnimatorLockOnPower.AddEnemyModifier(this.totalMultiplier);
        AnimatorVulnerablePower.AddEnemyModifier(this.totalMultiplier);
        AnimatorWeakPower.AddEnemyModifier(this.totalMultiplier2);
        AnimatorFrailPower.AddEnemyModifier(this.totalMultiplier2);

        this.updateDescription();
    }
}
