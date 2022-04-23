package eatyourbeets.cards.animator.special;

import eatyourbeets.cards.animator.series.NoGameNoLife.Sora;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;

public abstract class Sora_BattlePlan extends AnimatorCard
{
    protected final static CardSeries SERIES = Sora.DATA.Series;
    protected final static String IMAGE_PATH = GR.GetCardImage(GR.Animator.CreateID(Sora_BattlePlan.class.getSimpleName()));

    protected Sora_BattlePlan(EYBCardData cardData)
    {
        super(cardData);

        SetAffinity_Star(1);

        SetExhaust(true);

        cropPortrait = false;
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }
}