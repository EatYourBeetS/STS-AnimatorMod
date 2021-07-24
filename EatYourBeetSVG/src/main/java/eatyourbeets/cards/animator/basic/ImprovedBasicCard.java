package eatyourbeets.cards.animator.basic;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.ColoredTexture;
import eatyourbeets.utilities.JUtils;

public abstract class ImprovedBasicCard extends AnimatorCard
{
    public final AffinityType affinityType;

    public ImprovedBasicCard(EYBCardData data, AffinityType type, String foregroundTexturePath)
    {
        super(data);

        InitializeAffinity(type, 1, 0, 0);

        this.affinityType = type;
        this.cropPortrait = false;
        this.portraitImg.color = affinityType.GetAlternateColor(0.85f);
        this.portraitForeground = new ColoredTexture(GR.GetTexture(foregroundTexturePath, true), null);
    }

    protected void SecondaryEffect()
    {
        if (affinityType == AffinityType.Star)
        {
            if (CheckAffinity(AffinityType.General, magicNumber))
            {
                JUtils.FindMax(CombatStats.Affinities.Powers, p -> p.amount).RetainOnce();
            }
        }
        else
        {
            if (CheckAffinity(affinityType, magicNumber))
            {
                CombatStats.Affinities.GetPower(affinityType).RetainOnce();
            }
        }
    }
}