package pinacolada.cards.pcl.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.common.BurningPower;
import pinacolada.utilities.PCLActions;

public class Thoma extends PCLCard
{
    public static final PCLCardData DATA = Register(Thoma.class).SetSkill(1, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.Self).SetSeriesFromClassPackage(true);

    public Thoma()
    {
        super(DATA);

        Initialize(0, 4, 6, 1);
        SetUpgrade(0, 3, 0, 0);
        SetAffinity_Red(1, 0 ,0);
        SetAffinity_Orange(1, 0, 2);
    }

    @Override
    protected float GetInitialBlock()
    {
        return  (player.hasPower(BurningPower.POWER_ID) || player.hasPower(VulnerablePower.POWER_ID) ? super.GetInitialBlock() + magicNumber : super.GetInitialBlock());
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            PCLActions.Bottom.ApplyBurning(TargetHelper.Enemies(), secondaryValue);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
    }
}