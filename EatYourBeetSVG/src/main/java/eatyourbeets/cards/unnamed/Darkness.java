package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;

public class Darkness extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Darkness.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Darkness()
    {
        super(DATA);

        Initialize(0,3, 2, 3);
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
            upgradeBlock(2);
        }
    }
}