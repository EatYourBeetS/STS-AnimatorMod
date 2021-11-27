package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Curse_Decay extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_Decay.class)
            .SetCurse(-2, EYBCardTarget.None, false);

    public Curse_Decay()
    {
        super(DATA, true);

        Initialize(0, 0, 2, 3);

        SetUnplayable(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        if (CombatStats.CanActivateLimited(cardID)) {
            for (AbstractPower po : player.powers)
            {
                if (VulnerablePower.POWER_ID.equals(po.ID) && CombatStats.TryActivateLimited(cardID))
                {
                    GameActions.Top.RemovePower(player, po);
                    break;
                }
            }
        }
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (dontTriggerOnUseCard)
        {
            GameActions.Bottom.TakeDamage(magicNumber, AttackEffects.POISON);
        }
    }
}