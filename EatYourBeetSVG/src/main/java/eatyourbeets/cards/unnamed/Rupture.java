package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.UnnamedCard;

public class Rupture extends UnnamedCard
{
    public static final String ID = Register(Rupture.class.getSimpleName());

    public Rupture()
    {
        super(ID, 3, CardType.ATTACK, CardRarity.COMMON, CardTarget.NONE);

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