package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.UnnamedCard;

public class Ascent extends UnnamedCard
{
    public static final String ID = Register(Ascent.class.getSimpleName());

    public Ascent()
    {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);

        Initialize(0,0, 7, 9);

        SetVoidbound(true);
        SetMastery(9);
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
            upgradeSecondaryValue(-3);
        }
    }
}