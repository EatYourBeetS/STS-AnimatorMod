package eatyourbeets.cards.animator.beta.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Curse_JunTormented extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_JunTormented.class)
            .SetCurse(-2, EYBCardTarget.None).SetSeries(CardSeries.RozenMaiden);
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.StackPower(new FrailPower(p, 1, true));
            GameActions.Bottom.StackPower(new WeakPower(p, 1, true));
        }
    }
    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return false;
    }

}