package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnEndOfTurnSubscriber;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class LaughingMan extends PCLCard implements OnEndOfTurnSubscriber, OnStartOfTurnSubscriber
{
    public static final PCLCardData DATA = Register(LaughingMan.class).SetSkill(0, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None).SetMaxCopies(1).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GhostInTheShell);
    private CardType cardType;

    public LaughingMan()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetAutoplay(true);
        SetExhaust(true);

        SetAffinity_Star(1, 0, 0);
    }

    public void OnUpgrade() {
        SetPermanentHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLCombatStats.onEndOfTurn.SubscribeOnce(this);
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer) {
        cardType = null;
        PCLActions.Bottom.SelectFromPile(name, magicNumber, player.hand)
                .SetOptions(false, false)
                .SetMessage(GR.PCL.Strings.HandSelection.Obtain)
                .AddCallback(cards ->
                {
                    if (cards.size() > 0) {
                        PCLCombatStats.onStartOfTurn.SubscribeOnce(this);
                        cardType = cards.get(0).type;
                    }
                });
    }

    @Override
    public void OnStartOfTurn()
    {
        super.OnStartOfTurn();
        if (cardType != null) {
            PCLGameEffects.Queue.ShowCardBriefly(this);
            PCLActions.Bottom.StackPower(new LaughingManPower(player, this.magicNumber, cardType));
        }
    }

    public static class LaughingManPower extends PCLPower
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
            if (!card.type.equals(cardType) && !(card instanceof Necronomicurse)) {
                PCLActions.Bottom.Exhaust(card);
                PCLActions.Bottom.Draw(1);
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