package pinacolada.cards.base.cardeffects.CounterIntentEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.utilities.PCLActions;

public class CounterIntentEffect_Magic extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(PCLCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        int orbs = GetOrbs(nanami);

        PCLActions.Bottom.ChannelRandomOrbs(orbs);
    }

    @Override
    public String GetDescription(PCLCard nanami)
    {
        return ACTIONS.ChannelRandomOrbs(GetOrbs(nanami), true);
    }

    private int GetOrbs(PCLCard nanami)
    {
        return nanami.energyOnUse + (nanami.upgraded ? 2 : 1);
    }
}