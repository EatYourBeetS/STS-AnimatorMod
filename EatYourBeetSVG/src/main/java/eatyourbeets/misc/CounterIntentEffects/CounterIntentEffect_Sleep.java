package eatyourbeets.misc.CounterIntentEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class CounterIntentEffect_Sleep extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(EYBCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ApplyPoison(p, m, GetPoison(nanami));
    }

    @Override
    public String GetDescription(EYBCard nanami)
    {
        return ACTIONS.Apply(GetPoison(nanami), GR.Tooltips.Poison, true);
    }

    private int GetPoison(EYBCard nanami)
    {
        return 3 * (nanami.energyOnUse + (nanami.upgraded ? 2 : 1));
    }
}