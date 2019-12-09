package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.HiteiPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class Hitei extends AnimatorCard
{
    public static final String ID = Register(Hitei.class.getSimpleName(), EYBCardBadge.Synergy);

    public Hitei()
    {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0,2);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.StackPower(new HiteiPower(p, upgraded));

        if (HasActiveSynergy())
        {
            GameActions.Bottom.GainAgility(2);
            GameActions.Bottom.GainForce(2);
            GameActions.Bottom.GainIntellect(2);
        }
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