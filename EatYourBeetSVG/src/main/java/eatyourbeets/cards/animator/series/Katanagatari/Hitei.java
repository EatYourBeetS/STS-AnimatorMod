package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class Hitei extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Hitei.class)
            .SetPower(1, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();
    public static final int POWER_ACTIVATION_COST = 4;
    public static final int EXHAUST_SELECTION_SIZE = 2;
    public static final int CARD_DRAW = 2;

    public Hitei()
    {
        super(DATA);

        Initialize(0, 0, EXHAUST_SELECTION_SIZE, POWER_ACTIVATION_COST);
        SetCostUpgrade(-1);

        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new HiteiPower(p, 1));
    }

    public static class HiteiPower extends AnimatorClickablePower
    {
        public HiteiPower(AbstractPlayer owner, int amount)
        {
            super(owner, Hitei.DATA, PowerTriggerConditionType.TakeDamage, POWER_ACTIVATION_COST);

            this.triggerCondition.SetOneUsePerPower(true);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, CARD_DRAW, EXHAUST_SELECTION_SIZE);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            GameActions.Bottom.Draw(CARD_DRAW);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            for (int i = 0; i < amount; i++)
            {
                GameActions.Bottom.Callback(() ->
                {
                    if (player.drawPile.size() < EXHAUST_SELECTION_SIZE)
                    {
                        GameActions.Top.Callback(new EmptyDeckShuffleAction(), this::ExhaustCards);
                    }
                    else
                    {
                        ExhaustCards();
                    }
                });
            }

            this.flash();
        }

        private void ExhaustCards()
        {
            final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            final RandomizedList<AbstractCard> pile = new RandomizedList<>(player.drawPile.group);
            while (group.size() < EXHAUST_SELECTION_SIZE && pile.Size() > 0)
            {
                group.addToTop(pile.Retrieve(rng));
            }

            if (group.size() >= EXHAUST_SELECTION_SIZE)
            {
                GameActions.Top.ExhaustFromPile(name, 1, group)
                .SetOptions(false, false);
            }
        }
    }
}