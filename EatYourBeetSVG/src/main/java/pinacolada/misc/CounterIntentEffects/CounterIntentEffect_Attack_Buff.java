package pinacolada.misc.CounterIntentEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class CounterIntentEffect_Attack_Buff extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(PCLCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.GainBlock(GetBlock(nanami));
        PCLActions.Bottom.GainMight(GetForce(nanami));
    }

    @Override
    public String GetDescription(PCLCard nanami)
    {
        return ACTIONS.GainAmount(GetForce(nanami), GR.Tooltips.Might, true);
    }

    @Override
    public int GetBlock(PCLCard nanami)
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

    private int GetForce(PCLCard nanami)
    {
        return (nanami.energyOnUse + 1) * 2;
    }
}