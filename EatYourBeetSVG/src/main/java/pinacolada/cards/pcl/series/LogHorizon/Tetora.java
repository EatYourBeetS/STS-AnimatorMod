package pinacolada.cards.pcl.series.LogHorizon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.utilities.ColoredString;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;

public class Tetora extends PCLCard
{
    public static final PCLCardData DATA = Register(Tetora.class)
            .SetPower(0, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();
    private static final int POWER_CARD_COST = 8;
    private static final int USE_COST = 7;
    private static final int SYNERGY_TIMES = 2;

    public Tetora()
    {
        super(DATA);

        Initialize(0, 0, 3, POWER_CARD_COST);

        SetAffinity_Star(1);

        SetHarmonic(true);

        SetAffinityRequirement(PCLAffinity.General, USE_COST);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
        SetAffinityRequirement(PCLAffinity.General, 6);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return super.cardPlayable(m) && CheckAffinity(PCLAffinity.General);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
            PCLActions.Bottom.StackPower(new TetoraPower(p, magicNumber, SYNERGY_TIMES));
        });
    }

    public static class TetoraPower extends PCLClickablePower implements OnSynergySubscriber
    {
        private final int baseSecondaryValue;
        private int secondaryValue;
        private boolean active;

        public TetoraPower(AbstractPlayer owner, int amount, int secondaryValue)
        {
            super(owner, Tetora.DATA, PowerTriggerConditionType.Affinity, POWER_CARD_COST);

            this.triggerCondition.SetUses(1,true, false);

            this.amount = amount;
            this.baseSecondaryValue = this.secondaryValue = secondaryValue;

            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onSynergy.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onSynergy.Unsubscribe(this);
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            super.OnUse(m, cost);
            PCLActions.Bottom.Exchange(name, amount);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();
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
                    PCLActions.Bottom.GainRandomAffinityPower(1, false);
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