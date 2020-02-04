package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class NanamiEffect_Debuff extends NanamiEffect
{
    @Override
    public void EnqueueActions(Nanami nanami, AbstractPlayer p, AbstractMonster m)
    {
        int stack = GetStacks(nanami);

        GameActions.Bottom.ApplyWeak(p, m, stack);
        GameActions.Bottom.ApplyVulnerable(p, m, stack);
    }

    @Override
    public String GetDescription(Nanami nanami)
    {
        int stack = GetStacks(nanami);

        return ACTIONS.Apply(stack, GR.Tooltips.Weak, true) + " NL " +
               ACTIONS.Apply(stack, GR.Tooltips.Vulnerable, true);
    }

    private int GetStacks(Nanami nanami)
    {
        return 1 + (nanami.energyOnUse * (nanami.upgraded ? 2 : 1));
    }
}