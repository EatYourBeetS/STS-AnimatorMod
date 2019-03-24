package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PenNibPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.animator.Nanami;

public class NanamiEffect_Defend_Debuff extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        int damage = GetDamage(nanami);
        if (damage > 0)
        {
            GameActionsHelper.DamageTarget(p, m, damage, nanami.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
            if (p.hasPower(PenNibPower.POWER_ID))
            {
                GameActionsHelper.AddToBottom(new ReducePowerAction(p, p, PenNibPower.POWER_ID, 1));
            }
        }

        int vulnerable = GetVulnerable(nanami);

        GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, vulnerable, false), vulnerable);
    }

    public static void UpdateDescription(Nanami nanami)
    {
        nanami.rawDescription = Desc(DAMAGE, GetDamage(nanami), true) + Desc(VULNERABLE, GetVulnerable(nanami));
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