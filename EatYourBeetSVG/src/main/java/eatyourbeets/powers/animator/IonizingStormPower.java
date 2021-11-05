package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.interfaces.subscribers.OnOrbApplyFocusSubscriber;
import eatyourbeets.interfaces.subscribers.OnOrbPassiveEffectSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.SuperchargedPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class IonizingStormPower extends AnimatorPower implements OnOrbPassiveEffectSubscriber, OnOrbApplyFocusSubscriber
{
    public static final int PER_CHARGE = 5;
    public static final String POWER_ID = CreateFullID(IonizingStormPower.class);

    public IonizingStormPower(AbstractPlayer owner, int amount)
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
    public void onEvokeOrb(AbstractOrb orb) {

        super.onEvokeOrb(orb);

        if (Lightning.ORB_ID.equals(orb.ID)) {
            makeMove(orb, orb.evokeAmount / 2 * amount);
        }
    }

    @Override
    public void OnOrbPassiveEffect(AbstractOrb orb) {
        if (Lightning.ORB_ID.equals(orb.ID)) {
            makeMove(orb, orb.passiveAmount / 2 * amount);
        }
    }

    private void makeMove(AbstractOrb orb, int applyAmount) {
        AbstractCreature target = player;
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
        this.applyPower(target, orb, applyAmount);
    }

    private void applyPower(AbstractCreature target, AbstractOrb orb, int applyAmount) {
        if (target != null) {
            GameActions.Bottom.ApplyElectrified(owner, target, applyAmount).CanStack(true);
        }
    }

    @Override
    public void OnApplyFocus(AbstractOrb orb) {
        if (Lightning.ORB_ID.equals(orb.ID)) {
            SuperchargedPower po = GameUtilities.GetPower(player, SuperchargedPower.POWER_ID);
            if (po != null) {
                orb.passiveAmount += po.charge / PER_CHARGE;
                orb.evokeAmount += po.charge / PER_CHARGE;
            }

        }
    }
}
