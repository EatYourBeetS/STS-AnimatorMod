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
            .SetMultiformData(2, false)
            .SetSeriesFromClassPackage();
    private static final int POWER_ENERGY_COST = 6;
    private static final int TRIGGER_LIMIT = 3;

    public Arpeggio()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetInnate(form != 1);
        }
        return super.SetForm(form, timesUpgraded);
    }


    @Override
    protected void OnUpgrade()
    {
        super.OnUpgrade();
        SetRetainOnce(true);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(POWER_ENERGY_COST, TRIGGER_LIMIT);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainOrbSlots(secondaryValue);
        PCLActions.Bottom.StackPower(new ArpeggioPower(p, magicNumber, secondaryValue));
    }

    public static class ArpeggioPower extends PCLClickablePower implements OnMatchBonusSubscriber
    {
        private static final int MAX_BONUS = 6;
        private static final int EARTH_BONUS = 1;
        private int secondaryValue;

        public ArpeggioPower(AbstractCreature owner, int amount, int secondaryValue)
        {
            super(owner, Arpeggio.DATA, PowerTriggerConditionType.Affinity, Arpeggio.POWER_ENERGY_COST);

            triggerCondition.SetUses(1, true, false);
            this.secondaryValue = secondaryValue;

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, TRIGGER_LIMIT, amount, secondaryValue);
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

        public void atStartOfTurn()
        {
            super.atStartOfTurn();
            ResetAmount();
        }


        @Override
        public void OnMatchBonus(AbstractCard card, PCLAffinity affinity)
        {
            if (amount > 0 && PCLAffinity.Orange.equals(affinity))
            {
                amount -= 1;
                PCLActions.Bottom.GainWisdom(secondaryValue);
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