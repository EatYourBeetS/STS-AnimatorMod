package pinacolada.cards.base.cardeffects.CounterIntentEffects;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class CounterIntentEffect_Defend extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(PCLCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        PCLActions.Bottom.DealDamage(p, m, GetDamage(nanami), DamageInfo.DamageType.THORNS, AttackEffects.BLUNT_LIGHT);
        PCLGameUtilities.UsePenNib();
    }

    @Override
    public int GetDamage(PCLCard nanami)
    {
        return ModifyDamage((nanami.energyOnUse + 1) * nanami.baseDamage, nanami);
    }

    @Override
    public String GetDescription(PCLCard nanami)
    {
        return "";
    }
}