package pinacolada.cards.pcl.special;

import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.series.OnePunchMan.Melzalgald;

public abstract class MelzalgaldAlt extends PCLCard
{
    protected final static CardSeries SERIES = Melzalgald.DATA.Series;

    public MelzalgaldAlt(PCLCardData data)
    {
        super(data);

        Initialize(4, 0, 2, 2);
        SetUpgrade(0, 0, 1, 1);

        SetAffinity_Star(1);

        SetRetainOnce(true);
        SetExhaust(true);
    }
}