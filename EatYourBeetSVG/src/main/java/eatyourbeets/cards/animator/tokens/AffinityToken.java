package eatyourbeets.cards.animator.tokens;

import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.ColoredTexture;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public abstract class AffinityToken extends AnimatorCard
{
    public static final String ID = GR.Animator.CreateID(AffinityToken.class.getSimpleName());

    protected static final ArrayList<EYBCardData> cards = new ArrayList<>();
    protected final AffinityType affinityType;

    public static EYBCardData Register(Class<? extends AnimatorCard> type)
    {
        EYBCardData data = AnimatorCard.Register(type).SetPower(2, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);
        cards.add(data);
        return data;
    }

    public static ArrayList<EYBCardData> GetCards()
    {
        return cards;
    }

    public static EYBCardData GetCard(AffinityType type)
    {
        switch (type)
        {
            case Red: return AffinityToken_Red.DATA;
            case Green: return AffinityToken_Green.DATA;
            case Blue: return AffinityToken_Blue.DATA;
            case Orange: return AffinityToken_Orange.DATA;
            case Light: return AffinityToken_Light.DATA;
            case Dark: return AffinityToken_Dark.DATA;
            //case Star: return AffinityToken_Star.DATA;

            default:
            {
                JUtils.LogWarning(AffinityToken.class, "Affinity token not supported for " + type);
                return null;
            }
        }
    }

    protected AffinityToken(EYBCardData cardData, AffinityType affinityType)
    {
        super(cardData);

        Initialize(0, 0, affinityType == AffinityType.Star ? 1 : 2);
        SetCostUpgrade(-1);
        InitializeAffinity(affinityType, 2, 0, 0);

        this.affinityType = affinityType;
        this.cropPortrait = false;
        this.portraitForeground = portraitImg;
        this.portraitImg = new ColoredTexture(GR.GetTexture(GR.GetCardImage(ID), true), affinityType.GetAlternateColor(0.55f));
    }
}