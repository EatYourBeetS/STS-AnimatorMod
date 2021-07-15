package eatyourbeets.cards.animator.beta.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Curse_Depression extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_Depression.class)
            .SetCurse(-2, EYBCardTarget.None);
    static
    {
        DATA.CardRarity = CardRarity.SPECIAL;
    }

    public Curse_Depression()
    {
        super(DATA, true);

        SetSeries(CardSeries.DateALive);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.DiscardFromHand(name, 1, true)
        .ShowEffect(true, true)
        .SetOptions(false, false, false)
        .SetFilter(card -> !GameUtilities.IsCurseOrStatus(card));

        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return false;
    }

}