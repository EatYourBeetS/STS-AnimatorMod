package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;

public class Shine extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Shine.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Shine()
    {
        super(DATA);

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