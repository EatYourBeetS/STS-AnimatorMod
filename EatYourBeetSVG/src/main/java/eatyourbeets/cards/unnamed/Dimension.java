package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.UnnamedCard;

public class Dimension extends UnnamedCard
{
    public static final String ID = Register(Dimension.class);

    public Dimension()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);

        Initialize(0,6);
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
            upgradeBlock(3);
        }
    }
}