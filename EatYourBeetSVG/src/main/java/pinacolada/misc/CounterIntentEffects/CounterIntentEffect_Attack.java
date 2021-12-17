package pinacolada.misc.CounterIntentEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.utilities.PCLActions;

public class CounterIntentEffect_Attack extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(PCLCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.GainBlock(GetBlock(nanami));
    }

    @Override
    public int GetBlock(PCLCard nanami)
    {
        return ModifyBlock((nanami.energyOnUse + 1) * nanami.baseBlock, nanami);
    }

    @Override
    public String GetDescription(PCLCard nanami)
    {
        return "";
    }
}