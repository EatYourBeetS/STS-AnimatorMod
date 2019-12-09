package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.UnnamedCard;

public class Noise extends UnnamedCard
{
    public static final String ID = Register(Noise.class.getSimpleName());

    public Noise()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.NONE);

        Initialize(6,0);

        SetEcho(true);
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
            upgradeDamage(3);
        }
    }
}