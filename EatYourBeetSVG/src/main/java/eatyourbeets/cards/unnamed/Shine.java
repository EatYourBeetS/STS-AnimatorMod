package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.UnnamedCard;

public class Shine extends UnnamedCard
{
    public static final String ID = Register(Shine.class.getSimpleName());

    public Shine()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);

        Initialize(0,0, 7, 3);

        SetMastery(secondaryValue);
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
            upgradeSecondaryValue(-1);
        }
    }
}