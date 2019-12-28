package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.UnnamedCard;

public class Visions extends UnnamedCard
{
    public static final String ID = Register(Visions.class);

    public Visions()
    {
        super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.NONE);

        Initialize(0,0, 2);
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
            upgradeMagicNumber(1);
        }
    }
}