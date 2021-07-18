package eatyourbeets.cards.animator.basic;

import com.badlogic.gdx.graphics.Texture;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public abstract class ImprovedDefend extends ImprovedBasicCard
{
    public static final ArrayList<EYBCardData> list = new ArrayList<>();

    public static ArrayList<EYBCardData> GetCards()
    {
        if (list.isEmpty())
        {
            list.add(Defend_Red.DATA);
            list.add(Defend_Green.DATA);
            list.add(Defend_Blue.DATA);
            list.add(Defend_Light.DATA);
            list.add(Defend_Dark.DATA);
            list.add(Defend_Star.DATA);
        }

        return list;
    }

    protected static EYBCardData Register(Class<? extends AnimatorCard> type)
    {
        return AnimatorCard.Register(type)
                .SetColor(CardColor.COLORLESS)
                .SetSkill(1, CardRarity.BASIC, EYBCardTarget.None)
                .SetImagePath(GR.GetCardImage(Defend.DATA.ID + "Alt1"));
    }

    public ImprovedDefend(EYBCardData data, AffinityType type)
    {
        super(data, type);

        Initialize(0,  type == AffinityType.Star ? 4 : 5);
        SetUpgrade(0, 3);

        this.tags.add(CardTags.STARTER_DEFEND);
        this.tags.add(GR.Enums.CardTags.IMPROVED_DEFEND);
    }

    protected void RetainPower()
    {
        if (affinityType == AffinityType.Star)
        {
            JUtils.FindMax(CombatStats.Affinities.Powers, p -> p.amount).RetainOnce();
        }
        else
        {
            CombatStats.Affinities.GetPower(affinityType).RetainOnce();
        }
    }

    @Override
    protected Texture GetPortraitForeground()
    {
        return GR.GetTexture(GR.GetCardImage(Defend.DATA.ID + "Alt2"), true);
    }
}