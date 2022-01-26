package pinacolada.cards.pcl.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_Status;
import pinacolada.cards.pcl.series.GoblinSlayer.GoblinSlayer;
import pinacolada.utilities.PCLActions;

public class GoblinShaman extends PCLCard_Status
{
    public static final PCLCardData DATA = Register(GoblinShaman.class)
            .SetStatus(1, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeries(GoblinSlayer.DATA.Series);

    public GoblinShaman()
    {
        super(DATA, true);

        Initialize(0, 0);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        PCLActions.Bottom.Draw(1);
        PCLActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (this.dontTriggerOnUseCard)
        {
            PCLActions.Bottom.ApplyFrail(null, p, 1);
            PCLActions.Bottom.GainDesecration(1);
        }
    }
}