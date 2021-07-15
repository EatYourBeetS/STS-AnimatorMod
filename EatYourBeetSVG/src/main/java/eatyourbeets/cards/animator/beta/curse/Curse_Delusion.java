package eatyourbeets.cards.animator.beta.curse;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class Curse_Delusion extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_Delusion.class)
            .SetCurse(-2, EYBCardTarget.None);
    static
    {
        DATA.CardRarity = CardRarity.SPECIAL;
    }

    public Curse_Delusion()
    {
        super(DATA, false);

        SetSeries(CardSeries.GenshinImpact);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {

    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        GameActions.Bottom.Callback(() ->
        {
            AbstractCard card = null;
            RandomizedList<AbstractCard> possible = new RandomizedList<>();
            for (AbstractCard c : player.drawPile.group)
            {
                if (c.costForTurn >= 0)
                {
                    possible.Add(c);
                }
            }

            if (possible.Size() > 0)
            {
                card = possible.Retrieve(rng);
            }

            if (card instanceof EYBCard && !card.hasTag(AUTOPLAY))
            {
                ((EYBCard) card).SetAutoplay(true);
            }

            GameActions.Bottom.Flash(this);
        });
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.GainAgility(1, true);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return false;
    }

}