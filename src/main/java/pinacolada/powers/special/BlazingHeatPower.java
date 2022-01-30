package pinacolada.powers.special;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.interfaces.subscribers.OnOrbPassiveEffectSubscriber;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.orbs.pcl.Fire;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import static pinacolada.cards.pcl.special.BlazingHeat.FIRE_EVOKE_BONUS;
import static pinacolada.cards.pcl.special.BlazingHeat.FIRE_TRIGGER_BONUS;

public class BlazingHeatPower extends PCLPower implements OnOrbPassiveEffectSubscriber
{
    public static final String POWER_ID = CreateFullID(BlazingHeatPower.class);

    public BlazingHeatPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        PCLCombatStats.onOrbPassiveEffect.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        PCLCombatStats.onOrbPassiveEffect.Unsubscribe(this);
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, FIRE_TRIGGER_BONUS * amount, FIRE_EVOKE_BONUS * amount);
    }

    @Override
    public void onEvokeOrb(AbstractOrb orb) {

        super.onEvokeOrb(orb);

        if (Fire.ORB_ID.equals(orb.ID)) {
            makeMove(orb, orb.evokeAmount *  MathUtils.ceil(FIRE_EVOKE_BONUS * amount / 100f), true);
        }
    }

    @Override
    public void OnOrbPassiveEffect(AbstractOrb orb) {
        if (Fire.ORB_ID.equals(orb.ID)) {
            makeMove(orb, orb.passiveAmount * MathUtils.ceil(FIRE_TRIGGER_BONUS * amount / 100f), false);
        }
    }

    private void makeMove(AbstractOrb orb, int applyAmount, boolean showTrail) {
        AbstractCreature target = null;
        int maxHealth = Integer.MIN_VALUE;

        if (owner.isPlayer) {
            for (AbstractMonster m : PCLGameUtilities.GetEnemies(true))
            {
                if (m.currentHealth > maxHealth)
                {
                    maxHealth = m.currentHealth;
                    target = m;
                }
            }
        }

        if (target != null) {
            int actualDamage = AbstractOrb.applyLockOn(target, applyAmount);
            // This damage action should not remove Freezing because the base Fire damage action already does this
            if (target.hasPower(PCLAttackType.Fire.powerToRemove)) {
                actualDamage *= PCLAttackType.Fire.GetDamageMultiplier();
            }
            if (actualDamage > 0)
            {
                PCLActions.Bottom.DealDamage(source, target, actualDamage, DamageInfo.DamageType.THORNS, AttackEffects.FIRE_EXPLOSION);
                if (showTrail) {
                    PCLActions.Bottom.VFX(VFX.SmallExplosion(target.hb));
                }
            }
        }
    }
}
