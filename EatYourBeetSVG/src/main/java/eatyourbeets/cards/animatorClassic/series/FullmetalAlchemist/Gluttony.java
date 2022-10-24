package eatyourbeets.cards.animatorClassic.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class Gluttony extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Gluttony.class).SetSeriesFromClassPackage().SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Gluttony()
    {
        super(DATA);

        Initialize(0, 0, 4, 16);

        SetHealing(true);
        SetExhaust(true);
        
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        boolean playable = super.cardPlayable(m);

        if (playable)
        {
            int totalCards = player.drawPile.size() + player.discardPile.size() + player.hand.size();
            if (totalCards < secondaryValue)
            {
                cantUseMessage = cardData.Strings.EXTENDED_DESCRIPTION[0];

                return false;
            }
        }

        return playable && (player.drawPile.size() >= magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (p.drawPile.size() >= magicNumber)
        {
            GameActions.Bottom.MoveCards(p.drawPile, p.exhaustPile, magicNumber)
            .ShowEffect(true, true)
            .SetOrigin(CardSelection.Top);

            GameActions.Bottom.Heal(magicNumber);
            GameActions.Bottom.GainForce(magicNumber);
        }
    }
}