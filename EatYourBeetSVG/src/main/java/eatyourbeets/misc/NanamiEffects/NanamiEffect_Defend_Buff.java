package eatyourbeets.misc.NanamiEffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;
import eatyourbeets.utilities.GameUtilities;

public class NanamiEffect_Defend_Buff extends NanamiEffect
{
    @Override
    public void EnqueueActions(Nanami nanami, AbstractPlayer p, AbstractMonster m)
    {
        int damage = GetDamage(nanami);
        if (damage > 0)
        {
            GameActions.Bottom.DealDamage(p, m, damage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            GameUtilities.UsePenNib();
        }

        GameActions.Bottom.GainForce(GetForce(nanami));
    }

    @Override
    public String GetDescription(Nanami nanami)
    {
        return ACTIONS.GainAmount(GetForce(nanami), GR.Tooltips.Force, true);
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

    private int GetForce(Nanami nanami)
    {
        return nanami.energyOnUse + 1;
    }
}