package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.Nanami;
import eatyourbeets.utilities.GameUtilities;

public class NanamiEffect_Stun extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        int damage = GetDamage(nanami);
        if (damage > 0)
        {
            GameActions.Bottom.DealDamage(p, m, damage, nanami.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
            GameUtilities.UsePenNib();
        }

        GameActions.Bottom.ApplyVulnerable(p, m, GetVulnerable(nanami));
    }

    public static String UpdateDescription(Nanami nanami)
    {
        return Desc(DAMAGE, GetDamage(nanami), true) + Desc(VULNERABLE, GetVulnerable(nanami));
    }

    private static int GetDamage(Nanami nanami)
    {
        int modifier = nanami.energyOnUse;

        if (modifier > 0)
        {
            int diff = (nanami.damage - nanami.baseDamage);

            return ((nanami.energyOnUse + 1) * nanami.baseDamage) + diff;
        }
        else
        {
            return -1;
        }
    }

    private static int GetVulnerable(Nanami nanami)
    {
        return nanami.energyOnUse + 1;
    }
}