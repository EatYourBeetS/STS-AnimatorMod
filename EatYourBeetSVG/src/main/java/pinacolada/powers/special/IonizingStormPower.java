package pinacolada.powers.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.interfaces.subscribers.OnOrbPassiveEffectSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.powers.common.ElectrifiedPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

import static pinacolada.cards.pcl.special.IonizingStorm.LIGHTNING_BONUS;

public class IonizingStormPower extends PCLPower implements OnOrbPassiveEffectSubscriber
{
    public static final int PER_CHARGE = 1;
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
        description = FormatDescription(0, LIGHTNING_BONUS * amount, PER_CHARGE);
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
            this.applyPower(target, orb, applyAmount);
        }
    }

    public void atStartOfTurn()
    {
        super.atStartOfTurn();
        int count = PCLJUtils.Count(PCLGameUtilities.GetEnemies(true), e -> e.hasPower(ElectrifiedPower.POWER_ID));
        if (count > 0) {
            PCLActions.Bottom.GainInvocation(PER_CHARGE * count);
        }
    }

    private void applyPower(AbstractCreature target, AbstractOrb orb, int applyAmount) {
        if (target != null) {
            PCLActions.Bottom.ApplyElectrified(owner, target, applyAmount).CanStack(true);
        }
    }
}
