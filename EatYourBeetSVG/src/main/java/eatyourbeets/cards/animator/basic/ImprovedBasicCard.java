package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.AdvancedTexture;

public abstract class ImprovedBasicCard extends AnimatorCard
{
    public final Affinity affinity1;
    public final Affinity affinity2;

    public ImprovedBasicCard(EYBCardData data, Affinity affinity1, Affinity affinity2, String foregroundTexturePath)
    {
        super(data);

        InitializeAffinity(affinity1, 1, 0, 0);
        this.affinity1 = affinity1;

        if (affinity2 != null) {
            InitializeAffinity(affinity2, 1, 0, 0);
            this.affinity2 = affinity2;
        }
        else
        {
            this.affinity2 = null;
        }

        this.cropPortrait = false;
        this.portraitImg.color = affinity1.GetAlternateColor(0.85f);
        this.portraitForeground = new AdvancedTexture(GR.GetTexture(foregroundTexturePath, true), null);

        SetTag(GR.Enums.CardTags.IMPROVED_BASIC_CARD, true);
    }
}