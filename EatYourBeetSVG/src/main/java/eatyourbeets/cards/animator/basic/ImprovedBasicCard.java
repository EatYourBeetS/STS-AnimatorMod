package eatyourbeets.cards.animator.basic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.JUtils;

public abstract class ImprovedBasicCard extends AnimatorCard
{
    public final AffinityType affinityType;

    private Color overrideRenderColor;

    public ImprovedBasicCard(EYBCardData data, AffinityType type)
    {
        super(data);

        InitializeAffinity(type, 1, 0, 0);

        this.affinityType = type;
        this.cropPortrait = false;
        this.overrideRenderColor = Color.WHITE.cpy().lerp(affinityType.GetAlternateColor(), 0.85f);
    }

    protected void SecondaryEffect()
    {
        if (affinityType == AffinityType.Star)
        {
            if (CheckTeamwork(AffinityType.General, magicNumber))
            {
                JUtils.FindMax(CombatStats.Affinities.Powers, p -> p.amount).RetainOnce();
            }
        }
        else
        {
            if (CheckTeamwork(affinityType, magicNumber))
            {
                CombatStats.Affinities.GetPower(affinityType).RetainOnce();
            }
        }
    }

    protected abstract Texture GetPortraitForeground();

    @Override
    protected void renderPortrait(SpriteBatch sb)
    {
        Color temp = _renderColor.Get(this);
        overrideRenderColor.a = temp.a;
        _renderColor.Set(this, overrideRenderColor);
        super.renderPortrait(sb);
        _renderColor.Set(this, temp);
        Texture cache = portraitImg;
        portraitImg = GetPortraitForeground();
        super.renderPortrait(sb);
        portraitImg = cache;
    }
}