package eatyourbeets.cards.effects.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class NanamiEffect_Debuff extends NanamiEffect
{
    @Override
    public void EnqueueActions(EYBCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        int stack = GetStacks(nanami);

        GameActions.Bottom.ApplyWeak(p, m, stack);
        GameActions.Bottom.ApplyVulnerable(p, m, stack);
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
    public String GetDescription(EYBCard nanami)
    {
        int stack = GetStacks(nanami);

        return ACTIONS.Apply(stack, GR.Tooltips.Weak, true) + " NL " +
               ACTIONS.Apply(stack, GR.Tooltips.Vulnerable, true);
    }

    private int GetStacks(EYBCard nanami)
    {
        return 1 + (nanami.energyOnUse * (nanami.upgraded ? 2 : 1));
    }
}