package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;

public class Arpeggio extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Arpeggio.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();
    private static final int POWER_ENERGY_COST = 2;

    public Arpeggio()
    {
        super(DATA);

        Initialize(0, 0, POWER_ENERGY_COST);

        SetAffinity_Blue(2);

        SetDelayed(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetDelayed(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new ArpeggioPower(p, 1));
    }

    public static class ArpeggioPower extends AnimatorClickablePower
    {
        private static final int MAX_BONUS = 2;
        private int intellectBonus;

        public ArpeggioPower(AbstractCreature owner, int amount)
        {
            super(owner, Arpeggio.DATA, PowerTriggerConditionType.Energy, Arpeggio.POWER_ENERGY_COST);

            triggerCondition.SetUses(1, false, true);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, intellectBonus, MAX_BONUS);
        }

//        @Override
//        protected void onAmountChanged(int previousAmount, int difference)
//        {
//            intellectBonus = Math.min(MAX_BONUS, amount);
//            CombatStats.Affinities.GetRow(Affinity.Blue).ActivationPowerAmount += intellectBonus - Math.min(MAX_BONUS, previousAmount);
//
//            super.onAmountChanged(previousAmount, difference);
//        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.GainOrbSlots(1);
            GameActions.Bottom.ChannelOrbs(Earth::new, 1);
        }
    }
}