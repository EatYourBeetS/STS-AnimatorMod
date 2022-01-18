package pinacolada.cards.pcl.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.stances.EnduranceStance;
import pinacolada.stances.VelocityStance;
import pinacolada.utilities.PCLActions;

public class Nyanta extends PCLCard
{
    public static final PCLCardData DATA = Register(Nyanta.class)
            .SetSkill(2, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public Nyanta()
    {
        super(DATA);

        Initialize(0, 7, 2, 2);
        SetUpgrade(0, 2, 1, 1);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);

        SetRetainOnce(true);

        SetAffinityRequirement(PCLAffinity.Green, 3);
        SetAffinityRequirement(PCLAffinity.Orange, 3);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        PCLActions.Bottom.GainTemporaryThorns(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.TryChooseSpendAffinity(this, PCLAffinity.Orange, PCLAffinity.Green).AddConditionalCallback((cards) -> {
            for (AffinityChoice c : cards) {
                if (c.Affinity.equals(PCLAffinity.Green)) {
                    PCLActions.Bottom.ChangeStance(VelocityStance.STANCE_ID);
                }
                else {
                    PCLActions.Bottom.ChangeStance(EnduranceStance.STANCE_ID);
                }
            }
        });
    }
}