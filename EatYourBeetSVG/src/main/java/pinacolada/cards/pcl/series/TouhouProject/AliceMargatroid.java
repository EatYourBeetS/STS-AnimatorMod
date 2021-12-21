package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.interfaces.subscribers.OnTrySpendAffinitySubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class AliceMargatroid extends PCLCard
{
    public static final PCLCardData DATA = Register(AliceMargatroid.class).SetPower(2, CardRarity.UNCOMMON).SetSeriesFromClassPackage();

    public AliceMargatroid()
    {
        super(DATA);

        Initialize(0, 0, 1, 3);
        SetUpgrade(0, 0, 0, 1);
        SetAffinity_Blue(2, 0, 0);
        SetAffinity_Light(1,0,0);

        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainVitality(magicNumber);
        PCLActions.Bottom.StackPower(new AlicePower(p, magicNumber, secondaryValue));
    }

    public static class AlicePower extends PCLPower implements OnTrySpendAffinitySubscriber
    {
        private int secondaryValue;

        public AlicePower(AbstractCreature owner, int amount, int secondaryValue)
        {
            super(owner, AliceMargatroid.DATA);
            this.amount = amount;
            this.secondaryValue = secondaryValue;
            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();
            PCLCombatStats.onTrySpendAffinity.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();
            PCLCombatStats.onTrySpendAffinity.Unsubscribe(this);
        }


        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurn();

            PCLActions.Bottom.Callback(() -> {
                PCLActions.Bottom.Exchange(name, amount);
            });
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount, secondaryValue);
        }

        @Override
        public int OnTrySpendAffinity(PCLAffinity affinity, int amount, boolean isActuallySpending) {
            if (isActuallySpending) {
                PCLActions.Bottom.GainBlock(secondaryValue);
            }
            return amount;
        }
    }
}

