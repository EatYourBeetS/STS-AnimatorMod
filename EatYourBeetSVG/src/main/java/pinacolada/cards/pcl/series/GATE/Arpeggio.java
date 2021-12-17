package pinacolada.cards.pcl.series.GATE;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.actions.orbs.TriggerOrbPassiveAbility;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.interfaces.subscribers.OnMatchBonusSubscriber;
import pinacolada.orbs.pcl.Earth;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;

public class Arpeggio extends PCLCard
{
    public static final PCLCardData DATA = Register(Arpeggio.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();
    private static final int POWER_ENERGY_COST = 6;
    private static final int TRIGGER_LIMIT = 3;

    public Arpeggio()
    {
        super(DATA);

        Initialize(0, 0, 1, TRIGGER_LIMIT);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(POWER_ENERGY_COST);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainOrbSlots(magicNumber);
        PCLActions.Bottom.StackPower(new ArpeggioPower(p, magicNumber));
    }

    public static class ArpeggioPower extends PCLClickablePower implements OnMatchBonusSubscriber
    {
        private static final int MAX_BONUS = 6;
        private static final int EARTH_BONUS = 1;
        private int intellectBonus;

        public ArpeggioPower(AbstractCreature owner, int amount)
        {
            super(owner, Arpeggio.DATA, PowerTriggerConditionType.Affinity, Arpeggio.POWER_ENERGY_COST);

            triggerCondition.SetUses(1, true, false);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, TRIGGER_LIMIT,  intellectBonus, MAX_BONUS);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onMatchBonus.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onMatchBonus.Unsubscribe(this);
        }


        @Override
        public void OnMatchBonus(AbstractCard card, PCLAffinity affinity)
        {
            if (PCLAffinity.Blue.equals(affinity))
            {
                PCLActions.Bottom.GainWisdom(amount);
                this.flashWithoutSound();
            }
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);
            PCLActions.Bottom.Callback(new TriggerOrbPassiveAbility(TRIGGER_LIMIT, false, true, null).SetFilter(orb -> Earth.ORB_ID.equals(orb.ID)));
        }
    }
}