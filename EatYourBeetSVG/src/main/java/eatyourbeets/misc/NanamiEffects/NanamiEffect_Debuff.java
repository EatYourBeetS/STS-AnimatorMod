package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.Nanami;

public class NanamiEffect_Debuff extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        int stack = GetStack(nanami);

        GameActions.Bottom.ApplyWeak(p, m, stack);
        GameActions.Bottom.ApplyVulnerable(p, m, stack);
    }

    public static String UpdateDescription(Nanami nanami)
    {
        int stack = GetStack(nanami);

        return Desc(WEAK, stack, true) + Desc(VULNERABLE, stack);
    }

    private static int GetStack(Nanami nanami)
    {
        return 1 + (nanami.energyOnUse * (nanami.upgraded ? 2 : 1));
    }
}