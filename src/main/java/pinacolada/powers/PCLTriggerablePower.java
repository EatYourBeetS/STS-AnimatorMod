package pinacolada.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.utilities.ColoredString;
import pinacolada.actions.damage.LoseHP;
import pinacolada.powers.special.ElementalExposurePower;
import pinacolada.ui.combat.CombatHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public abstract class PCLTriggerablePower extends PCLPower implements HealthBarRenderPower
{
    protected final int defaultMultiplier;
    protected final int maxMultiplier;

    public PCLTriggerablePower(AbstractCreature owner, AbstractCreature source, String id, int defaultMultiplier)
    {
        this(owner, source, id, defaultMultiplier, 9999);
    }

    public PCLTriggerablePower(AbstractCreature owner, AbstractCreature source, String id, int defaultMultiplier, int maxMultiplier)
    {
        super(owner, id);

        this.source = source;
        this.defaultMultiplier = defaultMultiplier;
        this.maxMultiplier = maxMultiplier;
        Initialize(amount, PowerType.DEBUFF, true);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, GetPassiveDamage(), GetEffectMultiplier());
    }

    @Override
    protected ColoredString GetSecondaryAmount(Color c)
    {
        return new ColoredString((int) GetEffectMultiplier(), Color.RED, c.a);
    }


    @Override
    public int getHealthBarAmount()
    {
        return CombatHelper.GetHealthBarAmount(owner, GetPassiveDamage(), false, true);
    }

    @Override
    public void atStartOfTurn()
    {
        this.flashWithoutSound();

        PCLActions.Bottom.Add(Trigger());
        ReducePower(1);
    }

    public LoseHP Trigger() {
        return new LoseHP(owner, source, GetPassiveDamage(), GetAttackEffect()).CanKill(owner == null || !owner.isPlayer);
    }

    public int GetPassiveDamage()
    {
        return MathUtils.ceil((amount / 2f) * GetDamageMultiplier());
    }

    public float GetDamageMultiplier()
    {
        return ((PCLGameUtilities.IsPlayer(owner)) ? 100 : (100 + PCLCombatStats.GetPassiveDamageBonus(ID))) / 100f * GetElementalExposure();
    }

    public float GetEffectMultiplier()
    {
        return (PCLGameUtilities.IsPlayer(owner)) ? defaultMultiplier : Math.min(maxMultiplier, defaultMultiplier + PCLCombatStats.GetEffectBonus(ID)) * GetElementalExposure();
    }

    protected float GetElementalExposure() {
        return ElementalExposurePower.CalculatePercentage(PCLGameUtilities.GetPowerAmount(owner, ElementalExposurePower.POWER_ID));
    }

    public abstract AbstractGameAction.AttackEffect GetAttackEffect();
}
