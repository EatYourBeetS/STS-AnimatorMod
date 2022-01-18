package pinacolada.cards.base.cardeffects.CounterIntentEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class CounterIntentEffect_Buff extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(PCLCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.GainMight(GetForce(nanami));
    }

    @Override
    public String GetDescription(PCLCard nanami)
    {
        return ACTIONS.GainAmount(GetForce(nanami), GR.Tooltips.Might, true);
    }

    private int GetForce(PCLCard nanami)
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