package pinacolada.cards.pcl.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Millim extends PCLCard
{
    public static final PCLCardData DATA = Register(Millim.class)
            .SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Fire)
            .SetSeriesFromClassPackage();

    public Millim()
    {
        super(DATA);

        Initialize(6, 0, 3, 2);

        SetAffinity_Star(1, 0, 0);
        SetAffinity_Red(0, 0, 1);
        SetAffinity_Green(0, 0, 1);
        SetAffinity_Blue(0, 0, 1);

        SetAffinityRequirement(PCLAffinity.General, 6);
        SetUnique(true, true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + PCLGameUtilities.GetDebuffsCount(enemy) * magicNumber);
    }


    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 3 == 1)
        {
            upgradeMagicNumber(1);
            upgradeDamage(1);
        }
        else
        {
            upgradeMagicNumber(0);
            upgradeDamage(2);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY);

        PCLActions.Bottom.ApplyBurning(TargetHelper.Normal(m), secondaryValue);
        PCLActions.Bottom.ApplyPoison(TargetHelper.Normal(m), secondaryValue);

        PCLActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
            for (int i = 0; i < magicNumber; i++)
            {
                PCLActions.Bottom.GainRandomAffinityPower(1, false);
            }
        });
    }
}