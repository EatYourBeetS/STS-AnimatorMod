package eatyourbeets.cards.animator.basic;

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
        return AnimatorCard.Register(type).SetColor(CardColor.COLORLESS).SetSkill(1, CardRarity.BASIC, EYBCardTarget.None)
                .SetImagePath(GR.GetCardImage(Defend.DATA.ID + "Alt1"));
    }

    public ImprovedDefend(EYBCardData data, AffinityType type)
    {
        super(data, type, GR.GetCardImage(Defend.DATA.ID + "Alt2"));

        if (affinityType == AffinityType.Star)
        {
            Initialize(0, 4, 3);
        }
        else
        {
            Initialize(0, 5, 2);
        }
        SetUpgrade(0, 3);

        SetTag(CardTags.STARTER_DEFEND, true);
        SetTag(GR.Enums.CardTags.IMPROVED_DEFEND, true);
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