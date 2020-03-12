package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;

public class BlindingLight extends UnnamedCard
{
    public static final EYBCardData DATA = Register(BlindingLight.class).SetAttack(0, CardRarity.BASIC);

    public BlindingLight()
    {
        super(DATA);

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