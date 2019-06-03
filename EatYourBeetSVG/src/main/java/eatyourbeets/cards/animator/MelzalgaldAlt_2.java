package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.Synergies;

public class MelzalgaldAlt_2 extends MelzalgaldAlt
{
    public static final String ID = CreateFullID(MelzalgaldAlt_2.class.getSimpleName());

    public MelzalgaldAlt_2()
    {
        super(ID);

        Initialize(7,0, 1);

        SetSynergy(Synergies.OnePunchMan, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActionsHelper.ApplyPower(p, p, new DexterityPower(p, magicNumber), magicNumber);
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