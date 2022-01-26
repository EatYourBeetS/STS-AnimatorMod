package pinacolada.cards.pcl.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.interfaces.subscribers.OnOrbPassiveEffectSubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.interfaces.subscribers.OnPCLClickablePowerUsed;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.powers.affinity.InvocationPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Miko extends PCLCard
{
    public static final PCLCardData DATA = Register(Miko.class)
            .SetPower(3, CardRarity.RARE)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Miko()
    {
        super(DATA);

        Initialize(0, 0, 1, 10);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ChannelOrb(new Plasma());
        PCLActions.Bottom.StackPower(new MikoPower(p, magicNumber));
    }

    public static class MikoPower extends PCLPower implements OnOrbPassiveEffectSubscriber, OnPCLClickablePowerUsed
    {
        public MikoPower(AbstractCreature owner, int amount)
        {
            super(owner, Miko.DATA);

            Initialize(amount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onOrbPassiveEffect.Subscribe(this);
            PCLCombatStats.onPCLClickablePowerUsed.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onOrbPassiveEffect.Unsubscribe(this);
            PCLCombatStats.onPCLClickablePowerUsed.Unsubscribe(this);
        }

        @Override
        public void OnOrbPassiveEffect(AbstractOrb orb) {
            if (Plasma.ORB_ID.equals(orb.ID)) {
                PCLActions.Bottom.GainInvocation(amount * orb.passiveAmount);
                flash();
            }
        }

        @Override
        public void onEvokeOrb(AbstractOrb orb)
        {
            super.onEvokeOrb(orb);

            if (Plasma.ORB_ID.equals(orb.ID)) {
                PCLActions.Bottom.GainInvocation(amount * orb.evokeAmount);
                flash();
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, PCLGameUtilities.GetPCLAffinityPowerLevel(PCLAffinity.Light));
        }

        @Override
        public boolean OnClickablePowerUsed(PCLClickablePower power, AbstractMonster target) {
            if (power instanceof InvocationPower) {
                PCLActions.Bottom.ChannelOrbs(PCLOrbHelper.Plasma, amount);
            }
            return true;
        }
    }
}