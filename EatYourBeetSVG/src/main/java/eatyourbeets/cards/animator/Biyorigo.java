package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.BiyorigoPower;

public class Biyorigo extends AnimatorCard
{
    public static final String ID = Register(Biyorigo.class.getSimpleName());

    public Biyorigo()
    {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0, 1);

        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new ArtifactPower(p, magicNumber), magicNumber);
        GameActionsHelper.ApplyPower(p, p, new BiyorigoPower(p, 1), 1);
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