package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnShuffleSubscriber;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class FielNirvalen extends AnimatorCard
{
    public static final EYBCardData DATA = Register(FielNirvalen.class)
            .SetPower(2, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public FielNirvalen()
    {
        super(DATA);

        Initialize(0, 0, 1, 3);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Water(1);
        SetAffinity_Light(1);
    }

    public void OnUpgrade() {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(secondaryValue);
        GameActions.Bottom.StackPower(new FielNirvalenPower(p, magicNumber, magicNumber, secondaryValue));
    }

    public static class FielNirvalenPower extends AnimatorPower implements OnShuffleSubscriber, OnSynergySubscriber
    {
        private boolean canObtain = true;
        private final int secondaryAmount;
        private final int choices;

        public FielNirvalenPower(AbstractCreature owner, int amount, int secondaryAmount, int choices)
        {
            super(owner, FielNirvalen.DATA);

            this.amount = amount;
            this.secondaryAmount = secondaryAmount;
            this.choices = Math.min(AffinityToken.GetCards().size(), choices);

            Initialize(amount);
            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            CombatStats.onSynergy.Subscribe(this);
            CombatStats.onShuffle.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            CombatStats.onSynergy.Unsubscribe(this);
            CombatStats.onShuffle.Unsubscribe(this);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            canObtain = true;
            ResetAmount();
        }

        @Override
        public void OnShuffle(boolean triggerRelics)
        {
            if (!owner.powers.contains(this))
            {
                CombatStats.onShuffle.Unsubscribe(this);
                return;
            }

            if (canObtain)
            {
                GameActions.Bottom.Add(AffinityToken.SelectTokenAction(name, secondaryAmount, choices)
                        .AddCallback(cards ->
                        {
                            for (AbstractCard c : cards)
                            {
                                GameActions.Bottom.MakeCardInDrawPile(c);
                            }
                        }));
                canObtain = false;
                flash();
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, secondaryAmount);
        }

        @Override
        public void OnSynergy(AbstractCard card) {
            if (amount > 0)
            {
                GameActions.Bottom.Scry(1);
                this.amount -= 1;
                updateDescription();
                flash();
            }
        }
    }
}