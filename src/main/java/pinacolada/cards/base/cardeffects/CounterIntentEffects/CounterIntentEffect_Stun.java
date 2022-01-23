package pinacolada.cards.base.cardeffects.CounterIntentEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.effects.AttackEffects;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class CounterIntentEffect_Stun extends CounterIntentEffect
{
    @Override
    public void EnqueueActions(PCLCard nanami, AbstractPlayer p, AbstractMonster m)
    {
        int damage = GetDamage(nanami);
        if (damage > 0)
        {
            PCLActions.Bottom.DealDamage(p, m, damage, nanami.damageTypeForTurn, AttackEffects.NONE);
            PCLGameUtilities.RemoveDamagePowers();
        }

        PCLActions.Bottom.ApplyVulnerable(p, m, GetVulnerable(nanami));
    }

    @Override
    public String GetDescription(PCLCard nanami)
    {
        return ACTIONS.Apply(GetVulnerable(nanami), GR.Tooltips.Vulnerable, true);
    }

    @Override
    public int GetDamage(PCLCard nanami)
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

    private int GetVulnerable(PCLCard nanami)
    {
        return nanami.energyOnUse + 1;
    }
}