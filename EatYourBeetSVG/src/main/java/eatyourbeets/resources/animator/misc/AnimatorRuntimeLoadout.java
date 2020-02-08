package eatyourbeets.resources.animator.misc;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.cards.base.*;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JavaUtilities;

import java.util.HashMap;
import java.util.Map;

public class AnimatorRuntimeLoadout
{
    private final static EYBCardTooltip PromotedTooltip = new EYBCardTooltip(GR.Animator.Strings.SeriesSelection.PickupBonusHeader, GR.Animator.Strings.SeriesSelection.PickupBonusBody);

    public final int ID;
    public final Map<String, AbstractCard> Cards;
    public final boolean IsBeta;
    public final AnimatorLoadout Loadout;

    public int bonus;
    public AnimatorCard card;
    public boolean promoted;

    public static AnimatorRuntimeLoadout TryCreate(AnimatorLoadout loadout)
    {
        if (loadout != null)
        {
            AnimatorRuntimeLoadout result = new AnimatorRuntimeLoadout(loadout);
            if (result.Cards.size() > 0)
            {
                return result;
            }
        }

        return null;
    }

    private AnimatorRuntimeLoadout(AnimatorLoadout loadout)
    {
        this.ID = loadout.ID;
        this.IsBeta = loadout.IsBeta;
        this.Loadout = loadout;
        this.Cards = GetNonColorlessCards(loadout.Synergy);

        this.promoted = false;
        this.card = null;
    }

    public void Promote()
    {
        if (card != null)
        {
            throw new RuntimeException("Can not promote a card that has already been built.");
        }

        this.promoted = true;
    }

    public AbstractCard BuildCard()
    {
        AbstractCard temp = CardLibrary.getCard(Loadout.GetSymbolicCardID());
        if (temp == null)
        {
            JavaUtilities.Log(this, "Loadout.GetSymbolicCardID() failed, " + Loadout.Name);
            return null;
        }

        AnimatorCardBuilder builder = new AnimatorCardBuilder(String.valueOf(Loadout.ID)).SetImage(temp.assetUrl);

        if (promoted)
        {
            card = builder
            .SetText(Loadout.Name, GR.Animator.Strings.SeriesSelection.ContainsNCards_Promoted(Cards.size()), "")
            .SetProperties(temp.type, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.NONE).Build();
            card.tooltips.add(PromotedTooltip);
        }
        else if (Loadout.IsBeta)
        {
            card = builder
            .SetText(Loadout.Name, GR.Animator.Strings.SeriesSelection.ContainsNCards_Beta(Cards.size()), "")
            .SetProperties(temp.type, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.NONE).Build();
        }
        else
        {
            card = builder
            .SetText(Loadout.Name, GR.Animator.Strings.SeriesSelection.ContainsNCards(Cards.size()), "")
            .SetProperties(temp.type, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE).Build();
        }

        return card;
    }

    private Map<String, AbstractCard> GetNonColorlessCards(Synergy synergy)
    {
        Map<String, AbstractCard> cards = new HashMap<>();

        if (synergy != null && synergy != Synergies.ANY)
        {
            for (AbstractCard card : CardLibrary.getAllCards())
            {
                AnimatorCard c = JavaUtilities.SafeCast(card, AnimatorCard.class);
                if (c != null && card.color == GR.Animator.CardColor && synergy.equals(c.synergy)
                    && card.rarity != AbstractCard.CardRarity.SPECIAL
                    && card.rarity != AbstractCard.CardRarity.BASIC)
                {
                    cards.put(c.cardID, c);
                }
            }
        }

        return cards;
    }
}
