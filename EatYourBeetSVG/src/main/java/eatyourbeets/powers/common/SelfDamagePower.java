package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.ui.animator.combat.CombatHelper;
import eatyourbeets.utilities.GameActions;

public class SelfDamagePower extends CommonPower implements HealthBarRenderPower
{
    private static final Color healthBarColor = Color.PURPLE.cpy();

    public static final String POWER_ID = CreateFullID(SelfDamagePower.class);

    public SelfDamagePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.priority = 97;

        Initialize(amount, PowerType.DEBUFF, false);
    }

    @Override
    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.BLOOD_SPLAT, 0.95f, 1.05f);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        GameActions.Bottom.DealDamage(null, owner, amount, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_VERTICAL);

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