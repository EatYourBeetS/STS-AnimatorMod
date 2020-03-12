package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;

public class Glass extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Glass.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public Glass()
    {
        super(DATA);

        Initialize(0,3);
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
            SetEcho(true);
        }
    }
}