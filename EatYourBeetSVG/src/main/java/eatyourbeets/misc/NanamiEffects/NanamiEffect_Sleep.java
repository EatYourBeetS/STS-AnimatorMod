package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.Nanami;

public class NanamiEffect_Sleep extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        int poison = GetPoison(nanami);

        GameActions.Bottom.ApplyPoison(p, m, poison);
    }

    public static String UpdateDescription(Nanami nanami)
    {
        return Desc(POISON, GetPoison(nanami));
    }

    private static int GetPoison(Nanami nanami)
    {
        return 3 * (nanami.energyOnUse + (nanami.upgraded ? 2 : 1));
    }
}