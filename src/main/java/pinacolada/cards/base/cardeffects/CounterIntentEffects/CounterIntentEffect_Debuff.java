package pinacolada.cards.base.cardeffects.CounterIntentEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class CounterIntentEffect_Debuff extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(PCLCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        int stack = GetStacks(nanami);

        PCLActions.Bottom.ApplyWeak(p, m, stack);
        PCLActions.Bottom.ApplyVulnerable(p, m, stack);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        if (m != null)
        {
            PCLGameUtilities.GetPCLIntent(m).AddWeak();
        }
    }

    @Override
    public String GetDescription(PCLCard nanami)
    {
        int stack = GetStacks(nanami);

        return ACTIONS.Apply(stack, GR.Tooltips.Weak, true) + " NL " +
               ACTIONS.Apply(stack, GR.Tooltips.Vulnerable, true);
    }

    private int GetStacks(PCLCard nanami)
    {
        return 1 + (nanami.energyOnUse * (nanami.upgraded ? 2 : 1));
    }
}