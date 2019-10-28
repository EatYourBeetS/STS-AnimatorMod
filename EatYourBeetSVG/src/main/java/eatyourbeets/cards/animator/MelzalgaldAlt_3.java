package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.Synergies;

public class MelzalgaldAlt_3 extends MelzalgaldAlt
{
    public static final String ID = Register(MelzalgaldAlt_3.class.getSimpleName());

    public MelzalgaldAlt_3()
    {
        super(ID);

        Initialize(7,0, 2);

        SetSynergy(Synergies.OnePunchMan, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActionsHelper.ApplyPower(p, p, new RegenPower(p, magicNumber), magicNumber);
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