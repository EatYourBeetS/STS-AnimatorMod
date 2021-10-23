package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public abstract class ImprovedStrike extends ImprovedBasicCard
{
    public static final ArrayList<EYBCardData> list = new ArrayList<>();

    public static ArrayList<EYBCardData> GetCards()
    {
        if (list.isEmpty())
        {
            list.add(Strike_Fire_Thunder.DATA);
            list.add(Strike_Air_Steel.DATA);
            list.add(Strike_Light_Water.DATA);
            list.add(Strike_Dark_Poison.DATA);
            list.add(Strike_Earth_Nature.DATA);
            list.add(Strike_Mind_Cyber.DATA);
            list.add(Strike_Star.DATA);
        }

        return list;
    }

    protected static EYBCardData Register(Class<? extends AnimatorCard> type)
    {
        return AnimatorCard.Register(type).SetColor(CardColor.COLORLESS).SetAttack(1, CardRarity.BASIC)
                .SetImagePath(GR.GetCardImage(Strike.DATA.ID + "Alt1"));
    }

    protected static EYBCardData RegisterSpecial(Class<? extends AnimatorCard> type)
    {
        return AnimatorCard.Register(type).SetColor(CardColor.COLORLESS).SetAttack(1, CardRarity.SPECIAL)
                .SetImagePath(GR.GetCardImage(Strike.DATA.ID + "Alt1"));
    }

    public ImprovedStrike(EYBCardData data, Affinity affinity1, Affinity affinity2)
    {
        super(data, affinity1, affinity2, GR.GetCardImage(Strike.DATA.ID + "Alt2"));

        Initialize(7, 0);
        SetUpgrade(3, 0);

        SetTag(CardTags.STARTER_STRIKE, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
    }
}