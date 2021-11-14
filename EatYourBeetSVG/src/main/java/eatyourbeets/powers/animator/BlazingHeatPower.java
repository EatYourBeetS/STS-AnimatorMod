package eatyourbeets.powers.animator;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.subscribers.OnOrbPassiveEffectSubscriber;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import static eatyourbeets.cards.animator.beta.special.BlazingHeat.FIRE_EVOKE_BONUS;
import static eatyourbeets.cards.animator.beta.special.BlazingHeat.FIRE_TRIGGER_BONUS;

public class BlazingHeatPower extends AnimatorPower implements OnOrbPassiveEffectSubscriber
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

        CombatStats.onOrbPassiveEffect.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        CombatStats.onOrbPassiveEffect.Unsubscribe(this);
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
        AbstractCreature target = player;
        int maxHealth = Integer.MIN_VALUE;

        if (owner.isPlayer) {
            for (AbstractMonster m : GameUtilities.GetEnemies(true))
            {
                if (m.currentHealth > maxHealth)
                {
                    maxHealth = m.currentHealth;
                    target = m;
                }
            }
        }

        int actualDamage = AbstractOrb.applyLockOn(target, applyAmount);
        if (actualDamage > 0)
        {
            GameActions.Bottom.DealDamage(source, target, actualDamage, DamageInfo.DamageType.THORNS, AttackEffects.FIRE_EXPLOSION);
            if (showTrail) {
                GameActions.Bottom.VFX(VFX.SmallExplosion(target.hb));
            }
        }



    }
}
