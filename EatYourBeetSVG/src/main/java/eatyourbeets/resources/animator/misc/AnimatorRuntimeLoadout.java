package eatyourbeets.resources.animator.misc;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.cards.base.*;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.HashMap;
import java.util.Map;

public class AnimatorRuntimeLoadout
{
    private final static EYBCardTooltip PromotedTooltip = new EYBCardTooltip(GR.Animator.Strings.SeriesSelection.PickupBonusHeader, GR.Animator.Strings.SeriesSelection.PickupBonusBody);

    public final int ID;
    public final boolean IsBeta;
    public final AnimatorLoadout Loadout;
    public final Map<String, AbstractCard> Cards;
    public final EYBCardAffinityStatistics AffinityStatistics;

    public int bonus;
    public EYBCard card;
    public boolean promoted;

    public static AnimatorRuntimeLoadout TryCreate(AnimatorLoadout loadout)
    {
        // Starting series of level 5 of more are not selectable until unlocked
        if (loadout != null && loadout.UnlockLevel <= Math.max(5, GR.Animator.GetUnlockLevel()))
        {
            AnimatorRuntimeLoadout result = new AnimatorRuntimeLoadout(loadout);
            if (result.Cards.size() > 0 && result.Loadout.GetSymbolicCard() != null)
            {
                return result;
            }
        }

        return null;
    }

    public AnimatorRuntimeLoadout(AnimatorLoadout loadout)
    {
        this.ID = loadout.ID;
        this.IsBeta = loadout.IsBeta;
        this.Loadout = loadout;
        this.Cards = GetNonColorlessCards(loadout.Series);
        this.AffinityStatistics = new EYBCardAffinityStatistics(Cards.values(), false);

        this.promoted = false;
        this.card = null;
    }

    public CardGroup GetCardPool()
    {
        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : Cards.values())
        {
            final CardGroup pool = GameUtilities.GetCardPool(c.rarity);
            if (pool != null)
            {
                c = pool.findCardById(c.cardID);

                if (c != null)
                {
                    final AbstractCard copy = c.makeCopy();
                    copy.isSeen = c.isSeen;
                    group.group.add(copy);
                }
            }
        }

        group.sortByRarity(true);

        return group;
    }

    public void Promote()
    {
        if (Settings.isDebug)
        {
            JUtils.LogInfo(this, "Debug mode: No mandatory series, but no trophies will be unlocked.");
            return;
        }

        if (card != null)
        {
            throw new RuntimeException("Can not promote a card that has already been built.");
        }

        this.promoted = true;
    }

    public AbstractCard BuildCard()
    {
        final EYBCardData data = Loadout.GetSymbolicCard();
        if (data == null)
        {
            JUtils.LogInfo(this, Loadout.Name + " has no symbolic card.");
            return null;
        }

        final AbstractCard temp = data.CreateNewInstance();
        final DynamicCardBuilder builder = new AnimatorCardBuilder(String.valueOf(Loadout.ID)).SetImagePath(temp.assetUrl).CanUpgrade(false);

        if (promoted)
        {
            card = builder
            .SetText(Loadout.Name, GR.Animator.Strings.SeriesSelection.ContainsNCards_Promoted(Cards.size()), null)
            .SetProperties(temp.type, GR.Animator.CardColor, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.NONE).Build();
            card.tooltips.add(PromotedTooltip);
        }
        else if (Loadout.IsBeta)
        {
            card = builder
            .SetText(Loadout.Name, GR.Animator.Strings.SeriesSelection.ContainsNCards_Beta(Cards.size()), null)
            .SetProperties(temp.type, GR.Animator.CardColor, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.NONE).Build();
        }
        else
        {
            card = builder
            .SetText(Loadout.Name, GR.Animator.Strings.SeriesSelection.ContainsNCards(Cards.size()), null)
            .SetProperties(temp.type, GR.Animator.CardColor, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE).Build();
        }

        int i = 0;
        int maxLevel = 2;
        float maxPercentage = 0;
        for (EYBCardAffinityStatistics.Group g : AffinityStatistics)
        {
            float percentage = g.GetPercentage(0);
            if (percentage == 0 || i > 2)
            {
                break;
            }

            if (percentage < maxPercentage || (maxLevel == 2 && percentage < 0.3f))
            {
                maxLevel -= 1;
            }
            if (maxLevel > 0)
            {
                card.affinities.Add(g.Affinity, maxLevel);
            }

            maxPercentage = percentage;
            i += 1;
        }

        return card;
    }

    private Map<String, AbstractCard> GetNonColorlessCards(CardSeries series)
    {
        Map<String, AbstractCard> cards = new HashMap<>();

        if (series != null && series != CardSeries.ANY)
        {
            for (AbstractCard card : CardLibrary.getAllCards())
            {
                AnimatorCard c = JUtils.SafeCast(card, AnimatorCard.class);
                if (c != null && card.color == GR.Animator.CardColor && series.equals(c.series)
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
