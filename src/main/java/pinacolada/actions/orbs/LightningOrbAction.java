package pinacolada.actions.orbs;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import eatyourbeets.actions.EYBAction;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.powers.common.RippledPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class LightningOrbAction extends EYBAction
{
    private final AbstractOrb orb;
    private final boolean hitAll;

    public LightningOrbAction(AbstractOrb orb, int damage, boolean hitAll) {
        super(ActionType.DAMAGE);
        Initialize(damage);

        this.orb = orb;
        this.hitAll = hitAll;
    }

    public void update() {
        if (!this.hitAll) {
            AbstractMonster enemy = null;

            for (AbstractMonster m : PCLGameUtilities.GetEnemies(true))
            {
                if (m.hasPower(LockOnPower.POWER_ID))
                {
                    enemy = m;
                    break;
                }
            }

            if (enemy == null) {
                enemy = PCLGameUtilities.GetRandomEnemy(true);
            }

            if (enemy != null)
            {
                int actualDamage = AbstractOrb.applyLockOn(enemy, amount);
                if (actualDamage > 0)
                {
                    PCLActions.Top.DealDamage(source, enemy, actualDamage, DamageInfo.DamageType.THORNS, AttackEffects.LIGHTNING)
                            .SetVFX(Settings.FAST_MODE, false)
                            .SetPCLAttackType(PCLAttackType.Electric, true);
                    ActivateRippled(enemy, actualDamage);
                }
            }
        } else {
            int[] damage = DamageInfo.createDamageMatrix(amount, true, true);
            PCLActions.Top.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AttackEffects.LIGHTNING)
                    .SetPCLAttackType(PCLAttackType.Electric, true)
                    .SetVFX(Settings.FAST_MODE, true);
            PCLActions.Top.SFX(SFX.ORB_LIGHTNING_EVOKE);
            for (AbstractMonster enemy : PCLGameUtilities.GetEnemies(true)) {
                int actualDamage = AbstractOrb.applyLockOn(enemy, amount);
                if (actualDamage > 0) {
                    ActivateRippled(enemy, actualDamage);
                }
            }
        }

        if (this.orb != null) {
            PCLActions.Bottom.VFX(new OrbFlareEffect(this.orb, OrbFlareEffect.OrbFlareColor.LIGHTNING), Settings.FAST_MODE ? 0.0F : 0.6F / (float) AbstractDungeon.player.orbs.size());
        }

        Complete();
    }

    protected void ActivateRippled(AbstractMonster mo, int damageAmount) {
        RippledPower rp = PCLGameUtilities.GetPower(mo, RippledPower.POWER_ID);
        if (rp != null) {
            rp.DoDamage(damageAmount);
        }
    }
}
