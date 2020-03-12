package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;

public class Time extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Time.class).SetSkill(2, CardRarity.COMMON, EYBCardTarget.None);

    public Time()
    {
        super(DATA);

        Initialize(0,0);

        SetExhaust(true);
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
            upgradeBaseCost(1);
        }
    }
}