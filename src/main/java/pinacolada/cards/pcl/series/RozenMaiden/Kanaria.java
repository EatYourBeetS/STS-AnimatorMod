package pinacolada.cards.pcl.series.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnCardCreatedSubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.pcl.special.Kanaria_Pizzicato;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Kanaria extends PCLCard
{
    public static final PCLCardData DATA = Register(Kanaria.class)
    		.SetPower(2, CardRarity.RARE).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Kanaria_Pizzicato(), false));
    public static final int COST = 5;

    public Kanaria()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0);
        SetAffinity_Light(1, 0, 0);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Blue(1, 0, 0);

        SetCostUpgrade(-1);
        SetAffinityRequirement(PCLAffinity.Blue, 5);
    }

    @Override
    public String GetRawDescription()
    {
        return GetRawDescription( COST);
    }

    @Override
    public AbstractAttribute GetSecondaryInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
        PCLActions.Bottom.StackPower(new KanariaPower(p, 1, magicNumber));
    }

    public static class KanariaPower extends PCLClickablePower implements OnCardCreatedSubscriber
    {
        protected int secondaryAmount;

        public KanariaPower(AbstractCreature owner, int amount, int secondaryAmount)
        {
            super(owner, Kanaria.DATA, PowerTriggerConditionType.Affinity, COST, null, null, PCLAffinity.Blue, PCLAffinity.Light);
            this.secondaryAmount = secondaryAmount;

            Initialize(amount);
            updateDescription();
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, amount, secondaryAmount, this.triggerCondition.requiredAmount);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            ResetAmount();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onCardCreated.Subscribe(this);
            PCLCombatStats.MatchingSystem.Powers.get(PCLAffinity.Light.ID).SetEnabled(true);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onCardCreated.Unsubscribe(this);
        }


        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            PCLActions.Bottom.MakeCardInHand(new Kanaria_Pizzicato());
        }

        @Override
        public void OnCardCreated(AbstractCard card, boolean startOfBattle) {
            if (amount > 0 && PCLGameUtilities.IsHindrance(card)) {
                PCLActions.Bottom.GainInvocation(secondaryAmount);
                amount -= 1;
            }
        }
    }
}