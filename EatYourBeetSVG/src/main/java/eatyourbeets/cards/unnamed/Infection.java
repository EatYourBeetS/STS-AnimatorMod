package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;

public class Infection extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Infection.class).SetSkill(1, CardRarity.RARE, EYBCardTarget.Normal);

    public Infection()
    {
        super(DATA);

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