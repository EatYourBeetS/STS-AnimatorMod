package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;

public class MelzalgaldAlt_1 extends MelzalgaldAlt
{
    public static final String ID = Register(MelzalgaldAlt_1.class.getSimpleName());

    public MelzalgaldAlt_1()
    {
        super(ID);

        Initialize(7,0, 2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActions.Bottom.GainForce(magicNumber);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}