package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;

public class Noise extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Noise.class).SetAttack(1, CardRarity.COMMON);

    public Noise()
    {
        super(DATA);

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