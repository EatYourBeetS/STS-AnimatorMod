package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.HiteiPower;
import eatyourbeets.utilities.GameActions;

public class Hitei extends AnimatorCard
{
    public static final String ID = Register(Hitei.class);

    public Hitei()
    {
        super(ID, 1, CardRarity.UNCOMMON, CardType.POWER, CardTarget.SELF);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new HiteiPower(p, upgraded));

        if (HasSynergy())
        {
            GameActions.Bottom.GainAgility(2);
            GameActions.Bottom.GainForce(2);
            GameActions.Bottom.GainIntellect(2);
        }
    }
}