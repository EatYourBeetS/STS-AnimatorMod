package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnAfterCardDiscardedSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class ArthurGaz extends AnimatorCard {
    public static final AbstractCard[] ChaikaClones = new AbstractCard[]{
            new ChaikaBohdan(),
            new ChaikaKamaz(),
            new ChaikaTrabant(),
            new Fredrika(),
            new Layla(),
            new Viivi()
    };

    public static final EYBCardData DATA = Register(ArthurGaz.class)
            .SetPower(3, CardRarity.RARE)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                for (AbstractCard chaikaClone : ChaikaClones)
                {
                    data.AddPreview(chaikaClone, true);
                }
            });

    public ArthurGaz() {
        super(DATA);

        Initialize(0, 0, 2);

        SetAffinity_Light(2);
        SetAffinity_Dark(2);
        SetAffinity_Poison(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.StackPower(new ArthurGazPower(p, this.magicNumber, upgraded));
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
    }

    public static class ArthurGazPower extends AnimatorPower implements OnAfterCardDiscardedSubscriber {

        private boolean upgraded;

        public ArthurGazPower(AbstractPlayer owner, int amount, boolean upgraded) {
            super(owner, ArthurGaz.DATA);

            this.amount = amount;
            this.upgraded = true;

            updateDescription();
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
        public void OnAfterCardDiscarded()
        {
            if (GameActionManager.totalDiscardedThisTurn <= amount)
            {
                RandomizedList<AbstractCard> createOptions = new RandomizedList<>();
                createOptions.AddAll(ArthurGaz.ChaikaClones);

                AbstractCard newCard = createOptions.Retrieve(rng).makeCopy();
                if (upgraded) {
                    newCard.upgrade();
                }

                if (newCard != null)
                {
                    newCard.modifyCostForCombat(-1);
                    GameActions.Bottom.MakeCardInHand(newCard);
                }
            }
        }

        @Override
        public void updateDescription() {
            if (upgraded)
            {
                description = FormatDescription(1, amount);
            }
            else
            {
                description = FormatDescription(0, amount);
            }
        }
    }
}