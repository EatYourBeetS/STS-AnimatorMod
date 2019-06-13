package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.animator.Nanami;

public class NanamiEffect_Sleep extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        int poison = GetPoison(nanami);

        GameActionsHelper.ApplyPower(p, m, new PoisonPower(m, p, poison), poison);
    }

    public static void UpdateDescription(Nanami nanami)
    {
        nanami.rawDescription = Desc(POISON, GetPoison(nanami));
    }

    private static int GetPoison(Nanami nanami)
    {
        return 3 * (nanami.energyOnUse + (nanami.upgraded ? 2 : 1));
    }
}