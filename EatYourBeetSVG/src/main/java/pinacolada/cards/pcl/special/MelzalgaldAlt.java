package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.HPAttribute;
import pinacolada.cards.pcl.series.OnePunchMan.Melzalgald;
import pinacolada.utilities.PCLGameUtilities;

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

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return heal <= 0 ? null : HPAttribute.Instance.SetCard(this, false).SetText(new ColoredString(heal, Colors.Cream(1f)));
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        this.heal = PCLGameUtilities.GetHealthRecoverAmount(secondaryValue);
    }
}