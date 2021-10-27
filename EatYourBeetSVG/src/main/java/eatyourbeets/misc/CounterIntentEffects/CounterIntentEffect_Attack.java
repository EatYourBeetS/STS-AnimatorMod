package eatyourbeets.misc.CounterIntentEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.GameActions;

public class CounterIntentEffect_Attack extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(EYBCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(GetBlock(nanami));
    }

    @Override
    public int GetBlock(EYBCard nanami)
    {
        return ModifyBlock((nanami.energyOnUse + 1) * nanami.baseBlock, nanami);
    }

    @Override
    public String GetDescription(EYBCard nanami)
    {
        return "";
    }
}