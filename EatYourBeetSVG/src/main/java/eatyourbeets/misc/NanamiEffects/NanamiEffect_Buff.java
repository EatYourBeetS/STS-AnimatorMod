package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.animator.Nanami;

public class NanamiEffect_Buff extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, GetStrength(nanami)), GetStrength(nanami));
    }

    public static void UpdateDescription(Nanami nanami)
    {
        nanami.rawDescription = Desc(STRENGTH, GetStrength(nanami));
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