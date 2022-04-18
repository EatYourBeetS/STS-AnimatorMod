package eatyourbeets.cards.unnamed.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.powers.UnnamedPower;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;
import java.util.StringJoiner;

public class Unravel extends UnnamedCard
{
    public static final int FETCH_AMOUNT = 2;
    public static final EYBCardData DATA = Register(Unravel.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .ObtainableAsReward((data, deck) -> deck.size() >= (12 + (12 * data.GetTotalCopies(deck))));

    public Unravel()
    {
        super(DATA);

        Initialize(0, 0, 3, 3);
        SetUpgrade(0, 0, 0, -1);

        SetExhaust(true);
    }

    @Override
    public ColoredString GetSpecialVariableString()
    {
        return super.GetSpecialVariableString(FETCH_AMOUNT);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.MoveCards(p.drawPile, p.exhaustPile, magicNumber)
        .ShowEffect(true, true)
        .SetOrigin(CardSelection.Top)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.StackPower(new UnravelPower(player, cards, secondaryValue));
            }
        });
    }

    public static class UnravelPower extends UnnamedPower
    {
        private static int counter;
        private final ArrayList<AbstractCard> cards = new ArrayList<>();

        public UnravelPower(AbstractCreature owner, ArrayList<AbstractCard> cards, int turns)
        {
            super(owner, Unravel.DATA);

            this.ID += counter++;
            this.cards.addAll(cards);

            Initialize(turns, PowerType.BUFF, true);
        }

        @Override
        public void updateDescription()
        {
            final StringJoiner sj = new StringJoiner(", ");
            for (AbstractCard card : cards)
            {
                sj.add(JUtils.ModifyString(card.name, w -> "#y" + w));
            }

            this.description = FormatDescription(0, amount, FETCH_AMOUNT) + sj.toString();
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            ReducePower(1);
            flashWithoutSound();
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            if (cards.size() > 0)
            {
                GameActions.Bottom.FetchFromPile(name, FETCH_AMOUNT, player.exhaustPile)
                .SetFilter(cards::contains)
                .SetOptions(false, false, true);
                flashWithoutSound();
            }
        }

        @Override
        public AbstractPower makeCopy()
        {
            return new UnravelPower(owner, cards, amount);
        }
    }
}