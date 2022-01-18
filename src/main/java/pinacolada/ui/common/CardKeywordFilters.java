package pinacolada.ui.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.Mathf;
import org.apache.commons.lang3.StringUtils;
import pinacolada.cards.base.*;
import pinacolada.resources.GR;
import pinacolada.ui.controls.GUI_Button;
import pinacolada.ui.controls.GUI_Dropdown;
import pinacolada.ui.controls.GUI_Label;
import pinacolada.ui.controls.GUI_VerticalScrollBar;
import pinacolada.ui.hitboxes.AdvancedHitbox;
import pinacolada.ui.hitboxes.DraggableHitbox;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static pinacolada.ui.common.AffinityKeywordButton.ICON_SIZE;

public class CardKeywordFilters extends GUIElement
{
    public enum CostFilter
    {
        CostX("X", -1, -1),
        Cost0("0", 0, 0),
        Cost1("1", 1, 1),
        Cost2("2",2,2),
        Cost3Plus("3+",3,9999),
        Unplayable("Unplayable",-9999,-2);

        public final int lowerBound;
        public final int upperBound;
        public final String name;

        CostFilter(String name, int lowerBound, int upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
            this.name = name;
        }
    }

    public enum OriginFilter
    {
        BaseGame("Base Game", GR.PackageNames.OG),
        Animator("Animator",GR.PackageNames.EYB),
        PCL("Fool",GR.PackageNames.PCL);

        public final String name;
        public final String packagePrefix;

        OriginFilter(String name, String packagePrefix) {
            this.name = name;
            this.packagePrefix = packagePrefix;
        }
    }

    private static final Color FADE_COLOR = new Color(0f, 0f, 0f, 0.84f);
    protected static final float SPACING = Settings.scale * 22.5f;
    protected static final float DRAW_START_X = (float) Settings.WIDTH * 0.15f;
    protected static final float DRAW_START_Y = (float) Settings.HEIGHT * 0.78f;
    protected static final float PAD_X = AbstractCard.IMG_WIDTH * 0.75f + Settings.CARD_VIEW_PAD_X;
    protected static final float PAD_Y = Scale(10);
    protected static final float SCROLL_BAR_THRESHOLD = 500f * Settings.scale;

    public static final int ROW_SIZE = 8;
    public static final HashSet<AbstractCard.CardColor> CurrentColors = new HashSet<>();
    public static final HashSet<OriginFilter> CurrentOrigins = new HashSet<>();
    public static final HashSet<CardSeries> CurrentSeries = new HashSet<>();
    public static final HashSet<PCLCardTooltip> CurrentFilters = new HashSet<>();
    public static final HashSet<CostFilter> CurrentCosts = new HashSet<>();
    public static final HashSet<AbstractCard.CardRarity> CurrentRarities = new HashSet<>();
    public static final HashSet<AbstractCard.CardType> CurrentTypes = new HashSet<>();
    public static PCLCardAffinities CurrentAffinities = new PCLCardAffinities(null);
    protected final HashMap<PCLCardTooltip,Integer> CurrentFilterCounts = new HashMap<>();
    protected final ArrayList<CardKeywordButton> FilterButtons = new ArrayList<>();
    protected final ArrayList<AffinityKeywordButton> AffinityButtons = new ArrayList<>();
    protected final ArrayList<GUI_Button> ScalingButtons = new ArrayList<>();
    protected int currentTotal;
    protected ArrayList<AbstractCard> referenceCards;
    protected ActionT1<CardKeywordButton> onClick;

    protected boolean canDragScreen = false; //TODO allow toggle
    protected boolean invalidated;
    protected boolean isColorless;
    protected float draw_x;
    protected float lowerScrollBound = -Settings.DEFAULT_SCROLL_LIMIT;
    protected float upperScrollBound = Settings.DEFAULT_SCROLL_LIMIT;
    protected float scrollStart;
    protected float scrollDelta;
    protected int filterSizeCache;
    protected final GUI_Dropdown<CardSeries> SeriesDropdown;
    protected final GUI_Dropdown<OriginFilter> OriginsDropdown;
    protected final GUI_Dropdown<CostFilter> CostDropdown;
    protected final GUI_Dropdown<AbstractCard.CardRarity> RaritiesDropdown;
    protected final GUI_Dropdown<AbstractCard.CardType> TypesDropdown;
    protected final GUI_Dropdown<AbstractCard.CardColor> ColorsDropdown;
    protected final GUI_Button closeButton;
    protected final GUI_Button clearButton;
    public final GUI_VerticalScrollBar scrollBar;
    public final GUI_Label affinitiesSectionLabel;
    public final GUI_Label currentTotalHeaderLabel;
    public final GUI_Label currentTotalLabel;
    public final GUI_Label keywordsSectionLabel;
    public final AdvancedHitbox hb;
    public boolean draggingScreen;
    public boolean autoShowScrollbar;

    protected boolean isAccessedFromCardPool;
    private boolean shouldSortByCount;

    public static ArrayList<PCLCardTooltip> GetAllTooltips(PCLCard eC) {
        ArrayList<PCLCardTooltip> dynamicTooltips = new ArrayList<>();
        if (eC != null) {
            eC.GenerateDynamicTooltips(dynamicTooltips);
            for (PCLCardTooltip tip : eC.tooltips)
            {
                if (tip.canRender && !dynamicTooltips.contains(tip))
                {
                    dynamicTooltips.add(tip);
                }
            }
        }
        return dynamicTooltips;
    }

    public static ArrayList<AbstractCard> ApplyFilters(ArrayList<AbstractCard> input) {
        return PCLJUtils.Filter(input, c -> {
                    //Colors check
                    if (!CurrentColors.isEmpty()) {
                        boolean passes = false;
                        for (AbstractCard.CardColor co : CurrentColors) {
                            if (co == c.color) {
                                passes = true;
                                break;
                            }
                        }
                        if (!passes) {
                            return false;
                        }
                    }

                    //Origin check
                    if (!CurrentOrigins.isEmpty()) {
                        boolean passes = false;
                        for (OriginFilter of : CurrentOrigins) {
                            if (c.getClass().getPackage().getName().startsWith(of.packagePrefix)) {
                                passes = true;
                                break;
                            }
                        }
                        if (!passes) {
                            return false;
                        }
                    }

                    //Series check
                    if (!CurrentSeries.isEmpty()) {
                        PCLCard aC = PCLJUtils.SafeCast(c, PCLCard.class);
                        if (aC == null || aC.series == null || !CurrentSeries.contains(aC.series)) {
                            return false;
                        }
                    }

                    //Tooltips check
                    PCLCard eC = PCLJUtils.SafeCast(c, PCLCard.class);
                    if (!CurrentFilters.isEmpty() && (eC == null || !GetAllTooltips(eC).containsAll(CurrentFilters))) {
                        return false;
                    }

                    //Rarities check
                    if (!CurrentRarities.isEmpty() && !CurrentRarities.contains(c.rarity)) {
                        return false;
                    }

                    //Types check
                    if (!CurrentTypes.isEmpty() && !CurrentTypes.contains(c.type)) {
                        return false;
                    }

                    //Affinity check
                    for (PCLCardAffinity cAffinity : CurrentAffinities.List) {
                        if (PCLGameUtilities.GetPCLAffinityLevel(c, cAffinity.type, true) < cAffinity.level) {
                            return false;
                        }
                    }
                    if (PCLGameUtilities.GetPCLAffinityLevel(c, PCLAffinity.Star, true) < CurrentAffinities.GetLevel(PCLAffinity.Star)) {
                        return false;
                    }

                    //Cost check
                    if (!CurrentCosts.isEmpty()) {
                        boolean passes = false;
                        for (CostFilter cf : CurrentCosts) {
                            if (c.cost >= cf.lowerBound && c.cost <= cf.upperBound) {
                                passes = true;
                                break;
                            }
                        }
                        return passes;
                    }

                    return true;
        });
    }

    public static boolean AreFiltersEmpty() {
        return CurrentColors.isEmpty() && CurrentOrigins.isEmpty() && CurrentFilters.isEmpty() && CurrentSeries.isEmpty() && CurrentCosts.isEmpty() && CurrentRarities.isEmpty() && CurrentTypes.isEmpty() && CurrentAffinities.IsEmpty();
    }

    public CardKeywordFilters()
    {
        isActive = false;
        hb = new AdvancedHitbox(DRAW_START_X, DRAW_START_Y, Scale(180), Scale(70)).SetIsPopupCompatible(true);
        closeButton = new GUI_Button(GR.PCL.Images.HexagonalButton.Texture(), new DraggableHitbox(0, 0, Settings.WIDTH * 0.07f, Settings.HEIGHT * 0.07f, false).SetIsPopupCompatible(true))
                .SetBorder(GR.PCL.Images.HexagonalButtonBorder.Texture(), Color.WHITE)
                .SetPosition(Settings.WIDTH * 0.96f, Settings.HEIGHT * 0.95f).SetText(CombatRewardScreen.TEXT[6])
                .SetOnClick(this::Close)
                .SetColor(Color.GRAY);
        clearButton = new GUI_Button(GR.PCL.Images.HexagonalButton.Texture(), new DraggableHitbox(0, 0, Settings.WIDTH * 0.07f, Settings.HEIGHT * 0.07f).SetIsPopupCompatible(true))
                .SetBorder(GR.PCL.Images.HexagonalButtonBorder.Texture(), Color.WHITE)
                .SetColor(Color.FIREBRICK)
                .SetPosition(Settings.WIDTH * 0.96f, Settings.HEIGHT * 0.86f).SetText("Clear")
                .SetOnClick(() -> this.Clear(true, isAccessedFromCardPool));
        this.scrollBar = new GUI_VerticalScrollBar(new Hitbox(ScreenW(0.03f), ScreenH(0.7f)))
                .SetOnScroll(this::OnScroll);

        SeriesDropdown = new GUI_Dropdown<CardSeries>(new AdvancedHitbox(hb.x - SPACING * 3, hb.y + SPACING * 3, Scale(240), Scale(48)), cs -> cs.LocalizedName)
                .SetOnOpenOrClose(isOpen -> {
                    CardCrawlGame.isPopupOpen = this.isActive;
                })
                .SetOnChange(selectedSeries -> {
                    CurrentSeries.clear();
                    CurrentSeries.addAll(selectedSeries);
                    if (onClick != null) {
                        onClick.Invoke(null);
                    }
                })
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, GR.PCL.Strings.SeriesUI.SeriesUI)
                .SetIsMultiSelect(true)
                .SetCanAutosizeButton(true);

        OriginsDropdown = new GUI_Dropdown<OriginFilter>(new AdvancedHitbox(SeriesDropdown.hb.x, SeriesDropdown.hb.y, Scale(240), Scale(48)), c -> c.name)
                .SetOnOpenOrClose(isOpen -> {
                    CardCrawlGame.isPopupOpen = this.isActive;
                })
                .SetOnChange(costs -> {
                    CurrentOrigins.clear();
                    CurrentOrigins.addAll(costs);
                    if (onClick != null) {
                        onClick.Invoke(null);
                    }
                })
                .SetLabelFunctionForButton(items -> {
                    if (items.size() == 0) {
                        return GR.PCL.Strings.Misc.Any;
                    }
                    if (items.size() > 1) {
                        return items.size() + " " + GR.PCL.Strings.SeriesUI.ItemsSelected;
                    }
                    return StringUtils.join(PCLJUtils.Map(items, item -> item.name), ", ");
                }, null,false)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, GR.PCL.Strings.SeriesUI.Origins)
                .SetIsMultiSelect(true)
                .SetCanAutosizeButton(true)
                .SetItems(OriginFilter.values());

        CostDropdown = new GUI_Dropdown<CostFilter>(new AdvancedHitbox(hb.x + SeriesDropdown.hb.width + SPACING * 3, hb.y + SPACING * 3, Scale(240), Scale(48)), c -> c.name)
                .SetOnOpenOrClose(isOpen -> {
                    CardCrawlGame.isPopupOpen = this.isActive;
                })
                .SetOnChange(costs -> {
                    CurrentCosts.clear();
                    CurrentCosts.addAll(costs);
                    if (onClick != null) {
                        onClick.Invoke(null);
                    }
                })
                .SetLabelFunctionForButton(items -> {
                    if (items.size() == 0) {
                        return GR.PCL.Strings.Misc.Any;
                    }
                    if (items.size() > 1) {
                        return items.size() + " " + GR.PCL.Strings.SeriesUI.ItemsSelected;
                    }
                    return StringUtils.join(PCLJUtils.Map(items, item -> item.name), ", ");
                }, null,false)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, GR.PCL.Strings.SeriesUI.Costs)
                .SetIsMultiSelect(true)
                .SetCanAutosizeButton(true)
                .SetItems(CostFilter.values());

        RaritiesDropdown = new GUI_Dropdown<AbstractCard.CardRarity>(new AdvancedHitbox(hb.x + CostDropdown.hb.width + SPACING * 3, hb.y + SPACING * 3, Scale(240), Scale(48))
                ,item -> StringUtils.capitalize(item.toString().toLowerCase()))
                .SetOnOpenOrClose(isOpen -> {
                    CardCrawlGame.isPopupOpen = this.isActive;
                })
                .SetOnChange(costs -> {
                    CurrentRarities.clear();
                    CurrentRarities.addAll(costs);
                    if (onClick != null) {
                        onClick.Invoke(null);
                    }
                })
                .SetLabelFunctionForButton(items -> {
                    if (items.size() == 0) {
                        return GR.PCL.Strings.Misc.Any;
                    }
                    if (items.size() > 1) {
                        return items.size() + " " + GR.PCL.Strings.SeriesUI.ItemsSelected;
                    }
                    return StringUtils.join(PCLJUtils.Map(items, item -> StringUtils.capitalize(item.toString().toLowerCase())), ", ");
                }, null,false)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, GR.PCL.Strings.SeriesUI.Rarities)
                .SetIsMultiSelect(true)
                .SetCanAutosizeButton(true)
                .SetItems(AbstractCard.CardRarity.values());

        TypesDropdown = new GUI_Dropdown<AbstractCard.CardType>(new AdvancedHitbox(hb.x + RaritiesDropdown.hb.width + SPACING * 3, hb.y + SPACING * 3, Scale(240), Scale(48))
                ,item -> StringUtils.capitalize(item.toString().toLowerCase()))
                .SetOnOpenOrClose(isOpen -> {
                    CardCrawlGame.isPopupOpen = this.isActive;
                })
                .SetOnChange(costs -> {
                    CurrentTypes.clear();
                    CurrentTypes.addAll(costs);
                    if (onClick != null) {
                        onClick.Invoke(null);
                    }
                })
                .SetLabelFunctionForButton(items -> {
                    if (items.size() == 0) {
                        return GR.PCL.Strings.Misc.Any;
                    }
                    if (items.size() > 1) {
                        return items.size() + " " + GR.PCL.Strings.SeriesUI.ItemsSelected;
                    }
                    return StringUtils.join(PCLJUtils.Map(items, item -> StringUtils.capitalize(item.toString().toLowerCase())), ", ");
                }, null,false)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, GR.PCL.Strings.SeriesUI.Types)
                .SetIsMultiSelect(true)
                .SetCanAutosizeButton(true)
                .SetItems(AbstractCard.CardType.values());

        ColorsDropdown = new GUI_Dropdown<AbstractCard.CardColor>(new AdvancedHitbox(hb.x + TypesDropdown.hb.width + SPACING * 3, hb.y + SPACING * 3, Scale(240), Scale(48))
                ,item -> StringUtils.capitalize(item.toString().toLowerCase()))
                .SetOnOpenOrClose(isOpen -> {
                    CardCrawlGame.isPopupOpen = this.isActive;
                })
                .SetOnChange(colors -> {
                    CurrentColors.clear();
                    CurrentColors.addAll(colors);
                    if (onClick != null) {
                        onClick.Invoke(null);
                    }
                })
                .SetLabelFunctionForButton(items -> {
                    if (items.size() == 0) {
                        return GR.PCL.Strings.Misc.Any;
                    }
                    if (items.size() > 1) {
                        return items.size() + " " + GR.PCL.Strings.SeriesUI.ItemsSelected;
                    }
                    return StringUtils.join(PCLJUtils.Map(items, item -> StringUtils.capitalize(item.toString().toLowerCase())), ", ");
                }, null,false)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, GR.PCL.Strings.SeriesUI.Colors)
                .SetIsMultiSelect(true)
                .SetCanAutosizeButton(true);

        affinitiesSectionLabel = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
                new AdvancedHitbox(TypesDropdown.hb.x + TypesDropdown.hb.width + SPACING * 4, hb.y + SPACING * 6.1f, Scale(48), Scale(48)))
                .SetFont(EYBFontHelper.CardTitleFont_Small, 0.8f)
                .SetText(GR.PCL.Strings.SeriesUI.Affinities)
                .SetColor(Settings.GOLD_COLOR)
                .SetAlignment(0.5f, 0.0f, false);
        keywordsSectionLabel = affinitiesSectionLabel.MakeCopy().SetText(GR.PCL.Strings.SeriesUI.Keywords);
        currentTotalHeaderLabel = new GUI_Label(EYBFontHelper.CardTitleFont_Normal,
                new AdvancedHitbox(Settings.WIDTH * 0.01f, Settings.HEIGHT * 0.94f, Scale(48), Scale(48)))
                .SetFont(EYBFontHelper.CardTitleFont_Small, 1f)
                .SetText(GR.PCL.Strings.SeriesUI.Total)
                .SetColor(Settings.GOLD_COLOR)
                .SetAlignment(0.5f, 0.0f, false);
        currentTotalLabel = new GUI_Label(EYBFontHelper.CardTitleFont_Normal,
                new AdvancedHitbox(Settings.WIDTH * 0.01f, Settings.HEIGHT * 0.906f, Scale(48), Scale(48)))
                .SetFont(EYBFontHelper.CardTitleFont_Small, 1f)
                .SetText("")
                .SetColor(Settings.BLUE_TEXT_COLOR)
                .SetAlignment(0.5f, 0.0f, false);

        for (int i = 0; i < PCLAffinity.All().length; i++)
        {
            PCLAffinity a = PCLAffinity.All()[i];
            AffinityButtons.add(new AffinityKeywordButton(affinitiesSectionLabel.hb, a)
                            .SetLevel(CardKeywordFilters.CurrentAffinities.GetLevel(a))
                            .SetOffsets(0.5f + (i * 1.05f), -0.5f * (ICON_SIZE / affinitiesSectionLabel.hb.width))
                            .SetOnClick((button) -> {
                                button.SetLevel((button.currentLevel + 1) % 2);
                                CurrentAffinities.Set(button.Type, button.currentLevel);
                                if (onClick != null) {
                                    onClick.Invoke(null);
                                }
            }));
        }

    }

    public CardKeywordFilters Initialize(ActionT1<CardKeywordButton> onClick, ArrayList<AbstractCard> cards, boolean isColorless) {
        Clear(false, true);
        CurrentFilterCounts.clear();
        FilterButtons.clear();
        currentTotal = 0;

        HashSet<CardSeries> availableSeries = new HashSet<>();
        HashSet<Integer> availableCosts = new HashSet<>();
        HashSet<AbstractCard.CardColor> availableColors = new HashSet<>();
        HashSet<AbstractCard.CardRarity> availableRarities = new HashSet<>();
        HashSet<AbstractCard.CardType> availableTypes = new HashSet<>();

        this.onClick = onClick;
        this.isColorless = isColorless;
        referenceCards = cards;
        if (referenceCards != null) {
            currentTotal = (referenceCards.size() == 1 && referenceCards.get(0) instanceof FakeLibraryCard) ? 0 : referenceCards.size();
            for (AbstractCard card : referenceCards) {
                PCLCard eC = PCLJUtils.SafeCast(card, PCLCard.class);
                if (eC != null) {
                    for (PCLCardTooltip tooltip : GetAllTooltips(eC)) {
                        CurrentFilterCounts.merge(tooltip, 1, Integer::sum);
                    }
                    if (eC.series != null) {
                        availableSeries.add(eC.series);
                    }
                }
                availableRarities.add(card.rarity);
                availableTypes.add(card.type);
                availableCosts.add(Mathf.Clamp(card.cost, CostFilter.Unplayable.upperBound, CostFilter.Cost3Plus.lowerBound));
                availableColors.add(card.color);
            }
        }

        for (Map.Entry<PCLCardTooltip,Integer> filter : CurrentFilterCounts.entrySet())
        {
            int cardCount = filter.getValue();
            FilterButtons.add(new CardKeywordButton(hb, filter.getKey()).SetOnClick(onClick).SetCardCount(cardCount));
        }
        currentTotalLabel.SetText(currentTotal);

        ArrayList<CardSeries> seriesItems = new ArrayList<>(availableSeries);
        seriesItems.sort((a, b) -> StringUtils.compare(a.LocalizedName, b.LocalizedName));
        SeriesDropdown.SetItems(seriesItems);

        ArrayList<AbstractCard.CardRarity> rarityItems = new ArrayList<>();
        for (AbstractCard.CardRarity rarity : AbstractCard.CardRarity.values()) {
            if (availableRarities.contains(rarity)) {
                rarityItems.add(rarity);
            }
        }
        RaritiesDropdown.SetItems(rarityItems);

        ArrayList<AbstractCard.CardType> typesItems = new ArrayList<>();
        for (AbstractCard.CardType cardType : AbstractCard.CardType.values()) {
            if (availableTypes.contains(cardType)) {
                typesItems.add(cardType);
            }
        }
        TypesDropdown.SetItems(typesItems);

        ArrayList<CostFilter> costItems = new ArrayList<>();
        for (CostFilter c : CostFilter.values()) {
            if (availableCosts.contains(c.lowerBound) || availableCosts.contains(c.upperBound)) {
                costItems.add(c);
            }
        }
        CostDropdown.SetItems(costItems);

        ArrayList<AbstractCard.CardColor> colorsItems = new ArrayList<>(availableColors);
        colorsItems.sort((a, b) -> a == AbstractCard.CardColor.COLORLESS ? -1 : a == AbstractCard.CardColor.CURSE ? -2 : StringUtils.compare(a.name(), b.name()));
        ColorsDropdown.SetItems(colorsItems);
        isAccessedFromCardPool = colorsItems.size() > 1;
        if (isAccessedFromCardPool) {
            ColorsDropdown.SetSelection(PCLJUtils.Filter(colorsItems, c -> c != AbstractCard.CardColor.COLORLESS && c != AbstractCard.CardColor.CURSE), true);
        }

        return this;
    }

    public void Open() {
        CardCrawlGame.isPopupOpen = true;
        SetActive(true);
    }

    public void Close() {
        closeButton.hb.hovered = false;
        closeButton.hb.clicked = false;
        closeButton.hb.justHovered = false;
        InputHelper.justReleasedClickLeft = false;
        CardCrawlGame.isPopupOpen = false;
        SetActive(false);
    }


    public void Clear(boolean shouldInvoke, boolean shouldClearColors) {
        if (shouldClearColors) {
            CurrentColors.clear();
        }
        CurrentOrigins.clear();
        CurrentFilters.clear();
        CurrentSeries.clear();
        CurrentCosts.clear();
        CurrentRarities.clear();
        CurrentTypes.clear();
        CurrentAffinities.Clear();
        CostDropdown.SetSelectionIndices(null, false);
        OriginsDropdown.SetSelectionIndices(null, false);
        TypesDropdown.SetSelectionIndices(null, false);
        RaritiesDropdown.SetSelectionIndices(null, false);
        SeriesDropdown.SetSelectionIndices(null, false);
        for (AffinityKeywordButton c : AffinityButtons)
        {
            c.Reset(false);
        }
        if (shouldInvoke && onClick != null) {
            onClick.Invoke(null);
        }
    }

    public void Refresh(ArrayList<AbstractCard> cards)
    {
        referenceCards = cards;
        invalidated = true;
    }

    public void RefreshButtons() {
        CurrentFilterCounts.clear();
        currentTotal = 0;

        if (referenceCards != null) {
            currentTotal = (referenceCards.size() == 1 && referenceCards.get(0) instanceof FakeLibraryCard) ? 0 : referenceCards.size();
            for (AbstractCard card : referenceCards) {
                PCLCard eC = PCLJUtils.SafeCast(card, PCLCard.class);
                if (eC != null) {
                    for (PCLCardTooltip tooltip : GetAllTooltips(eC)) {
                        CurrentFilterCounts.merge(tooltip, 1, Integer::sum);
                    }
                }
            }
        }
        for (CardKeywordButton c : FilterButtons)
        {
            c.SetCardCount(CurrentFilterCounts.getOrDefault(c.Tooltip, 0));
        }

        currentTotalLabel.SetText(currentTotal);

        RefreshButtonOrder();
    }

    public void RefreshButtonOrder()
    {
        FilterButtons.sort((a, b) -> shouldSortByCount ? a.CardCount - b.CardCount : StringUtils.compare(a.Tooltip.title, b.Tooltip.title));

        int index = 0;
        for (CardKeywordButton c : FilterButtons)
        {
            if (c.isActive)
            {
                c.SetIndex(index);
                index += 1;
            }
        }
    }

    @Override
    public void Update() {
        hb.y = DRAW_START_Y + scrollDelta;
        SeriesDropdown.SetPosition(hb.x - SPACING * 3, DRAW_START_Y + scrollDelta + SPACING * 3);
        OriginsDropdown.SetPosition(SeriesDropdown.hb.x, SeriesDropdown.hb.y);
        if (isColorless) {
            CostDropdown.SetPosition(OriginsDropdown.hb.x + OriginsDropdown.hb.width + SPACING * 3, DRAW_START_Y + scrollDelta + SPACING * 3);
        }
        else {
            CostDropdown.SetPosition(SeriesDropdown.hb.x + SeriesDropdown.hb.width + SPACING * 3, DRAW_START_Y + scrollDelta + SPACING * 3);
        }
        RaritiesDropdown.SetPosition(CostDropdown.hb.x + CostDropdown.hb.width + SPACING * 3, DRAW_START_Y + scrollDelta + SPACING * 3);
        TypesDropdown.SetPosition(RaritiesDropdown.hb.x + RaritiesDropdown.hb.width + SPACING * 3, DRAW_START_Y + scrollDelta + SPACING * 3);
        affinitiesSectionLabel.SetPosition(TypesDropdown.hb.x + TypesDropdown.hb.width + SPACING * 4, DRAW_START_Y + scrollDelta + SPACING * 6.1f).Update();
        keywordsSectionLabel.SetPosition(hb.x- SPACING, DRAW_START_Y + scrollDelta + SPACING * 2).Update();
        currentTotalHeaderLabel.Update();
        currentTotalLabel.Update();
        hb.update();
        closeButton.TryUpdate();
        clearButton.TryUpdate();
        if (invalidated) {
            invalidated = false;
            RefreshButtons();
        }

        if (!GR.UI.IsDropdownOpen) {
            for (CardKeywordButton c : FilterButtons)
            {
                c.TryUpdate();
            }

            if (ShouldShowScrollbar())
            {
                scrollBar.Update();
                UpdateScrolling(scrollBar.isDragging);
            }
            else
            {
                UpdateScrolling(false);
            }

            UpdateInput();
        }

        if (isColorless) {
            OriginsDropdown.TryUpdate();
        }
        else {
            SeriesDropdown.TryUpdate();
        }
        CostDropdown.TryUpdate();
        RaritiesDropdown.TryUpdate();
        TypesDropdown.TryUpdate();
        for (AffinityKeywordButton c : AffinityButtons)
        {
            c.TryUpdate();
        }
    }

    @Override
    public void Render(SpriteBatch sb) {
        sb.setColor(FADE_COLOR);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
        sb.setColor(Color.WHITE);
        hb.render(sb);
        closeButton.TryRender(sb);
        clearButton.TryRender(sb);
        affinitiesSectionLabel.Render(sb);
        keywordsSectionLabel.Render(sb);
        currentTotalHeaderLabel.Render(sb);
        currentTotalLabel.Render(sb);
        for (CardKeywordButton c : FilterButtons)
        {
            c.TryRender(sb);
        }
        if (isColorless) {
            OriginsDropdown.TryRender(sb);
        }
        else {
            SeriesDropdown.TryRender(sb);
        }
        CostDropdown.TryRender(sb);
        RaritiesDropdown.TryRender(sb);
        TypesDropdown.TryRender(sb);
        for (AffinityKeywordButton c : AffinityButtons)
        {
            c.TryRender(sb);
        }
    }

    protected void OnScroll(float newPercent)
    {
        if (!GR.UI.IsDropdownOpen) {
            scrollDelta = MathHelper.valueFromPercentBetween(lowerScrollBound, upperScrollBound, newPercent);
        }
    }

    private void UpdateInput()
    {
        if (InputHelper.justClickedLeft)
        {
            if (closeButton.hb.hovered
                    || clearButton.hb.hovered
                    || SeriesDropdown.AreAnyItemsHovered()
                    || OriginsDropdown.AreAnyItemsHovered()
                    || CostDropdown.AreAnyItemsHovered()
                    || RaritiesDropdown.AreAnyItemsHovered()
                    || TypesDropdown.AreAnyItemsHovered()
                    || PCLJUtils.Any(AffinityButtons, ab -> ab.background_button.hb.hovered)) {
                return;
            }
            for (CardKeywordButton c : FilterButtons)
            {
                if (c.background_button.hb.hovered) {
                    //CardCrawlGame.sound.play("UI_CLICK_1");
                    //c.background_button.onLeftClick.Complete(c.background_button);
                    return;
                }
            }
            PCLJUtils.LogInfo(this, "Closing");
            Close();
            InputHelper.justClickedLeft = false;
        }
        else if (InputHelper.pressedEscape || CInputActionSet.cancel.isJustPressed())
        {
            CInputActionSet.cancel.unpress();
            InputHelper.pressedEscape = false;
            Close();
        }
    }

    protected void UpdateScrolling(boolean isDraggingScrollBar)
    {
        if (!isDraggingScrollBar)
        {
            if (draggingScreen)
            {
                if (InputHelper.isMouseDown && GR.UI.TryDragging())
                {
                    scrollDelta = InputHelper.mY - scrollStart;
                }
                else
                {
                    draggingScreen = false;
                }
            }
            else
            {
                if (InputHelper.scrolledDown)
                {
                    scrollDelta += Settings.SCROLL_SPEED;
                }
                else if (InputHelper.scrolledUp)
                {
                    scrollDelta -= Settings.SCROLL_SPEED;
                }

                if (canDragScreen && InputHelper.justClickedLeft && GR.UI.TryDragging())
                {
                    draggingScreen = true;
                    scrollStart = InputHelper.mY - scrollDelta;
                }
            }
        }

        if (filterSizeCache != FilterButtons.size())
        {
            RefreshOffset();
        }

        if (scrollDelta < lowerScrollBound)
        {
            scrollDelta = MathHelper.scrollSnapLerpSpeed(scrollDelta, lowerScrollBound);
        }
        else if (scrollDelta > upperScrollBound)
        {
            scrollDelta = MathHelper.scrollSnapLerpSpeed(scrollDelta, upperScrollBound);
        }

        scrollBar.Scroll(MathHelper.percentFromValueBetween(lowerScrollBound, upperScrollBound, scrollDelta), false);
    }

    public void RefreshOffset()
    {
        filterSizeCache = FilterButtons.size();
        upperScrollBound = Settings.DEFAULT_SCROLL_LIMIT;

        if (filterSizeCache > ROW_SIZE * 2)
        {
            int offset = ((filterSizeCache / ROW_SIZE) - ((filterSizeCache % ROW_SIZE > 0) ? 1 : 2));
            upperScrollBound += PAD_Y * offset;
        }
    }

    protected boolean ShouldShowScrollbar()
    {
        return autoShowScrollbar && upperScrollBound > SCROLL_BAR_THRESHOLD;
    }
}
