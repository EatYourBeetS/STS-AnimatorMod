package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class Yusarin extends AnimatorCard
{
    public static final String ID = Register(Yusarin.class.getSimpleName());

    public Yusarin()
    {
        super(ID, 0, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);

        Initialize(0, 0, 1, 1);

        SetExhaust(true);
        SetSynergy(Synergies.Charlotte);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Motivate();
        GameActions.Bottom.Draw(this.magicNumber);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}