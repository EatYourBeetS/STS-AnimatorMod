package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
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
            list.add(Strike_Red.DATA);
            list.add(Strike_Green.DATA);
            list.add(Strike_Blue.DATA);
            list.add(Strike_Light.DATA);
            list.add(Strike_Dark.DATA);
            list.add(Strike_Star.DATA);
        }

        return list;
    }

    protected static EYBCardData Register(Class<? extends AnimatorCard> type)
    {
        return AnimatorCard.Register(type).SetColor(CardColor.COLORLESS).SetAttack(1, CardRarity.BASIC)
                .SetImagePath(GR.GetCardImage(Strike.DATA.ID + "Alt1"));
    }

    public ImprovedStrike(EYBCardData data, AffinityType type)
    {
        super(data, type, GR.GetCardImage(Strike.DATA.ID + "Alt2"));

        if (affinityType == AffinityType.Star)
        {
            Initialize(6, 0, 3);
        }
        else
        {
            Initialize(7, 0, 2);
        }
        SetUpgrade(2, 0);

        SetAffinityRequirement(affinityType, magicNumber);

        SetTag(CardTags.STARTER_STRIKE, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        if (upgraded)
        {
            SecondaryEffect();
        }
    }
}