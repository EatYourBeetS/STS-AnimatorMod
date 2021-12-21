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
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
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
        if (info.TryActivateLimited()) {
            PCLActions.Bottom.GainInvocation(secondaryValue);
        }
        PCLActions.Bottom.ChannelOrb(new Plasma());
        PCLActions.Bottom.StackPower(new MikoPower(p, magicNumber));
    }

    public static class MikoPower extends PCLPower implements OnOrbPassiveEffectSubscriber
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
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onOrbPassiveEffect.Unsubscribe(this);
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
    }
}