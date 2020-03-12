package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;

public class Accelerate extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Accelerate.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Accelerate()
    {
        super(DATA);

        Initialize(0,0, 2);
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