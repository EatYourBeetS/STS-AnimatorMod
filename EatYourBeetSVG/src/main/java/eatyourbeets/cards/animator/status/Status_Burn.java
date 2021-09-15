package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class Status_Burn extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(Status_Burn.class)
            .SetStatus(-2, CardRarity.COMMON, EYBCardTarget.None);

    public Status_Burn()
    {
        super(DATA, true);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 2);

        SetUnplayable(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (dontTriggerOnUseCard)
        {
            GameActions.Bottom.DealDamage(p, p, magicNumber, DamageInfo.DamageType.THORNS, AttackEffects.FIRE);
        }
    }
}