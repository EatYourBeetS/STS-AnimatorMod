package eatyourbeets.misc.CounterIntentEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;

public class CounterIntentEffect_None extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(EYBCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Draw(10);
    }

    @Override
    public String GetDescription(EYBCard nanami)
    {
        return ACTIONS.Draw(10, true);
    }
}