package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.UnnamedCard;

public class BlindingLight extends UnnamedCard
{
    public static final String ID = Register(BlindingLight.class.getSimpleName());

    public BlindingLight()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.BASIC, CardTarget.NONE);

        Initialize(3,0);

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
            upgradeDamage(3);
        }
    }
}