package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.UnnamedCard;

public class Abyss extends UnnamedCard
{
    public static final String ID = Register(Abyss.class.getSimpleName());

    public Abyss()
    {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);

        Initialize(0,0, 5);

        SetMastery(2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        //GameActionsHelper_Legacy.AddToBottom(new LoseHPAction(p, p, magicNumber));
        //GameActionsHelper_Legacy.AddToBottom(new FetchAction(PlayerStatistics.Void, 1));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(-2);
        }
    }
}