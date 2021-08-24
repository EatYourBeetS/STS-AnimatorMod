package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnOrbApplyFocusSubscriber;
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
    private static final int POWER_ENERGY_COST = 1;

    public Arpeggio()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Blue(2);
        SetAffinity_Orange(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (secondaryValue > 0)
        {
            GameActions.Bottom.GainOrbSlots(magicNumber);
        }

        GameActions.Bottom.StackPower(new ArpeggioPower(p, 1));
    }

    public static class ArpeggioPower extends AnimatorClickablePower implements OnOrbApplyFocusSubscriber
    {
        private static final int MAX_BONUS = 2;
        private static final int EARTH_BONUS = 1;
        private boolean earthBonusIsActive = false;
        private int intellectBonus;

        public ArpeggioPower(AbstractCreature owner, int amount)
        {
            super(owner, Arpeggio.DATA, PowerTriggerConditionType.Energy, Arpeggio.POWER_ENERGY_COST);

            triggerCondition.SetUses(-1, false, false);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, amount, earthBonusIsActive ? (" " + Arpeggio.DATA.Strings.EXTENDED_DESCRIPTION[1]) : "", intellectBonus, MAX_BONUS);
        }

        @Override
        protected void onAmountChanged(int previousAmount, int difference)
        {
            intellectBonus = Math.min(MAX_BONUS, amount);
            CombatStats.Affinities.GetRow(Affinity.Blue).ActivationPowerAmount += intellectBonus - Math.min(MAX_BONUS, previousAmount);

            super.onAmountChanged(previousAmount, difference);
        }

        @Override
        public void onInitialApplication() {
            CombatStats.onOrbApplyFocus.Subscribe(this);
        }

        @Override
        public void onRemove() {
            CombatStats.onOrbApplyFocus.Unsubscribe(this);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            earthBonusIsActive = true;
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            earthBonusIsActive = false;
        }

        @Override
        public void OnApplyFocus(AbstractOrb orb)
        {
            if (earthBonusIsActive && orb != null && orb.ID.equals(Earth.ORB_ID)) {
                orb.passiveAmount += EARTH_BONUS;
                orb.evokeAmount += EARTH_BONUS;
            }
        }
    }
}