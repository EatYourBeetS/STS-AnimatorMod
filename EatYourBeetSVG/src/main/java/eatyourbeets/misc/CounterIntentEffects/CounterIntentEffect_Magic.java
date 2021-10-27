package eatyourbeets.misc.CounterIntentEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;

public class CounterIntentEffect_Magic extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(EYBCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        int orbs = GetOrbs(nanami);

        GameActions.Bottom.ChannelRandomOrbs(orbs);
    }

    @Override
    public String GetDescription(EYBCard nanami)
    {
        return ACTIONS.ChannelRandomOrbs(GetOrbs(nanami), true);
    }

    private int GetOrbs(EYBCard nanami)
    {
        return nanami.energyOnUse + (nanami.upgraded ? 2 : 1);
    }
}