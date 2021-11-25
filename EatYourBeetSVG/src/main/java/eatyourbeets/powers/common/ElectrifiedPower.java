package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CommonTriggerablePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.Mathf;

import java.util.ArrayList;

public class ElectrifiedPower extends CommonTriggerablePower
{
    private static final Color healthBarColor = new Color(1.0F, 0.9F, 0.25F, 1.0F);
    public static final String POWER_ID = CreateFullID(ElectrifiedPower.class);
    public static final int SPLASH_MULTIPLIER = 25;

    public static float CalculateDamage(float damage, float multiplier)
    {
        return Mathf.Max(1, damage * (multiplier / 100f));
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
        if (damageAmount > 0 && target != this.owner && info.type == DamageInfo.DamageType.NORMAL) {
            ArrayList<AbstractMonster> enemies = GameUtilities.GetEnemies(true);
            if (GameUtilities.IsPlayer(owner) || enemies.size() == 1) {
                GameActions.Bottom.DealDamage(owner, owner, (int) CalculateDamage(damageAmount, GetEffectMultiplier()), DamageInfo.DamageType.THORNS, AttackEffects.SPARK);
            }
            else {
                for (AbstractMonster enemy : enemies) {
                    if (enemy != owner) {
                        GameActions.Bottom.DealDamage(owner, enemy, (int) CalculateDamage(damageAmount, GetEffectMultiplier()), DamageInfo.DamageType.THORNS, AttackEffects.SPARK);
                    }
                }
            }
            this.flash();
        }
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
