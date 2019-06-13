package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.powers.PlayerStatistics;

public class Defend_Fate extends Defend
{
    public static final String ID = CreateFullID(Defend_Fate.class.getSimpleName());

    public Defend_Fate()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 3, 2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        int blockBonus = this.magicNumber * PlayerStatistics.GetCurrentEnemies(true).size();

        GameActionsHelper.GainBlock(p, this.block + blockBonus);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }
}