package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.orbs.TriggerOrbPassiveAbility;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnSynergyBonusSubscriber;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;

public class Arpeggio extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Arpeggio.class)
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
        GameActions.Bottom.GainOrbSlots(magicNumber);
        GameActions.Bottom.StackPower(new ArpeggioPower(p, magicNumber));
    }

    public static class ArpeggioPower extends AnimatorClickablePower implements OnSynergyBonusSubscriber
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

            CombatStats.onSynergyBonus.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onSynergyBonus.Unsubscribe(this);
        }


        @Override
        public void OnSynergyBonus(AbstractCard card, Affinity affinity)
        {
            if (Affinity.Blue.equals(affinity))
            {
                GameActions.Bottom.GainWisdom(amount);
                this.flashWithoutSound();
            }
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);
            GameActions.Bottom.Callback(new TriggerOrbPassiveAbility(TRIGGER_LIMIT, false, true, null).SetFilter(orb -> Earth.ORB_ID.equals(orb.ID)));
        }
    }
}