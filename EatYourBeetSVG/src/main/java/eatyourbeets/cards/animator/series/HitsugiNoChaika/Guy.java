package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.basic.MoveCards;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Guy extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Guy.class).SetSkill(0, CardRarity.COMMON, EYBCardTarget.None);

    public Guy()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Draw(this.magicNumber);
        GameActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .SetOptions(false, false, true);

        if (HasSynergy())
        {
            GameActions.Bottom.MoveCards(p.drawPile, p.discardPile, secondaryValue)
            .ShowEffect(true, true)
            .SetOrigin(MoveCards.Origin.FromTop);
        }
    }
}