package eatyourbeets.cards.animator.curse.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class Curse_Decay extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Curse_Decay.class)
            .SetCurse(UNPLAYABLE_COST, EYBCardTarget.None, false);

    public Curse_Decay()
    {
        super(DATA);

        Initialize(0, 0, 2);

        SetAffinity_Dark(1);

        SetEndOfTurnPlay(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (dontTriggerOnUseCard)
        {
            GameActions.Bottom.TakeDamage(player, magicNumber, AttackEffects.FIRE);
        }
    }
}