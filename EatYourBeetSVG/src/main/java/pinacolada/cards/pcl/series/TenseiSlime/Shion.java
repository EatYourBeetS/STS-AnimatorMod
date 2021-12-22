package pinacolada.cards.pcl.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.stances.MightStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Shion extends PCLCard
{
    public static final PCLCardData DATA = Register(Shion.class)
            .SetAttack(2, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Shion()
    {
        super(DATA);

        Initialize(15, 0, 6, 0);
        SetUpgrade(3, 0, 1);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Orange(0, 0, 1);

        SetAffinityRequirement(PCLAffinity.Orange, 4);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null) {
            if (PCLGameUtilities.GetPowerAmount(enemy, VulnerablePower.POWER_ID) > 0 || PCLGameUtilities.GetPowerAmount(enemy, WeakPower.POWER_ID) > 0) {
                return super.ModifyDamage(enemy, amount + magicNumber);
            }
        }
        return super.ModifyDamage(enemy, amount);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);

        if (TrySpendAffinity(PCLAffinity.Orange))
        {
            PCLActions.Bottom.ChangeStance(MightStance.STANCE_ID);
        }
    }
}