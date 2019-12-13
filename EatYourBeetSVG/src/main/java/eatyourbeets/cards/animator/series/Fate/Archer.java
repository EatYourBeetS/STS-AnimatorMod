package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
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
            for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
            {
                GameActions.Bottom.ApplyVulnerable(p, enemy, 1);
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