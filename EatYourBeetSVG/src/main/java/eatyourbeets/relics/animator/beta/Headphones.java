package eatyourbeets.relics.animator.beta;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

import static eatyourbeets.cards.base.EYBCard.HARMONIC;

public class Headphones extends AnimatorRelic
{
    public static final String ID = CreateFullID(Headphones.class);

    public Headphones()
    {
        super(ID, RelicTier.RARE, LandingSound.SOLID);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        SetEnabled(true);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        SetEnabled(true);
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if (IsEnabled() && drawnCard instanceof AnimatorCard && ((AnimatorCard) drawnCard).series != null) {
            GameActions.Bottom.ModifyTag(drawnCard,HARMONIC,true);
            flash();
        }
    }

    @Override
    public void atTurnStartPostDraw()
    {
        if (IsEnabled()) {
            SetEnabled(false);
            flash();
        }
    }
}