package eatyourbeets.misc.CounterIntentEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class CounterIntentEffect_Attack_Buff extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(EYBCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(GetBlock(nanami));
        GameActions.Bottom.GainForce(GetForce(nanami));
    }

    @Override
    public String GetDescription(EYBCard nanami)
    {
        return ACTIONS.GainAmount(GetForce(nanami), GR.Tooltips.Force, true);
    }

    @Override
    public int GetBlock(EYBCard nanami)
    {
        if (nanami.energyOnUse > 0)
        {
            return ModifyBlock ((nanami.energyOnUse + 1) * nanami.baseBlock, nanami);
        }
        else
        {
            return 0;
        }
    }

    private int GetForce(EYBCard nanami)
    {
        return nanami.energyOnUse + 1;
    }
}