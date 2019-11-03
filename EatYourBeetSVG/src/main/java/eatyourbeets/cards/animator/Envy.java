package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.EnvyPower;

public class Envy extends AnimatorCard
{
    public static final String ID = Register(Envy.class.getSimpleName(), EYBCardBadge.Special);

    public Envy()
    {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0, 20);

        SetHealing(true);
        SetSynergy(Synergies.FullmetalAlchemist, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (PlayerStatistics.TryActivateLimited(cardID))
        {
            p.increaseMaxHp(1, true);
        }

        GameActionsHelper.ApplyPower(p, p, new EnvyPower(p, 1), 1);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(5);
        }
    }
}