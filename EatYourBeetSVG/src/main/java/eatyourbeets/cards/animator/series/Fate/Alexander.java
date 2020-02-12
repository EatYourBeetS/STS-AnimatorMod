package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActions;

public class Alexander extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Alexander.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL);

    public Alexander()
    {
        super(DATA);

        Initialize(7, 0);
        SetUpgrade(0, 0);
        SetScaling(0, 0, 1);

        SetMultiDamage(true);
        SetSynergy(Synergies.Fate);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.PlayCopy(this, null);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        ForcePower.PreserveOnce();

        if (upgraded)
        {
            GameActions.Bottom.GainForce(1);
        }
    }
}