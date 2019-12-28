package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.UnnamedCard;

public class Darkness extends UnnamedCard
{
    public static final String ID = Register(Darkness.class);

    public Darkness()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);

        Initialize(0,3, 2, 3);
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
            upgradeBlock(2);
        }
    }
}