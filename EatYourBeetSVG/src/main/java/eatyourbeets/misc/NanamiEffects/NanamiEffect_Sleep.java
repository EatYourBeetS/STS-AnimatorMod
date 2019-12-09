package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.cards.animator.Nanami;

public class NanamiEffect_Sleep extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        int poison = GetPoison(nanami);

        GameActionsHelper_Legacy.ApplyPower(p, m, new PoisonPower(m, p, poison), poison);
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