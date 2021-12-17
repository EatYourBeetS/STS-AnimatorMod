package pinacolada.resources.pcl.misc;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import pinacolada.cards.base.*;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.HashMap;
import java.util.Map;

import static pinacolada.resources.GR.Enums.CardTags.EXPANDED;

public class PCLRuntimeLoadout
{
    private final static PCLCardTooltip BetaTooltip = new PCLCardTooltip(GR.PCL.Strings.SeriesSelection.Beta, GR.PCL.Strings.SeriesSelection.TooltipBeta);
    private final static PCLCardTooltip ExpansionTooltip = new PCLCardTooltip(GR.PCL.Strings.SeriesSelection.ExpansionHeader, GR.PCL.Strings.SeriesSelection.ExpansionCardBody);

    public final int ID;
    public final boolean IsBeta;
    public final PCLLoadout Loadout;
    public final PCLTrophies Trophies;
    public final Map<String, AbstractCard> BaseCards = new HashMap<>();
    public final Map<String, AbstractCard> ExpandedCards = new HashMap<>();
    public final PCLCardTooltip TrophyTooltip;
    public final PCLCardTooltip UnlockTooltip;

    public int bonus;
    public PCLCard card;
    public PCLCardAffinityStatistics AffinityStatistics;
    public boolean expansionEnabled;
    public boolean canEnableExpansion;
    public boolean isLocked;

    public static PCLRuntimeLoadout TryCreate(PCLLoadout loadout)
    {
        PCLRuntimeLoadout result = new PCLRuntimeLoadout(loadout);
        if (result.GetCardPoolInPlay().size() > 0 && result.Loadout.GetSymbolicCard() != null)
        {
            return result;
        }

        return null;
    }

    public PCLRuntimeLoadout(PCLLoadout loadout)
    {
        this.ID = loadout.ID;
        this.IsBeta = loadout.IsBeta;
        this.Loadout = loadout;
        this.canEnableExpansion = loadout.CanEnableExpansion();
        this.Trophies = Loadout.GetTrophies();

        this.expansionEnabled = GR.PCL.Config.ExpandedSeries.Get().contains(loadout.Series);
        this.TrophyTooltip = new PCLCardTooltip(GR.PCL.Strings.Trophies.Bronze, Trophies != null && Trophies.Trophy1 >= 0 ? GR.PCL.Strings.Trophies.BronzeFormatted(Trophies.Trophy1) : GR.PCL.Strings.Trophies.BronzeLocked);
        this.UnlockTooltip = new PCLCardTooltip(GR.PCL.Strings.CharSelect.RightText, GR.PCL.Strings.CharSelect.UnlocksAtLevel(loadout.UnlockLevel, GR.PCL.GetUnlockLevel()));
        InitializeCards(loadout.Series);

        this.AffinityStatistics = new PCLCardAffinityStatistics(GetCardPoolInPlay().values(), false);

        this.card = null;
        this.isLocked = GR.PCL.GetUnlockLevel() < Loadout.UnlockLevel;
    }

    public CardGroup GetCardPool()
    {
        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : GetCardPoolInPlay().values())
        {
            final CardGroup pool = PCLGameUtilities.GetCardPool(c.rarity);
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

    public void ToggleExpansion(boolean value) {
        if (!this.canEnableExpansion) {
            value = false;
            return;
        }
        this.expansionEnabled = value;
        this.AffinityStatistics = new PCLCardAffinityStatistics(GetCardPoolInPlay().values(), false);
        if (card != null) {
            card.upgraded = value;
            card.cardText.ForceRefresh();
            card.tooltips.add(ExpansionTooltip);
            if (Loadout.IsBeta) {
                card.tooltips.add(BetaTooltip);
            }
        }
    }

    public Map<String, AbstractCard> GetCardPoolInPlay() {
        return canEnableExpansion && expansionEnabled ? ExpandedCards : BaseCards;
    }

    public AbstractCard BuildCard()
    {
        final PCLCardData data = Loadout.GetSymbolicCard();
        if (data == null)
        {
            PCLJUtils.LogInfo(this, Loadout.Name + " has no symbolic card.");
            return null;
        }

        final AbstractCard temp = data.CreateNewInstance();
        final PCLCardBuilder builder = new PCLCardBuilder(String.valueOf(Loadout.ID))
                .SetImagePath(temp.assetUrl)
                .SetNumbers(0,0,BaseCards.size(),ExpandedCards.size(),0)
                .CanUpgrade(false);

        if (isLocked) {
            card = builder
                    .SetText(Loadout.Name, UnlockTooltip.description, null)
                    .SetProperties(AbstractCard.CardType.CURSE, AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.NONE).Build();
            card.tooltips.add(UnlockTooltip);
        }
        else if (Loadout.IsBeta)
        {
            card = builder
            .SetText(Loadout.Name,
                    GR.PCL.Strings.SeriesSelection.ContainsNCards_Beta("!M!"),
                    GR.PCL.Strings.SeriesSelection.ContainsNCards_Beta("!S!"))
            .SetProperties(temp.type, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.NONE).Build();
            card.tooltips.add(BetaTooltip);
        }
        else
        {
            String trophyString = (Trophies.Trophy1 > -1 ? Trophies.Trophy1 : "--") + "/" + PCLTrophies.MAXIMUM_TROPHY;
            card = builder
            .SetText(Loadout.Name,
                    GR.PCL.Strings.SeriesSelection.ContainsNCards_Full("!M!", trophyString),
                    GR.PCL.Strings.SeriesSelection.ContainsNCards_Full("!S!", trophyString))
            .SetProperties(temp.type, AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.NONE).Build();
            card.tooltips.add(TrophyTooltip);
        }

        if (canEnableExpansion) {
            card.tags.add(EXPANDED);
            card.tooltips.add(ExpansionTooltip);
        }

        int i = 0;
        int maxLevel = 2;
        float maxPercentage = 0;
        for (PCLCardAffinityStatistics.Group g : AffinityStatistics)
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
                PCLCard c = PCLJUtils.SafeCast(card, PCLCard.class);
                if (c != null && card.color == GR.PCL.CardColor && (series.equals(c.series) || series.equals(CardSeries.ANY))
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
