package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.animator.Nanami;

public class NanamiEffect_Stun extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        int damage = GetDamage(nanami);
        if (damage > 0)
        {
            GameActionsHelper.DamageTarget(p, m, damage, nanami.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
        }

        int stacks = GetVulnerable(nanami);

        GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, stacks, false), stacks);
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
            return (modifier + 1) * nanami.damage;
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