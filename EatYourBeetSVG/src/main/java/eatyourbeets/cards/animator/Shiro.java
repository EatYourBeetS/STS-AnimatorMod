package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.ShiroPower;

public class Shiro extends AnimatorCard
{
    public static final String ID = CreateFullID(Shiro.class.getSimpleName());

    public Shiro()
    {
        super(ID, 4, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        if (!this.freeToPlayOnce)
        {
            this.setCostForTurn(this.cost - PlayerStatistics.getSynergiesThisTurn());
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new ShiroPower(p));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(3);
        }
    }
}