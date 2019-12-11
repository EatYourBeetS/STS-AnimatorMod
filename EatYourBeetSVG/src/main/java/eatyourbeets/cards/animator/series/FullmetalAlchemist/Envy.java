package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.EnvyPower;

public class Envy extends AnimatorCard
{
    public static final String ID = Register(Envy.class.getSimpleName(), EYBCardBadge.Special);

    public Envy()
    {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0);

        SetEthereal(true);
        SetSynergy(Synergies.FullmetalAlchemist, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.StackPower(new EnvyPower(p, 1));

        int tempHP = Math.floorDiv(p.maxHealth - p.currentHealth, 5);
        if (tempHP > 0)
        {
            GameActions.Bottom.GainTemporaryHP(tempHP);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            SetEthereal(false);
        }
    }
}