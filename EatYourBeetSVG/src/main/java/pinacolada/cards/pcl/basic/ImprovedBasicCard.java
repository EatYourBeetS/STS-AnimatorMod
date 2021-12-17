package pinacolada.cards.pcl.basic;

import eatyourbeets.utilities.AdvancedTexture;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.resources.GR;

public abstract class ImprovedBasicCard extends PCLCard
{
    public final PCLAffinity affinity;

    public ImprovedBasicCard(PCLCardData data, PCLAffinity affinity, String foregroundTexturePath)
    {
        super(data);

        InitializeAffinity(affinity, 1, affinity == PCLAffinity.Star ? 0 : 1, 0);

        this.affinity = affinity;
        this.cropPortrait = false;
        this.portraitImg.color = affinity.GetAlternateColor(0.85f);
        this.portraitForeground = new AdvancedTexture(GR.GetTexture(foregroundTexturePath, true), null);

        SetTag(GR.Enums.CardTags.IMPROVED_BASIC_CARD, true);
    }
}