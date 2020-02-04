package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.BorosPower;
import eatyourbeets.utilities.GameActions;

public class Boros extends AnimatorCard
{
    public static final String ID = Register_Old(Boros.class);

    public Boros()
    {
        super(ID, 4, CardRarity.RARE, CardType.POWER, CardTarget.SELF);

        Initialize(0, 0, 3);
        SetCostUpgrade(-1);

        SetEthereal(true);
        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.GainForce(magicNumber);
        GameActions.Bottom.GainTemporaryHP(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ApplyPower(p, p, new BorosPower(p));
    }
}