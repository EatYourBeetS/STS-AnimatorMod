package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;

public class Envy extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Envy.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public Envy()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0,0,1);

        SetAffinity_Poison();
        SetAffinity_Dark();
        SetAffinity_Nature();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new EnvyPower(p, 1, magicNumber));
    }

    public static class EnvyPower extends AnimatorClickablePower
    {
        private int Prayer;
        private int timesPerTurn;

        public EnvyPower(AbstractPlayer owner, int amount, int timesPerTurn)
        {
            super(owner, Envy.DATA, PowerTriggerConditionType.Energy, 1);

            this.timesPerTurn = timesPerTurn;

            if (timesPerTurn > 1)
            {
                triggerCondition.SetTwoUsesPerPower(true);
            }
            else {
                triggerCondition.SetOneUsePerPower(true);
            }

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, 1, timesPerTurn);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            ResetAmount();
            updateDescription();
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            EYBCardAffinities affinities = CombatStats.Affinities.GetHandAffinities(null);

            for (Affinity a : Affinity.Basic())
            {
                int level = affinities.GetLevel(a, false);

                if (level > 0) {
                    GameActions.Bottom.StackAffinityPower(a, level, false);
                }
            }
        }
    }
}