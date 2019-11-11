package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;

public class MelzalgaldAlt_2 extends MelzalgaldAlt
{
    public static final String ID = Register(MelzalgaldAlt_2.class.getSimpleName());

    public MelzalgaldAlt_2()
    {
        super(ID);

        Initialize(7,0, 2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActionsHelper.GainIntellect(magicNumber);
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