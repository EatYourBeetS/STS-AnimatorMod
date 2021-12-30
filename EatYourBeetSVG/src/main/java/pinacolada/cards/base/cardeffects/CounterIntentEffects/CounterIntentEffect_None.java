package pinacolada.cards.base.cardeffects.CounterIntentEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.utilities.PCLActions;

public class CounterIntentEffect_None extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(PCLCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.Draw(10);
    }

    @Override
    public String GetDescription(PCLCard nanami)
    {
        return ACTIONS.Draw(10, true);
    }
}