package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.animator.Nanami;

public class NanamiEffect_Unknown extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        GameActionsHelper.GainEnergy(1);

        int temporaryHP = GetTempHP(nanami);
        if (temporaryHP > 0)
        {
            GameActionsHelper.GainTemporaryHP(p, p, temporaryHP);
        }
    }

    public static void UpdateDescription(Nanami nanami)
    {
        nanami.rawDescription = Desc(TEMP_HP, GetTempHP(nanami), true) + Desc(ENERGY, 1);
    }

    private static int GetTempHP(Nanami nanami)
    {
        return nanami.energyOnUse * (nanami.upgraded ? 6 : 5);
    }
}