package eatyourbeets.cards.animator.beta.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Curse_JunTormented extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_JunTormented.class)
            .SetCurse(-2, EYBCardTarget.None, false).SetSeries(CardSeries.RozenMaiden);
    static
    {
        DATA.CardRarity = CardRarity.SPECIAL;
    }

    public Curse_JunTormented()
    {
        super(DATA, true);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.Exhaust(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.ApplyFrail(null, p, 1);
            GameActions.Bottom.ApplyWeak(null, p, 1);
        }
    }
    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return false;
    }

}