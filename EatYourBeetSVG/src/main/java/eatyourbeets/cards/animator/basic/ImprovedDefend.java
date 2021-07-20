package eatyourbeets.cards.animator.basic;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

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

        if (affinityType == AffinityType.Star)
        {
            Initialize(0, 4, 3);
        }
        else
        {
            Initialize(0, 5, 2);
        }
        SetUpgrade(0, 3);

        this.tags.add(CardTags.STARTER_DEFEND);
        this.tags.add(GR.Enums.CardTags.IMPROVED_DEFEND);
    }

    @Override
    protected Texture GetPortraitForeground()
    {
        return GR.GetTexture(GR.GetCardImage(Defend.DATA.ID + "Alt2"), true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);

        if (upgraded)
        {
            SecondaryEffect();
        }
    }
}