package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.animator.Nanami;

public class NanamiEffect_Attack extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        GameActionsHelper.GainBlock(p, GetBlock(nanami));
    }

    public static String UpdateDescription(Nanami nanami)
    {
        return Desc(BLOCK, GetBlock(nanami));
    }

    private static int GetBlock(Nanami nanami)
    {
        int diff = (nanami.block - nanami.baseBlock);

        return ((nanami.energyOnUse + 1) * nanami.baseBlock) + diff;
    }
}