package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActions;

public class Alexander extends AnimatorCard
{
    public static final String ID = Register_Old(Alexander.class);

    public Alexander()
    {
        super(ID, 1, CardRarity.COMMON, CardType.ATTACK, CardTarget.ALL_ENEMY);

        Initialize(5, 0);
        SetUpgrade(2, 0);
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
        GameActions.Bottom.GainForce(1);

        if (upgraded)
        {
            ForcePower.PreserveOnce();
        }
    }
}