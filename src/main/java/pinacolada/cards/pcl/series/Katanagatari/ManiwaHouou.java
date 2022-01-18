package pinacolada.cards.pcl.series.Katanagatari;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnAfterCardDiscardedSubscriber;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.stances.PCLStance;
import pinacolada.utilities.PCLActions;

public class ManiwaHouou extends PCLCard
{
    public static final PCLCardData DATA = Register(ManiwaHouou.class)
            .SetPower(2, CardRarity.RARE)
            .SetSeries(CardSeries.Katanagatari)
            .SetMultiformData(2);

    public ManiwaHouou()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 0);

        SetAffinity_Green(1);
        SetAffinity_Dark(1);

        SetEthereal(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetEthereal(form == 1);
            SetInnate(form == 1);
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new ManiwaHououPower(p, magicNumber, secondaryValue));
    }

    public static class ManiwaHououPower extends PCLClickablePower implements OnAfterCardDiscardedSubscriber
    {
        private int secondaryAmount;

        public ManiwaHououPower(AbstractCreature owner, int amount, int secondaryAmount)
        {
            super(owner, ManiwaHouou.DATA, PowerTriggerConditionType.Affinity, 3);

            this.triggerCondition.SetUses(-1, false, false);
            this.secondaryAmount = secondaryAmount;

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, amount, secondaryAmount);
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            PCLCombatStats.onAfterCardDiscarded.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onAfterCardDiscarded.Unsubscribe(this);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurn();

            PCLActions.Bottom.DiscardFromHand(name, secondaryAmount, true)
                    .SetOptions(false, false, false)
                    .AddCallback(() -> PCLActions.Bottom.CreateThrowingKnives(secondaryAmount));
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            PCLActions.Bottom.WaitRealtime(0.35f);
            PCLActions.Bottom.ChangeStance(PCLStance.GetRandomStance());
            RemovePower(PCLActions.Last);
        }

        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(secondaryAmount, Settings.PURPLE_COLOR, c.a);
        }

        @Override
        public void OnAfterCardDiscarded() {
            PCLActions.Bottom.ApplyPoison(TargetHelper.RandomEnemy(), amount);
        }
    }
}