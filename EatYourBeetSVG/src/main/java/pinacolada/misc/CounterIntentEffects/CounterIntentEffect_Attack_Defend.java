package pinacolada.misc.CounterIntentEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class CounterIntentEffect_Attack_Defend extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(PCLCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.GainBlock(GetBlock(nanami));
        PCLActions.Bottom.DealDamage(p, m, GetDamage(nanami), nanami.damageTypeForTurn, AttackEffects.NONE);
    }

    @Override
    public String GetDescription(PCLCard nanami)
    {
        return "";
    }

    @Override
    public int GetBlock(PCLCard nanami)
    {
        return ModifyBlock((nanami.energyOnUse + 1) * nanami.baseBlock, nanami);
    }

    @Override
    public int GetDamage(PCLCard nanami)
    {
        return ModifyDamage((nanami.energyOnUse + 1) * nanami.baseDamage, nanami);
    }
}