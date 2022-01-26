package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.powers.common.ImpairedPower;
import pinacolada.powers.special.InvertPower;
import pinacolada.utilities.PCLActions;

public class YukariYakumo extends PCLCard
{
    public static final PCLCardData DATA = Register(YukariYakumo.class).SetSkill(2, CardRarity.RARE, PCLCardTarget.Self).SetSeriesFromClassPackage();
    public static final int DESECRATION_COST = 10;

    public YukariYakumo()
    {
        super(DATA);

        Initialize(0, 0, 4, 2);
        SetUpgrade(0, 0, 0, 1);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);

        SetExhaust(true);
        SetAffinityRequirement(PCLAffinity.Dark, 7);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ApplyFrail(p, p, magicNumber);
        PCLActions.Bottom.ApplyVulnerable(p, p, magicNumber);
        PCLActions.Bottom.ApplyWeak(p, p, magicNumber);
        PCLActions.Bottom.StackPower(new ImpairedPower(player, magicNumber));
        PCLActions.Bottom.StackPower(new InvertPower(player, secondaryValue));

        if (TrySpendAffinity(PCLAffinity.Dark)) {
            //GameActions.Bottom.ApplyFrail(TargetHelper.Enemies(), secondaryValue);
            PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), secondaryValue);
            PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), secondaryValue);
        }
    }
}

