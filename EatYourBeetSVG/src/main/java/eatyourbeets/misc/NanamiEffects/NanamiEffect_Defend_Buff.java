package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PenNibPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.animator.Nanami;

public class NanamiEffect_Defend_Buff extends NanamiEffect
{
    public static void Execute(AbstractPlayer p, AbstractMonster m, Nanami nanami)
    {
        int damage = GetDamage(nanami);
        if (damage > 0)
        {
            GameActionsHelper.DamageTarget(p, m, damage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            if (p.hasPower(PenNibPower.POWER_ID))
            {
                GameActionsHelper.AddToBottom(new ReducePowerAction(p, p, PenNibPower.POWER_ID, 1));
            }
        }

        int strength = GetStrength(nanami);

        GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, strength), strength);
    }

    public static void UpdateDescription(Nanami nanami)
    {
        nanami.rawDescription = Desc(DAMAGE, GetDamage(nanami), true) + Desc(STRENGTH, GetStrength(nanami));
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

    private static int GetStrength(Nanami nanami)
    {
        return nanami.energyOnUse + 1;
    }
}