package eatyourbeets.cards.effects.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;
import eatyourbeets.utilities.GameUtilities;

public class NanamiEffect_Strong_Debuff extends NanamiEffect
{
    @Override
    public void EnqueueActions(Nanami nanami, AbstractPlayer p, AbstractMonster m)
    {
        int stacks = GetStacks(nanami);
        GameActions.Bottom.ApplyWeak(p, m, stacks);
        GameActions.Bottom.ApplyVulnerable(p, m, stacks);
        GameActions.Bottom.ApplyPoison(p, m, GetPoison(nanami));
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        if (m != null)
        {
            GameUtilities.GetIntent(m).AddWeak();
        }
    }

    @Override
    public String GetDescription(Nanami nanami)
    {
        int stacks = GetStacks(nanami);
        return ACTIONS.Apply(stacks, GR.Tooltips.Weak, true) + " NL " +
               ACTIONS.Apply(stacks, GR.Tooltips.Vulnerable, true)  + " NL " +
               ACTIONS.Apply(GetPoison(nanami), GR.Tooltips.Poison, true);
    }

    private int GetStacks(Nanami nanami)
    {
        return 1 + (nanami.energyOnUse * (nanami.upgraded ? 2 : 1));
    }

    private int GetPoison(Nanami nanami)
    {
        return 2 + (nanami.energyOnUse * (nanami.upgraded ? 3 : 2));
    }
}