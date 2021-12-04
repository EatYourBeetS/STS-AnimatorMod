package eatyourbeets.powers.affinity;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;

public class MightPower extends AbstractAffinityPower
{
    public static final String POWER_ID = CreateFullID(MightPower.class);
    public static final Affinity AFFINITY_TYPE = Affinity.Red;

    public MightPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        return isActive && type == DamageInfo.DamageType.NORMAL ? damage * GetEffectiveMultiplier() : damage;
    }

    @Override
    public void OnUsingCard(AbstractCard c, AbstractPlayer p, AbstractMonster m)
    {
        if (c.baseDamage > 0 && isActive)
        {
            isActive = false;
            flash();
        }
    }
}