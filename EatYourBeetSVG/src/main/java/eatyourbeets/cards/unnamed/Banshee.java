package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.UnnamedCard;

public class Banshee extends UnnamedCard
{
    public static final String ID = Register(Banshee.class.getSimpleName());

    public Banshee()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);

        Initialize(0,0, 5);

        SetVoidbound(true);
        SetEthereal(true);
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
            upgradeMagicNumber(3);
        }
    }
}