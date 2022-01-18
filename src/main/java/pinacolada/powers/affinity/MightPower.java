package pinacolada.powers.affinity;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import pinacolada.cards.base.PCLAffinity;

public class MightPower extends AbstractPCLAffinityPower
{
    public static final String POWER_ID = CreateFullID(MightPower.class);
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Red;

    public MightPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        return isActive && type == DamageInfo.DamageType.NORMAL ? damage * GetEffectiveMultiplier() : damage;
    }

    @Override
    public void OnUsingCard(AbstractCard c)
    {
        if (c.baseDamage > 0 && isActive)
        {
            isActive = false;
            flash();
        }
    }
}