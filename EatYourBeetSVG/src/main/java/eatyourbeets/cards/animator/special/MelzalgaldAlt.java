package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.OnePunchMan.Melzalgald;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameUtilities;

public abstract class MelzalgaldAlt extends AnimatorCard
{
    protected final static CardSeries SERIES = Melzalgald.DATA.Series;

    public MelzalgaldAlt(EYBCardData data)
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
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        this.heal = GameUtilities.GetHealthRecoverAmount(secondaryValue);
    }
}