package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;

public class Rupture extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Rupture.class).SetAttack(3, CardRarity.COMMON);

    public Rupture()
    {
        super(DATA);

        Initialize(20,0, 12);
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
            upgradeBaseCost(2);
        }
    }
}