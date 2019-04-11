package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Boost;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class Tyuule extends AnimatorCard_Boost
{
    public static final String ID = CreateFullID(Tyuule.class.getSimpleName());

    public Tyuule()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);

        Initialize(0, 0, 3);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractMonster m1 : PlayerStatistics.GetCurrentEnemies(true))
        {
            GameActionsHelper.ApplyPower(p, m1, new PoisonPower(m1, p, this.magicNumber), this.magicNumber);
            GameActionsHelper.ApplyPower(p, m1, new VulnerablePower(m1, 1, false), 1);
        }

        if (ProgressBoost())
        {
            GameActionsHelper.GainEnergy(1);
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

    @Override
    protected int GetBaseBoost()
    {
        return 1;
    }
}