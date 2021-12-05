package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
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
    private static final int POWER_COST = 9;

    public Henrietta()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);

        SetAffinity_Blue(1);
        SetAffinity_Orange(2);
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
            super(owner, Henrietta.DATA, PowerTriggerConditionType.Affinity, Henrietta.POWER_COST);

            this.amount = amount;
            this.secondaryValue = secondaryValue;

            updateDescription();
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);
            Affinity highestAffinity = JUtils.FindMax(Arrays.asList(Affinity.Extended()), af -> CombatStats.Affinities.GetAffinityLevel(af, true));
            GameActions.Bottom.StackAffinityPower(highestAffinity, secondaryValue, false);
        }

        @Override
        public void onInitialApplication() {
            super.onInitialApplication();

            for (Affinity affinity: Affinity.Extended()) {
                CombatStats.Affinities.AddMaxActivationsPerTurn(affinity, amount);
            }
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            for (Affinity affinity: Affinity.Extended()) {
                CombatStats.Affinities.AddMaxActivationsPerTurn(affinity, -amount);
            }
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, POWER_COST, secondaryValue, amount);
        }
    }
}