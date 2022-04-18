package eatyourbeets.cards.effects.NanamiEffects;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;
import eatyourbeets.utilities.GameUtilities;

public class NanamiEffect_Defend_Debuff extends NanamiEffect
{
    @Override
    public void EnqueueActions(Nanami nanami, AbstractPlayer p, AbstractMonster m)
    {
        int damage = GetDamage(nanami);
        if (damage > 0)
        {
            GameActions.Bottom.DealDamage(p, m, damage, DamageInfo.DamageType.THORNS, AttackEffects.BLUNT_LIGHT);
            GameUtilities.RemoveDamagePowers();
        }

        GameActions.Bottom.ApplyVulnerable(p, m, GetVulnerable(nanami));
    }

    @Override
    public String GetDescription(Nanami nanami)
    {
        return ACTIONS.Apply(GetVulnerable(nanami), GR.Tooltips.Vulnerable, true);
    }

    @Override
    public int GetDamage(Nanami nanami)
    {
        if (nanami.energyOnUse > 0)
        {
            return ModifyDamage((nanami.energyOnUse + 1) * nanami.baseDamage, nanami);
        }
        else
        {
            return 0;
        }
    }

    private int GetVulnerable(Nanami nanami)
    {
        return nanami.energyOnUse + 1;
    }
}