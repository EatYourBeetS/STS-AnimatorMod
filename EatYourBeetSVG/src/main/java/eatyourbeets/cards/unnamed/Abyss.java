package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;

public class Abyss extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Abyss.class).SetSkill(0, CardRarity.RARE, EYBCardTarget.None);

    public Abyss()
    {
        super(DATA);

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