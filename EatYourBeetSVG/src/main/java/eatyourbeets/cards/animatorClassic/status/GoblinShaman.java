package eatyourbeets.cards.animatorClassic.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class GoblinShaman extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(GoblinShaman.class).SetStatus(1, CardRarity.COMMON, EYBCardTarget.None);

    public GoblinShaman()
    {
        super(DATA);

        Initialize(0, 0);

        SetEndOfTurnPlay(true);
        this.series = CardSeries.GoblinSlayer;
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();
        GameActions.Bottom.Draw(1);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.StackPower(new FrailPower(p, 1, true));
        }
    }
}