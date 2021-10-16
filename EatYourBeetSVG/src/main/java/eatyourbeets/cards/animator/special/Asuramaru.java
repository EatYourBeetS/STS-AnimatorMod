package eatyourbeets.cards.animator.special;

import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;

public class Asuramaru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Asuramaru.class).SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None);

    public Asuramaru()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

   /* @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(new DemonFormPower(p, secondaryValue));
        GameActions.Bottom.GainIntellect(magicNumber);
        GameActions.Bottom.GainAgility(magicNumber);
        GameActions.Bottom.GainForce(magicNumber);
        GameActions.Bottom.MakeCardInHand(new Wound());
        GameActions.Bottom.MakeCardInHand(new Wound());
    }*/
}