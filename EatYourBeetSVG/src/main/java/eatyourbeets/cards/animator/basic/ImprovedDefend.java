package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
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
            list.add(Defend_Orange.DATA);
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

    public ImprovedDefend(EYBCardData data, Affinity affinity)
    {
        super(data, affinity, GR.GetCardImage(Defend.DATA.ID + "Alt2"));

        if (affinity == Affinity.Star)
        {
            Initialize(0, 5, 3);
            SetUpgrade(0, 3);
        }
        else
        {
            Initialize(0, 6, 2);
            SetUpgrade(0, 2);
        }

        SetTag(CardTags.STARTER_DEFEND, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
    }
}