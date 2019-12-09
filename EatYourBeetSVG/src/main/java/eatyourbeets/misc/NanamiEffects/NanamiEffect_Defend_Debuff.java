package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.cards.animator.Nanami;
import eatyourbeets.utilities.GameUtilities;

public class NanamiEffect_Defend_Debuff extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        int damage = GetDamage(nanami);
        if (damage > 0)
        {
            GameActions.Bottom.DealDamage(p, m, damage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            GameUtilities.UsePenNib();
        }

        int vulnerable = GetVulnerable(nanami);

        GameActions.Bottom.ApplyVulnerable(p, m, vulnerable);
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