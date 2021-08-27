package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.ui.animator.combat.CombatHelper;
import eatyourbeets.utilities.GameActions;

public class PoisonPlayerPower extends CommonPower implements HealthBarRenderPower
{
    public static final String POWER_ID = CreateFullID(PoisonPlayerPower.class);

    private static final Color healthBarColor = Color.valueOf("78c13c");
    private AbstractCreature source;

    public PoisonPlayerPower(AbstractCreature owner, AbstractCreature source, int amount)
    {
        super(owner, source, POWER_ID);

        this.loadRegion("poison");
        this.powerIcon = this.region48;

        Initialize(amount, PowerType.DEBUFF, true);
    }

    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.POWER_POISON, 0.95F, 1.05f);
    }

    public void atStartOfTurn()
    {
        this.flashWithoutSound();

        GameActions.Bottom.LoseHP(source, owner, amount, AttackEffect.POISON).CanKill(!owner.isPlayer);
        GameActions.Bottom.ReducePower(this, 1);
    }

    @Override
    public int getHealthBarAmount()
    {
        return CombatHelper.GetHealthBarAmount(owner, amount, false, true);
    }

    @Override
    public Color getColor()
    {
        return healthBarColor;
    }
}
