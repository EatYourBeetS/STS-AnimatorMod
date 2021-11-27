package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Curse_Regret extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_Regret.class)
            .SetCurse(-2, EYBCardTarget.None, false);

    public Curse_Regret()
    {
        super(DATA, true);

        Initialize(0, 0, 1, 2);

        SetUnplayable(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        if (CombatStats.TryActivateLimited(cardID)) {
            GameActions.Bottom.RecoverHP(secondaryValue * player.hand.size());
        }
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (dontTriggerOnUseCard)
        {
            GameActions.Bottom.LoseHP(magicNumber * player.hand.size(), AbstractGameAction.AttackEffect.NONE);
        }
    }
}