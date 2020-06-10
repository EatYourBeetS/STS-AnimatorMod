package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.common.TemporaryDrawReductionPower;
import eatyourbeets.utilities.GameActions;

public class SuikaIbuki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SuikaIbuki.class).SetAttack(2, CardRarity.COMMON, EYBAttackType.Normal);

    public SuikaIbuki()
    {
        super(DATA);

        Initialize(10, 6, 1, 1);
        SetUpgrade(2, 1, 0, 0);
        SetScaling(0, 0, 0);

        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        GameActions.Bottom.GainForce(secondaryValue, upgraded);
        GameActions.Bottom.StackPower(new TemporaryDrawReductionPower(p, magicNumber));
    }
}

