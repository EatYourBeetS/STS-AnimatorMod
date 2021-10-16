package eatyourbeets.cards.animator.special;

import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;

public class BlackLotus extends AnimatorCard
{
    public static final EYBCardData DATA = Register(BlackLotus.class).SetAttack(1, CardRarity.SPECIAL, EYBAttackType.Ranged, EYBCardTarget.ALL).SetColor(CardColor.COLORLESS);

    public BlackLotus()
    {
        super(DATA);

        Initialize(7, 5, 1);
        SetUpgrade(0, 0, 1);
    }

    /*@Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.SFX("ATTACK_DEFECT_BEAM");
        GameActions.Bottom.VFX(new ColoredSweepingBeamEffect(p.hb.cX, p.hb.cY, p.flipHorizontal, Color.valueOf("3d0066")), 0.3f);
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.FIRE);
        GameActions.Bottom.GainBlur(magicNumber);
    }*/
}