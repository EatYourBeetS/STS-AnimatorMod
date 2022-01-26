package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import pinacolada.cards.base.*;
import pinacolada.orbs.pcl.Air;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class YuukaKazami extends PCLCard
{
    public static final PCLCardData DATA = Register(YuukaKazami.class).SetSkill(2, CardRarity.COMMON, PCLCardTarget.Self).SetSeriesFromClassPackage();

    public YuukaKazami()
    {
        super(DATA);

        Initialize(0, 9, 2, 3);
        SetUpgrade(0, 3, 0, 0);
        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        if (CheckPrimaryCondition(true)) {
            PCLActions.Bottom.ChannelOrb(new Air());
        }
        else {
            PCLActions.Bottom.ChannelOrb(new Lightning());
        }
    }

    @Override
    public void triggerWhenDrawn()
    {
        PCLAffinity lowest = PCLJUtils.FindMin(PCLAffinity.Basic(), af -> PCLCombatStats.MatchingSystem.GetAffinityLevel(af,true));
        PCLActions.Bottom.AddAffinity(lowest, 1);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        PCLAffinity lowest = PCLJUtils.FindMin(PCLAffinity.Basic(), af -> PCLCombatStats.MatchingSystem.GetAffinityLevel(af,true));
        PCLActions.Bottom.AddAffinity(lowest, 1);
    }

    @Override
    public boolean CheckPrimaryCondition(boolean tryUse)
    {
        return GameActionManager.turn % 2 == 0;
    }
}

