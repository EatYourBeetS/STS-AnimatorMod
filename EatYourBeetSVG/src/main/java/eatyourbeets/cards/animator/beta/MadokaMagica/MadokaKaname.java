package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import eatyourbeets.actions.basic.MoveCard;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MadokaKaname extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MadokaKaname.class).SetSkill(3, CardRarity.RARE, EYBCardTarget.None);

    public MadokaKaname()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetExhaust(true);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(!EffectHistory.TryActivateLimited(cardID))
        {
            return;
        }

        int numCurses = 0;

        numCurses += MoveCurses(p.discardPile, p.exhaustPile);

        if (upgraded)
        {
            numCurses += MoveCurses(p.hand, p.exhaustPile);
            numCurses += MoveCurses(p.drawPile, p.exhaustPile);
        }

        int intangibleCount = (int)Math.floor(numCurses / 2);

        GameActions.Bottom.StackPower(new IntangiblePlayerPower(p, intangibleCount));
    }

    private int MoveCurses(CardGroup source, CardGroup destination)
    {
        float duration = 0.3f;

        int numCurses = 0;

        for (AbstractCard card : source.group)
        {
            if (card.type == CardType.CURSE)
            {
                GameActions.Top.MoveCard(card, source, destination)
                        .ShowEffect(true, true, duration = Math.max(0.1f, duration * 0.8f))
                        .SetCardPosition(MoveCard.DEFAULT_CARD_X_RIGHT, MoveCard.DEFAULT_CARD_Y);
                numCurses++;
            }
        }

        return numCurses;
    }
}
