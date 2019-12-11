package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.UnnamedCard;

public class SoundWave extends UnnamedCard
{
    public static final String ID = Register(SoundWave.class.getSimpleName());

    public SoundWave()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.SPECIAL, CardTarget.ENEMY);

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