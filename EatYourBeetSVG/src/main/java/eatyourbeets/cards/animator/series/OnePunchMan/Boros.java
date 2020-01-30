package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.BorosPower;
import eatyourbeets.utilities.GameActions;

public class Boros extends AnimatorCard
{
    public static final String ID = Register(Boros.class);

    public Boros()
    {
        super(ID, 4, CardRarity.RARE, CardType.POWER, CardTarget.SELF);

        Initialize(0, 0, 2, 1);
        SetCostUpgrade(-1);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.GainForce(secondaryValue);
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ApplyPower(p, p, new BorosPower(p));
    }
}