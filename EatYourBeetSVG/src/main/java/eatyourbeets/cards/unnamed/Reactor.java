package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.UnnamedCard;

public class Reactor extends UnnamedCard
{
    public static final String ID = Register(Reactor.class);

    public Reactor()
    {
        super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.NONE);

        Initialize(0,0, 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {

    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(1);
        }
    }
}