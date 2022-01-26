package pinacolada.cards.pcl.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.ThrowingKnife;
import pinacolada.stances.pcl.VelocityStance;
import pinacolada.utilities.PCLActions;

public class Sonic extends PCLCard
{
    public static final PCLCardData DATA = Register(Sonic.class)
            .SetSkill(1, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage()
            .SetMultiformData(2, false)
            .PostInitialize(data ->
            {
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, false);
                }
            });

    public Sonic()
    {
        super(DATA);

        Initialize(0, 1, 2);
        SetUpgrade(0, 1, 0);

        SetAffinity_Green(1,0,3);
        SetAffinity_Dark(1);

        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.Dark, 3);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            if (form == 1) {
                SetHaste(false);
                this.AddScaling(PCLAffinity.Green, 1);
            }
            else {
                SetHaste(true);
            }
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.GainBlur(magicNumber);
        PCLActions.Bottom.GainVelocity(magicNumber);

        int knives = 0;
        if (VelocityStance.IsActive())
        {
            knives += 1;
        }
        if (TrySpendAffinity(PCLAffinity.Dark))
        {
            knives += 1;
        }

        PCLActions.Bottom.CreateThrowingKnives(knives);
    }
}