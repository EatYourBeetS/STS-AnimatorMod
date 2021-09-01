package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnEndOfTurnSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class LaughingMan extends AnimatorCard implements OnEndOfTurnSubscriber, OnStartOfTurnSubscriber
{
    public static final EYBCardData DATA = Register(LaughingMan.class).SetSkill(0, CardRarity.RARE, EYBCardTarget.None).SetMaxCopies(1).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GhostInTheShell);
    private CardType cardType;

    public LaughingMan()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetAutoplay(true);
        SetExhaust(true);

        SetAffinity_Star(1, 1, 0);
    }

    public void OnUpgrade() {
        SetPermanentHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        CombatStats.onEndOfTurn.SubscribeOnce(this);
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer) {
        cardType = null;
        GameActions.Bottom.SelectFromPile(name, magicNumber, player.hand)
                .SetOptions(false, false)
                .SetMessage(GR.Common.Strings.HandSelection.Choose)
                .AddCallback(cards ->
                {
                    if (cards.size() > 0) {
                        CombatStats.onStartOfTurn.SubscribeOnce(this);
                        cardType = cards.get(0).type;
                    }
                });
    }

    @Override
    public void OnStartOfTurn()
    {
        super.OnStartOfTurn();
        if (cardType != null) {
            GameEffects.Queue.ShowCardBriefly(this);
            GameActions.Bottom.StackPower(new LaughingManPower(player, this.magicNumber, cardType));
        }
    }

    public static class LaughingManPower extends AnimatorPower
    {
        private final CardType cardType;
        public LaughingManPower(AbstractPlayer owner, int amount, CardType cardType)
        {
            super(owner, LaughingMan.DATA);

            this.amount = amount;
            this.cardType = cardType;
            this.priority = 99;

            updateDescription();
        }

        public void onCardDraw(AbstractCard card) {
            if (!card.type.equals(cardType)) {
                GameActions.Bottom.Exhaust(card);
                GameActions.Bottom.Draw(1);
            }
        }

        @Override
        public void atEndOfRound()
        {
            ReducePower(1);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, cardType.toString());
        }
    }
}