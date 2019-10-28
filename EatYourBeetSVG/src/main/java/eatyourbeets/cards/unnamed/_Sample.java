package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.UnnamedCard;

public class _Sample extends UnnamedCard
{
    public static final String ID = Register(_Sample.class.getSimpleName());

    public _Sample()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);

        Initialize(0,0, 6, 1);
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
            upgradeMagicNumber(2);
            upgradeSecondaryValue(1);
        }
    }
}