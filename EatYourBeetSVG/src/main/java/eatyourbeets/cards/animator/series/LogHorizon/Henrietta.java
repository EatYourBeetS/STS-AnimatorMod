package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

import java.util.Arrays;

public class Henrietta extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Henrietta.class)
            .SetPower(3, CardRarity.UNCOMMON)
            .SetMultiformData(2)
            .SetSeriesFromClassPackage();
    private static final int POWER_ENERGY_COST = 4;

    public Henrietta()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);

        SetAffinity_Blue(1);
        SetAffinity_Orange(2);
    }

    @Override
    protected void OnUpgrade()
    {
        if (auxiliaryData.form == 0) {
            SetInnate(true);
        }
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetInnate(form == 0);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new HenriettaPower(p, magicNumber, secondaryValue));
    }

    public static class HenriettaPower extends AnimatorClickablePower
    {
        private int secondaryValue;

        public HenriettaPower(AbstractPlayer owner, int amount, int secondaryValue)
        {
            super(owner, Henrietta.DATA, PowerTriggerConditionType.Affinity, Henrietta.POWER_ENERGY_COST);

            this.amount = amount;
            this.secondaryValue = secondaryValue;

            updateDescription();
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);
            final EYBCardAffinities affinities = CombatStats.Affinities.GetHandAffinities(null);
            Affinity highestAffinity = JUtils.FindMax(Arrays.asList(Affinity.Basic()), affinities::GetLevel);
            GameActions.Bottom.StackAffinityPower(highestAffinity, secondaryValue, false);
        }

        @Override
        public void onInitialApplication() {
            super.onInitialApplication();

            for (Affinity affinity: Affinity.Basic()) {
                CombatStats.Affinities.AddMaxActivationsPerTurn(affinity, amount);
            }
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            for (Affinity affinity: Affinity.Basic()) {
                CombatStats.Affinities.AddMaxActivationsPerTurn(affinity, -amount);
            }
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, secondaryValue, amount);
        }
    }
}