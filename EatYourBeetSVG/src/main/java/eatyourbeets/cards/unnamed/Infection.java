package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.UnnamedCard;

public class Infection extends UnnamedCard
{
    public static final String ID = Register(Infection.class);

    public Infection()
    {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(0,0);

        SetVoidbound(true);
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
            upgradeBaseCost(0);
        }
    }
}