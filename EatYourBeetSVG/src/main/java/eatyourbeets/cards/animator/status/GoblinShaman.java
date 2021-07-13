package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;

public class GoblinShaman extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(GoblinShaman.class).SetStatus(1, CardRarity.COMMON, EYBCardTarget.None);

    public GoblinShaman()
    {
        super(DATA, true);

        Initialize(0, 0);

        SetSeries(CardSeries.GoblinSlayer);
        SetAffinity(0, 0, 1, 0, 1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();
        GameActions.Bottom.Draw(1);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.StackPower(new FrailPower(p, 1, true));
        }
    }
}