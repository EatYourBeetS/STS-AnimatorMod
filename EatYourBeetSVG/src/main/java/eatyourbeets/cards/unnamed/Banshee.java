package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;

public class Banshee extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Banshee.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Banshee()
    {
        super(DATA);

        Initialize(0,0, 5);

        SetVoidbound(true);
        SetEthereal(true);
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
            upgradeMagicNumber(3);
        }
    }
}