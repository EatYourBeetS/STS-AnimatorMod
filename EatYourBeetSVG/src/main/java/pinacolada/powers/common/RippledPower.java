package pinacolada.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.Mathf;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.powers.PCLTriggerablePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class RippledPower extends PCLTriggerablePower
{
    private static final Color healthBarColor = new Color(0.55F, 0.25F, 1.00F, 1.0F);
    public static final String POWER_ID = CreateFullID(RippledPower.class);
    public static final int SPLASH_MULTIPLIER = 25;

    public static float CalculateDamage(DamageInfo info, float multiplier)
    {
        float newDamage = Mathf.Max(1, info.output * (multiplier / 100f));
        return PCLGameUtilities.IsPlayer(info.owner) ? Math.min(PCLGameUtilities.GetHP(info.owner, true, true) - 1, newDamage) : newDamage;
    }

    public RippledPower(AbstractCreature owner, AbstractCreature source, int amount)
    {
        super(owner, source, POWER_ID, SPLASH_MULTIPLIER);

        this.priority = 4;

        Initialize(amount, PowerType.DEBUFF, true);
    }


    @Override
    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.PCL_PING, 0.75f, 0.85f);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.type == DamageInfo.DamageType.NORMAL)
        {
            if (PCLGameUtilities.IsPlayer(owner)) {
                PCLActions.Bottom.DealDamage(owner, owner, (int) CalculateDamage(info, GetEffectMultiplier()), DamageInfo.DamageType.THORNS, AttackEffects.WAVE);
            }
            else {
                for (AbstractMonster enemy : PCLGameUtilities.GetEnemies(true)) {
                    if (enemy != owner) {
                        PCLActions.Bottom.DealDamage(owner, enemy, (int) CalculateDamage(info, GetEffectMultiplier()), DamageInfo.DamageType.THORNS, AttackEffects.WAVE);
                    }
                }
            }
        }

        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(owner.isPlayer ? 1 : 0, GetPassiveDamage(), GetEffectMultiplier());
    }

    @Override
    public AbstractGameAction.AttackEffect GetAttackEffect() {
        return AttackEffects.WAVE;
    }

    @Override
    public Color getColor()
    {
        return healthBarColor;
    }
}
