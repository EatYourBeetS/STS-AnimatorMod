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
            list.add(Defend_Star.DATA);
            list.add(Defend_Fire_Steel.DATA);
            list.add(Defend_Air_Water.DATA);
            list.add(Defend_Light_Thunder.DATA);
            list.add(Defend_Earth_Poison.DATA);
            list.add(Defend_Dark_Cyber.DATA);
            list.add(Defend_Mind_Nature.DATA);
        }

        return list;
    }

    protected static EYBCardData Register(Class<? extends AnimatorCard> type)
    {
        return AnimatorCard.Register(type).SetColor(CardColor.COLORLESS).SetSkill(1, CardRarity.BASIC, EYBCardTarget.None)
                .SetImagePath(GR.GetCardImage(Defend.DATA.ID + "Alt1"));
    }

    protected static EYBCardData RegisterSpecial(Class<? extends AnimatorCard> type)
    {
        return AnimatorCard.Register(type).SetColor(CardColor.COLORLESS).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None)
                .SetImagePath(GR.GetCardImage(Defend.DATA.ID + "Alt1"));
    }

    public ImprovedDefend(EYBCardData data, Affinity affinity1, Affinity affinity2)
    {
        super(data, affinity1, affinity2, GR.GetCardImage(Defend.DATA.ID + "Alt2"));

        Initialize(0, 7);
        SetUpgrade(0, 3);

        SetTag(CardTags.STARTER_DEFEND, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
    }
}