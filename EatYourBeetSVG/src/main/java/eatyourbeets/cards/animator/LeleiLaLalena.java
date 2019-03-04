package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class LeleiLaLalena extends AnimatorCard
{
    public static final String ID = CreateFullID(LeleiLaLalena.class.getSimpleName());

    public LeleiLaLalena()
    {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(0,0, 1);

        baseSecondaryValue = secondaryValue = 1;

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        if (HasActiveSynergy())
        {
            target = CardTarget.ALL;
        }
        else
        {
            target = CardTarget.SELF;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (int i = 0; i < this.magicNumber; i++)
        {
            GameActionsHelper.ChannelOrb(new Frost(), true);
        }

        if (upgraded)
        {
            GameActionsHelper.CycleCardAction(1);
        }

        if (HasActiveSynergy())
        {
            for (AbstractMonster m1 : PlayerStatistics.GetCurrentEnemies(true))
            {
                GameActionsHelper.ApplyPower(p, m1, new WeakPower(m1, this.secondaryValue, false), this.secondaryValue);
            }
        }
    }

    @Override
    public void upgrade() 
    {
        TryUpgrade();
    }
}