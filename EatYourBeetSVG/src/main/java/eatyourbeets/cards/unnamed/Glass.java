package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.UnnamedCard;

public class Glass extends UnnamedCard
{
    public static final String ID = Register(Glass.class.getSimpleName());

    public Glass()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);

        Initialize(0,3);
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
            SetEcho(true);
        }
    }
}