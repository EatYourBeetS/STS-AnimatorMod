package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.replacement.AnimatorFrailPower;
import eatyourbeets.utilities.GameActions;

public class Curse_Shame extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_Shame.class)
            .SetCurse(-2, EYBCardTarget.None, false);

    public Curse_Shame()
    {
        super(DATA, true);

        Initialize(0, 0, 1);

        SetUnplayable(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        if (CombatStats.CanActivateLimited(cardID)) {
            for (AbstractPower po : player.powers)
            {
                if (FrailPower.POWER_ID.equals(po.ID) && CombatStats.TryActivateLimited(cardID))
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
            GameActions.Bottom.StackPower(new AnimatorFrailPower(player, magicNumber, true));
        }
    }
}