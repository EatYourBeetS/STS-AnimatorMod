package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;

public class Ascent extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Ascent.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None);

    public Ascent()
    {
        super(DATA);

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