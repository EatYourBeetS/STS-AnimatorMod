package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;

public class SoundWave extends UnnamedCard
{
    public static final EYBCardData DATA = Register(SoundWave.class).SetAttack(0, CardRarity.SPECIAL, EYBAttackType.Elemental, EYBCardTarget.Normal);

    public SoundWave()
    {
        super(DATA);

        Initialize(1,0);

        SetEcho(true);
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