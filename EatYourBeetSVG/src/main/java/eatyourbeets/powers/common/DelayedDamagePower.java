package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.ui.animator.combat.CombatHelper;
import eatyourbeets.utilities.GameActions;

public class DelayedDamagePower extends CommonPower implements HealthBarRenderPower
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
        GameActions.Bottom.TakeDamage(owner, amount, attackEffect);
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
}