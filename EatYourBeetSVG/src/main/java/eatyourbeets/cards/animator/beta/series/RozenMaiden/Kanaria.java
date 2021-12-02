package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import eatyourbeets.cards.animator.beta.special.Kanaria_Pizzicato;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnCardCreatedSubscriber;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Kanaria extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kanaria.class)
    		.SetPower(2, CardRarity.RARE).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Kanaria_Pizzicato(), false));
    public static final int COST = 5;

    public Kanaria()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0);
        SetAffinity_Light(2, 0, 0);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Blue(1, 1, 0);

        SetCostUpgrade(-1);
        SetAffinityRequirement(Affinity.Blue, 5);
    }

    @Override
    public String GetRawDescription()
    {
        return GetRawDescription( COST);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new KanariaPower(p, 1, magicNumber));
    }

    public static class KanariaPower extends AnimatorClickablePower implements OnCardCreatedSubscriber
    {
        protected int secondaryAmount;

        public KanariaPower(AbstractCreature owner, int amount, int secondaryAmount)
        {
            super(owner, Kanaria.DATA, PowerTriggerConditionType.Affinity, COST, null, null, Affinity.Blue, Affinity.Light);
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

            CombatStats.Affinities.GetPower(Affinity.Green).SetEnabled(true);
            CombatStats.Affinities.GetPower(Affinity.Light).SetEnabled(true);
            CombatStats.onCardCreated.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onCardCreated.Unsubscribe(this);
        }


        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            GameActions.Bottom.MakeCardInHand(new Kanaria_Pizzicato());
        }

        @Override
        public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
            super.onChangeStance(oldStance,newStance);

            GameActions.Last.Callback(() -> {
                CombatStats.Affinities.GetPower(Affinity.Green).SetEnabled(true);
                CombatStats.Affinities.GetPower(Affinity.Light).SetEnabled(true);
            });
        }

        @Override
        public void OnCardCreated(AbstractCard card, boolean startOfBattle) {
            if (amount > 0 && GameUtilities.IsHindrance(card)) {
                GameActions.Bottom.GainVelocity(secondaryAmount);
                GameActions.Bottom.GainSupercharge(secondaryAmount);
                amount -= 1;
            }
        }
    }
}