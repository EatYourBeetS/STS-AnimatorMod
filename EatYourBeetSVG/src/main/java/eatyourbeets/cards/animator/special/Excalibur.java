package eatyourbeets.cards.animator.special;

import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;

public class Excalibur extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Excalibur.class).SetAttack(3, CardRarity.SPECIAL, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public Excalibur()
    {
        super(DATA);

        Initialize(80, 0);
        SetUpgrade(19, 0);

        SetRetain(true);
        SetExhaust(true);
    }

    /*@Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new IntangiblePlayerPower(p, 1));
        GameActions.Bottom.VFX(new BorderLongFlashEffect(Color.GOLD));

        for (AbstractCreature m1 : GameUtilities.GetEnemies(true))
        {
            GameActions.Bottom.VFX(new VerticalImpactEffect(m1.hb_x, m1.hb_y));
        }

        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }*/
}