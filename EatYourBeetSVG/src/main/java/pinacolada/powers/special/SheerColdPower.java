package pinacolada.powers.special;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.interfaces.subscribers.OnOrbPassiveEffectSubscriber;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.vfx.SnowballEffect;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

import static pinacolada.cards.pcl.special.SheerCold.FROST_EVOKE_BONUS;
import static pinacolada.cards.pcl.special.SheerCold.FROST_TRIGGER_BONUS;

public class SheerColdPower extends PCLPower implements OnOrbPassiveEffectSubscriber
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
        ArrayList<AbstractMonster> enemies = PCLGameUtilities.GetEnemies(true);
        if (owner.isPlayer && enemies.size() > 0) {
            int highestAttack = Integer.MIN_VALUE;
            float chance = (float) (1/(Math.max(enemies.size(),1.0)));

            for (AbstractMonster m : enemies)
            {
                int damage = PCLGameUtilities.GetPCLIntent(m).GetDamage(true);
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
            PCLActions.Top.Wait(.15f);
            PCLActions.Top.VFX(new SnowballEffect(orb.hb.cX, orb.hb.cY, target.hb.cX, target.hb.cY)
                    .SetColor(Color.SKY, Color.NAVY).SetRealtime(true));
            PCLActions.Bottom.DealDamage(owner, target, actualDamage, DamageInfo.DamageType.THORNS, AttackEffects.ICE)
                    .SetVFX(true, true);
            PCLActions.Bottom.ApplyFreezing(owner, target, MathUtils.ceil(damageAmount / 2f)).CanStack(true);
        }
    }
}
