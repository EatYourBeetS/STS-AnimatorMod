package eatyourbeets.cards.animatorClassic.basic;

import com.badlogic.gdx.graphics.Color;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.AdvancedTexture;

public abstract class ImprovedBasicCard extends AnimatorClassicCard
{
    public ImprovedBasicCard(EYBCardData data, String foregroundTexturePath)
    {
        super(data);

        this.cropPortrait = false;
        this.portraitImg.color = new Color(0.45f, 0.45f, 0.45f, 1f);
        this.portraitForeground = new AdvancedTexture(GR.GetTexture(foregroundTexturePath, true), null);

        SetTag(GR.Enums.CardTags.IMPROVED_BASIC_CARD, true);
    }
}