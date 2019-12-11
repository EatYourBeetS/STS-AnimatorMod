package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;

public class NanamiEffect_Unknown extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        GameActions.Bottom.GainEnergy(1);

        int temporaryHP = GetTempHP(nanami);
        if (temporaryHP > 0)
        {
            GameActions.Bottom.GainTemporaryHP(temporaryHP);
        }
    }

    public static String UpdateDescription(Nanami nanami)
    {
        return Desc(TEMP_HP, GetTempHP(nanami), true) + Desc(ENERGY, 1);
    }

    private static int GetTempHP(Nanami nanami)
    {
        return nanami.energyOnUse * (nanami.upgraded ? 6 : 5);
    }
}