package pinacolada.cards.pcl.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.powers.replacement.TemporaryDrawReductionPower;
import pinacolada.utilities.PCLActions;

public class Azekura extends PCLCard
{
    public static final PCLCardData DATA = Register(Azekura.class)
            .SetSkill(2, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage();

    public Azekura()
    {
        super(DATA);

        Initialize(0, 9, 2, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1);

        SetAffinityRequirement(PCLAffinity.Red, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.GainPlatedArmor(magicNumber);

        if (TrySpendAffinity(PCLAffinity.Red))
        {
            PCLActions.Bottom.GainThorns(secondaryValue);
        }
        else {
            PCLActions.Bottom.StackPower(new TemporaryDrawReductionPower(p, 1));
        }
    }
}