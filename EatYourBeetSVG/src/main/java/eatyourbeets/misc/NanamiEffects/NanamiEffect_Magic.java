package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.animator.Nanami;

public class NanamiEffect_Magic extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        int orbs = GetOrbs(nanami);

        for (int i = 0; i < orbs; i++)
        {
            GameActionsHelper.ChannelOrb(Utilities.GetRandomOrb(), true);
        }
    }

    public static void UpdateDescription(Nanami nanami)
    {
        nanami.rawDescription = Desc(RANDOM_ORB, GetOrbs(nanami));
    }

    private static int GetOrbs(Nanami nanami)
    {
        return nanami.energyOnUse + (nanami.upgraded ? 2 : 1);
    }
}