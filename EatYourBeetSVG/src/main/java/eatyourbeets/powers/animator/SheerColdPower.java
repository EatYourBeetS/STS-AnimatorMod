package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.vfx.SnowballEffect;
import eatyourbeets.interfaces.subscribers.OnOrbPassiveEffectSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

import static eatyourbeets.cards.animator.beta.special.SheerCold.FROST_EVOKE_BONUS;
import static eatyourbeets.cards.animator.beta.special.SheerCold.FROST_TRIGGER_BONUS;

public class SheerColdPower extends AnimatorPower implements OnOrbPassiveEffectSubscriber
{
    public static final String POWER_ID = CreateFullID(SheerColdPower.class);

    public SheerColdPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
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
        description = FormatDescription(0, FROST_TRIGGER_BONUS * amount, FROST_EVOKE_BONUS * amount);
    }

    @Override
    public void onEvokeOrb(AbstractOrb orb) {

        super.onEvokeOrb(orb);

        if (Frost.ORB_ID.equals(orb.ID)) {
            makeMove(orb, orb.evokeAmount * FROST_EVOKE_BONUS * amount / 100);
        }
    }

    @Override
    public void OnOrbPassiveEffect(AbstractOrb orb) {
        if (Frost.ORB_ID.equals(orb.ID)) {
            makeMove(orb, orb.passiveAmount * FROST_TRIGGER_BONUS * amount / 100);
        }
    }

    private void makeMove(AbstractOrb orb, int damageAmount) {
        AbstractCreature target = null;
        ArrayList<AbstractMonster> enemies = GameUtilities.GetEnemies(true);
        if (owner.isPlayer && enemies.size() > 0) {
            int highestAttack = Integer.MIN_VALUE;
            float chance = (float) (1/(Math.max(enemies.size(),1.0)));

            for (AbstractMonster m : enemies)
            {
                int damage = GameUtilities.GetIntent(m).GetDamage(true);
                if (damage > highestAttack)
                {
                    highestAttack = damage;
                    target = m;
                }
                else if (damage == highestAttack && rng.randomBoolean(chance)) {
                    target = m;
                }
            }
        }
        if (target != null) {
            this.applyPower(target, orb, damageAmount);
        }
    }

    private void applyPower(AbstractCreature target, AbstractOrb orb, int damageAmount) {
        if (target != null) {
            int actualDamage = AbstractOrb.applyLockOn(target, damageAmount);
            GameActions.Top.Wait(.15f);
            GameActions.Top.VFX(new SnowballEffect(orb.hb.cX, orb.hb.cY, target.hb.cX, target.hb.cY)
                    .SetColor(Color.SKY, Color.CYAN).SetRealtime(true));
            GameActions.Bottom.DealDamage(owner, target, actualDamage, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
                    .SetVFX(true, true);
            GameActions.Bottom.ApplyFreezing(owner, target, MathUtils.ceil(damageAmount / 2f)).CanStack(true);
        }
    }
}
