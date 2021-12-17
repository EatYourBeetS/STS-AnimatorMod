package pinacolada.misc.CounterIntentEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class CounterIntentEffect_Strong_Debuff extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(PCLCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        int stacks = GetStacks(nanami);
        PCLActions.Bottom.ApplyWeak(p, m, stacks);
        PCLActions.Bottom.ApplyVulnerable(p, m, stacks);
        PCLActions.Bottom.ApplyPoison(p, m, GetPoison(nanami));
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
        int stacks = GetStacks(nanami);
        return ACTIONS.Apply(stacks, GR.Tooltips.Weak, true) + " NL " +
               ACTIONS.Apply(stacks, GR.Tooltips.Vulnerable, true)  + " NL " +
               ACTIONS.Apply(GetPoison(nanami), GR.Tooltips.Poison, true);
    }

    private int GetStacks(PCLCard nanami)
    {
        return 1 + (nanami.energyOnUse * (nanami.upgraded ? 2 : 1));
    }

    private int GetPoison(PCLCard nanami)
    {
        return 2 + (nanami.energyOnUse * (nanami.upgraded ? 3 : 2));
    }
}