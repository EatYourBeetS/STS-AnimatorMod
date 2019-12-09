package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.ArcherPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Archer extends AnimatorCard
{
    public static final String ID = Register(Archer.class.getSimpleName(), EYBCardBadge.Synergy);

    public Archer()
    {
        super(ID, 1, CardType.POWER, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0, 2);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.StackPower(new ArcherPower(p, this.magicNumber));

        if (HasActiveSynergy())
        {
            for (AbstractMonster m1 : GameUtilities.GetCurrentEnemies(true))
            {
                GameActions.Bottom.ApplyVulnerable(p, m, 1);
            }
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