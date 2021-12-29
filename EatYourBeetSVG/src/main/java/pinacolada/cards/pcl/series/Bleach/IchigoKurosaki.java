package pinacolada.cards.pcl.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.IchigoKurosaki_Bankai;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

public class IchigoKurosaki extends PCLCard
{
    public static final PCLCardData DATA = Register(IchigoKurosaki.class)
            .SetAttack(1, CardRarity.COMMON, PCLAttackType.Normal)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new IchigoKurosaki_Bankai(), false));

    public IchigoKurosaki()
    {
        super(DATA);

        Initialize(4, 0, 1, 15);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);

        SetProtagonist(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HORIZONTAL);

        PCLActions.Bottom.StackAffinityPower(PCLAffinity.Red, magicNumber);
        PCLActions.Bottom.StackAffinityPower(PCLAffinity.Green, magicNumber);

        if (CheckSpecialCondition(true))
        {
            PCLActions.Bottom.Exhaust(this);
            PCLActions.Bottom.MakeCardInDrawPile(new IchigoKurosaki_Bankai());
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return PCLCombatStats.MatchingSystem.GetPowerAmount(PCLAffinity.Red) >= secondaryValue;
    }
}