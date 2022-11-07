package eatyourbeets.cards.effects.NanamiEffects;

import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;
import eatyourbeets.utilities.GameUtilities;

public class NanamiEffect_Stun extends NanamiEffect
{
    @Override
    public void EnqueueActions(EYBCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        int damage = GetDamage(nanami);
        if (damage > 0)
        {
            GameActions.Bottom.DealDamage(p, m, damage, nanami.damageTypeForTurn, AttackEffects.NONE);
            GameUtilities.RemoveDamagePowers();
        }

        GameActions.Bottom.ApplyVulnerable(p, m, GetVulnerable(nanami));
    }

    @Override
    public String GetDescription(EYBCard nanami)
    {
        return ACTIONS.Apply(GetVulnerable(nanami), GR.Tooltips.Vulnerable, true);
    }

    @Override
    public int GetDamage(EYBCard nanami)
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

    private int GetVulnerable(EYBCard nanami)
    {
        return nanami.energyOnUse + 1;
    }
}