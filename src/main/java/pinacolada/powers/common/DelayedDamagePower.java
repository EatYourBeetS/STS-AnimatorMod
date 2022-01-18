package pinacolada.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.powers.PCLPower;
import pinacolada.ui.combat.CombatHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class DelayedDamagePower extends PCLPower implements HealthBarRenderPower
{
    private static final Color healthBarColor = Color.PURPLE.cpy();

    public static final String POWER_ID = CreateFullID(DelayedDamagePower.class);

    private AbstractGameAction.AttackEffect attackEffect;

    public DelayedDamagePower(AbstractCreature owner, int amount)
    {
        this(owner, amount, AttackEffects.SLASH_VERTICAL);
    }

    public DelayedDamagePower(AbstractCreature owner, int amount, AbstractGameAction.AttackEffect attackEffect)
    {
        super(owner, POWER_ID);

        this.priority = 97;
        this.attackEffect = attackEffect;

        Initialize(amount, PowerType.DEBUFF, false);
    }

    @Override
    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.HEART_BEAT, 1.25f, 1.35f, 0.9f);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        int damageAmount = owner.isPlayer ? Math.max(0, Math.min(PCLGameUtilities.GetHP(owner, true, true) - 1, amount)) : amount;
        PCLActions.Bottom.TakeDamage(owner, damageAmount, attackEffect);
        RemovePower();

        playApplyPowerSfx();
        flashWithoutSound();

        super.atEndOfTurn(isPlayer);
    }

    @Override
    public int getHealthBarAmount()
    {
        return CombatHelper.GetHealthBarAmount(owner, amount, true, true);
    }

    @Override
    public Color getColor()
    {
        return healthBarColor;
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, owner.isPlayer ? powerStrings.DESCRIPTIONS[1] : "");
    }
}