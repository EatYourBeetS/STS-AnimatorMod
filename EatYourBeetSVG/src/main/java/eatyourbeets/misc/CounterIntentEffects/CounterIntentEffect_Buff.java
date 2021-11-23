package eatyourbeets.misc.CounterIntentEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class CounterIntentEffect_Buff extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(EYBCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainMight(GetForce(nanami));
    }

    @Override
    public String GetDescription(EYBCard nanami)
    {
        return ACTIONS.GainAmount(GetForce(nanami), GR.Tooltips.Might, true);
    }

    private int GetForce(EYBCard nanami)
    {
        int energy = nanami.energyOnUse;
        if (nanami.upgraded)
        {
            return 4 + 3 * energy;
        }
        else
        {
            return (1 + energy) * 2;
        }
    }
}