package eatyourbeets.cards.animator.series.LogHorizon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;

public class Tetora extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Tetora.class)
            .SetPower(0, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();
    private static final int POWER_CARD_COST = 2;

    public Tetora()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);

        SetAffinity_Water(1);
        SetAffinity_Light(2);
        SetAffinity_Earth(1);

        SetHarmonic(true);

        SetAffinityRequirement(Affinity.General, 4);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return super.cardPlayable(m) && CheckAffinity(Affinity.General);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new TetoraPower(p, magicNumber, secondaryValue));
    }

    public static class TetoraPower extends AnimatorClickablePower implements OnSynergySubscriber
    {
        private final int baseSecondaryValue;
        private int secondaryValue;
        private boolean active;

        public TetoraPower(AbstractPlayer owner, int amount, int secondaryValue)
        {
            super(owner, Tetora.DATA, PowerTriggerConditionType.Discard, POWER_CARD_COST);

            this.triggerCondition.SetUses(1,true, false);

            this.amount = amount;
            this.baseSecondaryValue = this.secondaryValue = secondaryValue;

            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onSynergy.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onSynergy.Unsubscribe(this);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);
            for (Affinity affinity : Affinity.Basic()) {
                CombatStats.Affinities.BonusAffinities.Add(affinity, 2);
            }
            active = true;

        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();
            if (active) {
                active = false;
                for (Affinity affinity : Affinity.Basic()) {
                    CombatStats.Affinities.BonusAffinities.Add(affinity, -2);
                }
            }
            secondaryValue = baseSecondaryValue;
        }


        @Override
        public void OnSynergy(AbstractCard card)
        {
            if (secondaryValue > 0)
            {
                secondaryValue -= 1;
                for (int i = 0; i < amount; i++)
                {
                    GameActions.Bottom.GainRandomAffinityPower(1, false);
                }

                flash();
            }
        }


        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(secondaryValue, Settings.BLUE_TEXT_COLOR);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, secondaryValue, amount, POWER_CARD_COST);
        }
    }
}