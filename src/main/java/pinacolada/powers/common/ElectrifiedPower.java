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

import java.util.ArrayList;

public class ElectrifiedPower extends PCLTriggerablePower
{
    private static final Color healthBarColor = new Color(1.0F, 0.9F, 0.25F, 1.0F);
    public static final String POWER_ID = CreateFullID(ElectrifiedPower.class);
    public static final int SPLASH_MULTIPLIER = 25;

    public static float CalculateDamage(DamageInfo info, float multiplier)
    {
        float newDamage = Mathf.Max(1, info.output * (multiplier / 100f));
        return PCLGameUtilities.IsPlayer(info.owner) ? Math.min(PCLGameUtilities.GetHP(info.owner, true, true) - 1, newDamage) : newDamage;
    }

    public ElectrifiedPower(AbstractCreature owner, AbstractCreature source, int amount)
    {
        super(owner, source, POWER_ID, SPLASH_MULTIPLIER);

        this.priority = 4;

        Initialize(amount, PowerType.DEBUFF, true);
    }


    @Override
    public void playApplyPowerSfx()
    {
        SFX.Play(SFX.ORB_LIGHTNING_PASSIVE, 0.95f, 1.05f);
    }

    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.output > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL) {
            ArrayList<AbstractMonster> enemies = PCLGameUtilities.GetEnemies(true);
            if (PCLGameUtilities.IsPlayer(owner) || enemies.size() == 1) {
                PCLActions.Bottom.DealDamage(owner, owner, (int) CalculateDamage(info, GetEffectMultiplier()), DamageInfo.DamageType.THORNS, AttackEffects.SPARK);
            }
            else {
                for (AbstractMonster enemy : enemies) {
                    if (enemy != owner) {
                        PCLActions.Bottom.DealDamage(owner, enemy, (int) CalculateDamage(info, GetEffectMultiplier()), DamageInfo.DamageType.THORNS, AttackEffects.SPARK);
                    }
                }
            }
            this.flash();
        }
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(owner.isPlayer ? 1 : 0, GetPassiveDamage(), GetEffectMultiplier());
    }

    @Override
    public AbstractGameAction.AttackEffect GetAttackEffect() {
        return AttackEffects.SPARK;
    }

    @Override
    public Color getColor()
    {
        return healthBarColor;
    }
}
