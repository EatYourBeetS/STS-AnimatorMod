package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.animator.Nanami;
import eatyourbeets.utilities.GameUtilities;

public class NanamiEffect_Defend extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        GameActionsHelper.DamageTarget(p, m, GetDamage(nanami), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameUtilities.UsePenNib();
    }

    public static String UpdateDescription(Nanami nanami)
    {
        return Desc(DAMAGE, GetDamage(nanami));
    }

    private static int GetDamage(Nanami nanami)
    {
        int diff = (nanami.damage - nanami.baseDamage);

        return ((nanami.energyOnUse + 1) * nanami.baseDamage) + diff;
    }
}