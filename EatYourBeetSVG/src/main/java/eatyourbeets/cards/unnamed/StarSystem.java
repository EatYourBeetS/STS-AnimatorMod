package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;

public class StarSystem extends UnnamedCard
{
    public static final EYBCardData DATA = Register(StarSystem.class).SetPower(2, CardRarity.UNCOMMON);

    public StarSystem()
    {
        super(DATA);

        Initialize(0,0);
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