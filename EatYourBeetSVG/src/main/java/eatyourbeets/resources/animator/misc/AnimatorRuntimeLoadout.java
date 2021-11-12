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

import static eatyourbeets.resources.GR.Enums.CardTags.EXPANDED;

public class AnimatorRuntimeLoadout
{
    private final static EYBCardTooltip PromotedTooltip = new EYBCardTooltip(GR.Animator.Strings.SeriesSelection.Promoted, GR.Animator.Strings.SeriesSelection.TooltipPromoted);
    private final static EYBCardTooltip BetaTooltip = new EYBCardTooltip(GR.Animator.Strings.SeriesSelection.Beta, GR.Animator.Strings.SeriesSelection.TooltipBeta);
    private final static EYBCardTooltip ExpansionTooltip = new EYBCardTooltip(GR.Animator.Strings.SeriesSelection.ExpansionHeader, GR.Animator.Strings.SeriesSelection.ExpansionCardBody);

    public final int ID;
    public final boolean IsBeta;
    public final AnimatorLoadout Loadout;
    public final Map<String, AbstractCard> BaseCards = new HashMap<>();
    public final Map<String, AbstractCard> ExpandedCards = new HashMap<>();

    public int bonus;
    public AnimatorCard card;
    public EYBCardAffinityStatistics AffinityStatistics;
    public boolean promoted;
    public boolean expansionEnabled;
    public boolean canEnableExpansion;

    public static AnimatorRuntimeLoadout TryCreate(AnimatorLoadout loadout)
    {
        // Starting series of level 5 of more are not selectable until unlocked
        if (loadout != null && loadout.UnlockLevel <= Math.max(5, GR.Animator.GetUnlockLevel()))
        {
            AnimatorRuntimeLoadout result = new AnimatorRuntimeLoadout(loadout);
            if (result.GetCardPoolInPlay().size() > 0 && result.Loadout.GetSymbolicCard() != null)
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
        this.canEnableExpansion = loadout.CanEnableExpansion();
        InitializeCards(loadout.Series);

        this.AffinityStatistics = new EYBCardAffinityStatistics(GetCardPoolInPlay().values(), false);

        this.promoted = false;
        this.card = null;
    }

    public CardGroup GetCardPool()
    {
        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : GetCardPoolInPlay().values())
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

    public void ToggleExpansion(boolean value) {
        if (!this.canEnableExpansion) {
            value = false;
            return;
        }
        this.expansionEnabled = value;
        this.AffinityStatistics = new EYBCardAffinityStatistics(GetCardPoolInPlay().values(), false);
        if (card != null) {
            card.upgraded = value;
            card.cardText.ForceRefresh();
            card.tooltips.add(ExpansionTooltip);
            if (promoted) {
                card.tooltips.add(PromotedTooltip);
            }
            else if (Loadout.IsBeta) {
                card.tooltips.add(BetaTooltip);
            }
        }
    }

    public Map<String, AbstractCard> GetCardPoolInPlay() {
        return expansionEnabled ? ExpandedCards : BaseCards;
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
        final AnimatorCardBuilder builder = new AnimatorCardBuilder(String.valueOf(Loadout.ID))
                .SetImagePath(temp.assetUrl)
                .SetNumbers(0,0,BaseCards.size(),ExpandedCards.size(),0)
                .CanUpgrade(false);

        if (promoted)
        {
            card = builder
            .SetText(Loadout.Name,
                    GR.Animator.Strings.SeriesSelection.ContainsNCards_Promoted("!M!"),
                    GR.Animator.Strings.SeriesSelection.ContainsNCards_Promoted("!S!"))

            .SetProperties(temp.type, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.NONE).Build();
            card.tooltips.add(PromotedTooltip);
        }
        else if (Loadout.IsBeta)
        {
            card = builder
            .SetText(Loadout.Name,
                    GR.Animator.Strings.SeriesSelection.ContainsNCards_Beta("!M!"),
                    GR.Animator.Strings.SeriesSelection.ContainsNCards_Beta("!S!"))
            .SetProperties(temp.type, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.NONE).Build();
            card.tooltips.add(BetaTooltip);
        }
        else
        {
            card = builder
            .SetText(Loadout.Name,
                    GR.Animator.Strings.SeriesSelection.ContainsNCards("!M!"),
                    GR.Animator.Strings.SeriesSelection.ContainsNCards("!S!"))
            .SetProperties(temp.type, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE).Build();
        }

        if (canEnableExpansion) {
            card.tags.add(EXPANDED);
            card.tooltips.add(ExpansionTooltip);
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

    private void InitializeCards(CardSeries series)
    {
        if (series != null && series != CardSeries.COLORLESS)
        {
            for (AbstractCard card : CardLibrary.getAllCards())
            {
                AnimatorCard c = JUtils.SafeCast(card, AnimatorCard.class);
                if (c != null && card.color == GR.Animator.CardColor && (series.equals(c.series) || series.equals(CardSeries.ANY))
                    && card.rarity != AbstractCard.CardRarity.SPECIAL
                    && card.rarity != AbstractCard.CardRarity.BASIC)
                {
                    ExpandedCards.put(c.cardID, c);
                    if (!c.cardData.IsExpansionCard) {
                        BaseCards.put(c.cardID, c);
                    }
                }
            }
        }
    }
}
