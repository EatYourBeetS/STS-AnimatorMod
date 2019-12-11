package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;

public class NanamiEffect_Strong_Debuff extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        int stack = GetStack(nanami);
        int poison = GetPoison(nanami);

        GameActions.Bottom.ApplyWeak(p, m, stack);
        GameActions.Bottom.ApplyVulnerable(p, m, stack);
        GameActions.Bottom.ApplyPoison(p, m, poison);
    }

    public static String UpdateDescription(Nanami nanami)
    {
        int stack = GetStack(nanami);

        return Desc(WEAK, stack, true) + Desc(VULNERABLE, stack, true) + Desc(POISON, GetPoison(nanami));
    }

    private static int GetStack(Nanami nanami)
    {
        return 1 + (nanami.energyOnUse * (nanami.upgraded ? 2 : 1));
    }

    private static int GetPoison(Nanami nanami)
    {
        return 2 + (nanami.energyOnUse * (nanami.upgraded ? 3 : 2));
    }
}