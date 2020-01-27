package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.BiyorigoPower;
import eatyourbeets.utilities.GameActions;

public class Biyorigo extends AnimatorCard
{
    public static final String ID = Register(Biyorigo.class, EYBCardBadge.Special);

    public Biyorigo()
    {
        super(ID, 2, CardRarity.RARE, CardType.POWER, CardTarget.SELF);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0, 0, 2, 0);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainThorns(magicNumber);
        GameActions.Bottom.GainArtifact(secondaryValue);
        GameActions.Bottom.StackPower(new BiyorigoPower(p, 1));
    }
}