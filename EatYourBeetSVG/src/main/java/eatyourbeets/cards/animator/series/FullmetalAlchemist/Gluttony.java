package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class Gluttony extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Gluttony.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();
    public static final int MINIMUM_CARDS = 16;

    public Gluttony()
    {
        super(DATA);

        Initialize(0, 0, 4, 5);

        SetAffinity_Red(2);
        SetAffinity_Dark(2);

        SetHealing(true);
        SetExhaust(true);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(MINIMUM_CARDS);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetUnplayable((player.drawPile.size() + player.discardPile.size() + player.hand.size()) < MINIMUM_CARDS);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (p.drawPile.size() >= magicNumber)
        {
            GameActions.Bottom.MoveCards(p.drawPile, p.exhaustPile, magicNumber)
            .ShowEffect(true, true)
            .SetOrigin(CardSelection.Top);

            GameActions.Bottom.Heal(secondaryValue);
            GameActions.Bottom.GainForce(secondaryValue);
        }
    }
}