package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.AdvancedTexture;

public abstract class ImprovedBasicCard extends AnimatorCard
{
    public final Affinity affinity;

    public ImprovedBasicCard(EYBCardData data, Affinity affinity, String foregroundTexturePath)
    {
        super(data);

        InitializeAffinity(affinity, 1, affinity == Affinity.Star ? 0 : 1, 0);

        this.affinity = affinity;
        this.cropPortrait = false;
        this.portraitImg.color = affinity.GetAlternateColor(0.85f);
        this.portraitForeground = new AdvancedTexture(GR.GetTexture(foregroundTexturePath, true), null);

        SetTag(GR.Enums.CardTags.IMPROVED_BASIC_CARD, true);
    }
}