package eatyourbeets.cards.animator.series.Katanagatari;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnAfterCardDiscardedSubscriber;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.stances.EYBStance;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class ManiwaHouou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ManiwaHouou.class)
            .SetPower(2, CardRarity.RARE)
            .SetSeries(CardSeries.Katanagatari)
            .SetMultiformData(2);

    public ManiwaHouou()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 0);

        SetAffinity_Green(2);
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
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new ManiwaHououPower(p, magicNumber, secondaryValue));
    }

    public static class ManiwaHououPower extends AnimatorClickablePower implements OnAfterCardDiscardedSubscriber
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

            CombatStats.onAfterCardDiscarded.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onAfterCardDiscarded.Unsubscribe(this);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurn();

            GameActions.Bottom.DiscardFromHand(name, secondaryAmount, true)
                    .SetOptions(false, false, false)
                    .AddCallback(() -> GameActions.Bottom.CreateThrowingKnives(secondaryAmount));
        }

        @Override
        public void OnUse(AbstractMonster m, int cost)
        {
            GameActions.Bottom.WaitRealtime(0.35f);
            GameActions.Bottom.ChangeStance(EYBStance.GetRandomStance());
            RemovePower(GameActions.Last);
        }

        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(secondaryAmount, Settings.PURPLE_COLOR, c.a);
        }

        @Override
        public void OnAfterCardDiscarded() {
            GameActions.Bottom.ApplyPoison(TargetHelper.RandomEnemy(), amount);
        }
    }
}