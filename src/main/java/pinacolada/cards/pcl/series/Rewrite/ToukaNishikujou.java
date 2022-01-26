package pinacolada.cards.pcl.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class ToukaNishikujou extends PCLCard
{
    public static final PCLCardData DATA = Register(ToukaNishikujou.class).SetSkill(2, CardRarity.UNCOMMON, PCLCardTarget.None).SetSeriesFromClassPackage();

    public ToukaNishikujou()
    {
        super(DATA);

        Initialize(0, 6, 1, 1);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(1, 0, 0);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        PCLActions.Bottom.Cycle(name, secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.CreateThrowingKnives(magicNumber).AddCallback(card -> {
            if (card != null) {
                PCLAffinity af = PCLCombatStats.MatchingSystem.AffinityMeter.GetCurrentAffinity();
                PCLActions.Bottom.IncreaseScaling(card, af, PCLGameUtilities.InStance(af) ? secondaryValue + 1 : secondaryValue);
            }
        });
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        PCLAffinity af = PCLCombatStats.MatchingSystem.AffinityMeter.GetCurrentAffinity();
        return PCLGameUtilities.InStance(af);
    }
}