package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;

public class Visions extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Visions.class).SetPower(2, CardRarity.UNCOMMON);

    public Visions()
    {
        super(DATA);

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