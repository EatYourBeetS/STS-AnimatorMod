package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.animator.ArcherPower;
import eatyourbeets.utilities.GameActionsHelper;

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
        GameActionsHelper.ApplyPower(p, p, new ArcherPower(p, this.magicNumber), this.magicNumber);

        if (HasActiveSynergy())
        {
            for (AbstractMonster m1 : PlayerStatistics.GetCurrentEnemies(true))
            {
                GameActionsHelper.ApplyPower(p, m1, new VulnerablePower(m1, 1, false), 1);
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