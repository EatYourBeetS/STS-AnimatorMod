package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper_Legacy; import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.Nanami;

public class NanamiEffect_Attack_Defend extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        GameActions.Bottom.GainBlock(GetBlock(nanami));
        GameActions.Bottom.DealDamage(p, m, GetDamage(nanami), nanami.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
    }

    public static String UpdateDescription(Nanami nanami)
    {
        return Desc(DAMAGE, GetDamage(nanami), true) + Desc(BLOCK, GetBlock(nanami));
    }

    private static int GetBlock(Nanami nanami)
    {
        int diff = (nanami.block - nanami.baseBlock);

        return ((nanami.energyOnUse + 1) * nanami.baseBlock) + diff;
    }

    private static int GetDamage(Nanami nanami)
    {
        int diff = (nanami.damage - nanami.baseDamage);

        return ((nanami.energyOnUse + 1) * nanami.baseDamage) + diff;
    }
}