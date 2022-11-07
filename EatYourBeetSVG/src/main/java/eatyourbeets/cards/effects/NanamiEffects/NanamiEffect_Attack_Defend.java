package eatyourbeets.cards.effects.NanamiEffects;

import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.series.Katanagatari.Nanami;

public class NanamiEffect_Attack_Defend extends NanamiEffect
{
    @Override
    public void EnqueueActions(EYBCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(GetBlock(nanami));
        GameActions.Bottom.DealDamage(p, m, GetDamage(nanami), nanami.damageTypeForTurn, AttackEffects.BLUNT_HEAVY);
    }

    @Override
    public String GetDescription(EYBCard nanami)
    {
        return "";
    }

    @Override
    public int GetBlock(EYBCard nanami)
    {
        return ModifyBlock((nanami.energyOnUse + 1) * nanami.baseBlock, nanami);
    }

    @Override
    public int GetDamage(EYBCard nanami)
    {
        return ModifyDamage((nanami.energyOnUse + 1) * nanami.baseDamage, nanami);
    }
}