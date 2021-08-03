package eatyourbeets.misc.NanamiEffects;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;

public class NanamiEffect_Attack_Defend extends NanamiEffect
{
    @Override
    public void EnqueueActions(Nanami nanami, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(GetBlock(nanami));
        GameActions.Bottom.DealDamage(p, m, GetDamage(nanami), nanami.damageTypeForTurn, AttackEffects.NONE);
    }

    @Override
    public String GetDescription(Nanami nanami)
    {
        return "";
    }

    @Override
    public int GetBlock(Nanami nanami)
    {
        return ModifyBlock((nanami.energyOnUse + 1) * nanami.baseBlock, nanami);
    }

    @Override
    public int GetDamage(Nanami nanami)
    {
        return ModifyDamage((nanami.energyOnUse + 1) * nanami.baseDamage, nanami);
    }
}