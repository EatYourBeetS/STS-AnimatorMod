package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class RinTohsaka extends AnimatorCard
{
    public static final String ID = CreateFullID(RinTohsaka.class.getSimpleName());

    public RinTohsaka()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,7, 1);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (upgraded)
        {
            for (AbstractMonster m1 : PlayerStatistics.GetCurrentEnemies(true))
            {
                GameActionsHelper.ApplyPower(p, m1, new LockOnPower(m1, 1), 1);
            }
        }

        for (int i = 0; i < this.magicNumber; i++)
        {
            GameActionsHelper.ChannelOrb(Utilities.GetRandomOrb(), true);
        }

        if (HasActiveSynergy())
        {
            GameActionsHelper.GainBlock(p, this.block);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(2);
            target = CardTarget.ALL;
        }
    }
}