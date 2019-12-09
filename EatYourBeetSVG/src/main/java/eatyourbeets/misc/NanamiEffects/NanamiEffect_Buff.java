package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.Nanami;

public class NanamiEffect_Buff extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        GameActions.Bottom.GainForce(GetStrength(nanami));
    }

    public static String UpdateDescription(Nanami nanami)
    {
        return Desc(STRENGTH, GetStrength(nanami));
    }

    private static int GetStrength(Nanami nanami)
    {
        int energy = nanami.energyOnUse;

        if (energy == 0)
        {
            return 1;
        }
        else if (nanami.upgraded)
        {
            return 2 + (energy / 2) + energy;
        }
        else
        {
            return 1 + energy;
        }
    }
}