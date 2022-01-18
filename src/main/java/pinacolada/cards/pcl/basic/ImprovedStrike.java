package pinacolada.cards.pcl.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;

public abstract class ImprovedStrike extends ImprovedBasicCard
{
    public static final ArrayList<PCLCardData> list = new ArrayList<>();

    public static ArrayList<PCLCardData> GetCards()
    {
        if (list.isEmpty())
        {
            list.add(Strike_Red.DATA);
            list.add(Strike_Green.DATA);
            list.add(Strike_Blue.DATA);
            list.add(Strike_Orange.DATA);
            list.add(Strike_Light.DATA);
            list.add(Strike_Dark.DATA);
            list.add(Strike_Silver.DATA);
        }

        return list;
    }

    protected static PCLCardData Register(Class<? extends PCLCard> type)
    {
        return PCLCard.Register(type).SetColor(CardColor.COLORLESS).SetAttack(1, CardRarity.BASIC)
                .SetImagePath(GR.GetCardImage(Strike.DATA.ID + "Alt1"));
    }

    public ImprovedStrike(PCLCardData data, PCLAffinity affinity)
    {
        super(data, affinity, GR.GetCardImage(Strike.DATA.ID + "Alt2"));

        if (affinity == PCLAffinity.Star)
        {
            Initialize(6, 0, 3);
            SetUpgrade(3, 0);
        }
        else
        {
            Initialize(7, 0, 2);
            SetUpgrade(3, 0);
        }

        SetTag(CardTags.STARTER_STRIKE, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL);
    }
}