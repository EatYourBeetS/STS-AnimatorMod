package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

public class Destiny extends AnimatorRelic implements Hidden
{
    public static final String ID = CreateFullID(Destiny.class);
    public static final int TEMP_HP = 1;

    public Destiny()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public int getPrice()
    {
        return 600;
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard)
    {
        super.onCardDraw(drawnCard);

        if (drawnCard.type == AbstractCard.CardType.CURSE)
        {
            GameActions.Bottom.GainTemporaryHP(1);
            flash();
        }
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, TEMP_HP);
    }
}