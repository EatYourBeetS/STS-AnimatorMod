package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.UnnamedCard;

public class Time extends UnnamedCard
{
    public static final String ID = Register(Time.class.getSimpleName());

    public Time()
    {
        super(ID, 2, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);

        Initialize(0,0);

        SetExhaust(true);
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