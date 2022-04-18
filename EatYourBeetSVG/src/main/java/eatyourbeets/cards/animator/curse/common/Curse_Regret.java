package eatyourbeets.cards.animator.curse.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Curse_Regret extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Curse_Regret.class)
            .SetCurse(UNPLAYABLE_COST, EYBCardTarget.None, false);

    public Curse_Regret()
    {
        super(DATA);

        Initialize(0, 0);

        SetEndOfTurnPlay(true);
        SetAffinity_Dark(1);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        GameUtilities.ModifyMagicNumber(this, GameUtilities.GetOtherCardsInHand(this).size(), false);

        super.triggerOnEndOfTurnForPlayingCard();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (dontTriggerOnUseCard && magicNumber > 0)
        {
            GameActions.Bottom.LoseHP(null, player, magicNumber, AttackEffects.DARK)
            .IgnoreTempHP(false)
            .CanKill(false);
            GameUtilities.ModifyMagicNumber(this, 0, false);
        }
    }
}