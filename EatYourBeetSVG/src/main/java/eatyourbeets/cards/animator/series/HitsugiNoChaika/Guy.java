package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Guy extends AnimatorCard
{
    public static final String ID = Register_Old(Guy.class);

    public Guy()
    {
        super(ID, 0, CardRarity.COMMON, CardType.SKILL, CardTarget.SELF);

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
            .SetOptions(false, true);
        }
    }
}