package eatyourbeets.ui.common;

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
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Dropdown;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.controls.GUI_VerticalScrollBar;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.ui.hitboxes.DraggableHitbox;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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

    private static final Color FADE_COLOR = new Color(0f, 0f, 0f, 0.84f);
    protected static final float SPACING = Settings.scale * 22.5f;
    protected static final float DRAW_START_X = (float) Settings.WIDTH * 0.15f;
    protected static final float DRAW_START_Y = (float) Settings.HEIGHT * 0.78f;
    protected static final float PAD_X = AbstractCard.IMG_WIDTH * 0.75f + Settings.CARD_VIEW_PAD_X;
    protected static final float PAD_Y = Scale(10);
    protected static final float SCROLL_BAR_THRESHOLD = 500f * Settings.scale;

    public static final int ROW_SIZE = 8;
    public static final HashSet<CardSeries> CurrentSeries = new HashSet<>();
    public static final HashSet<EYBCardTooltip> CurrentFilters = new HashSet<>();
    public static final HashSet<CostFilter> CurrentCosts = new HashSet<>();
    public static final HashSet<AbstractCard.CardRarity> CurrentRarities = new HashSet<>();
    public static final HashSet<AbstractCard.CardType> CurrentTypes = new HashSet<>();
    public static EYBCardAffinities CurrentAffinities = new EYBCardAffinities(null);
    protected final HashMap<EYBCardTooltip,Integer> CurrentFilterCounts = new HashMap<>();
    protected final ArrayList<CardKeywordButton> FilterButtons = new ArrayList<>();
    protected final ArrayList<AffinityKeywordButton> AffinityButtons = new ArrayList<>();
    protected final ArrayList<GUI_Button> ScalingButtons = new ArrayList<>();
    protected ArrayList<AbstractCard> referenceCards;
    protected ActionT1<CardKeywordButton> onClick;

    protected boolean canDragScreen = false; //TODO allow toggle
    protected boolean invalidated;
    protected float draw_x;
    protected float lowerScrollBound = -Settings.DEFAULT_SCROLL_LIMIT;
    protected float upperScrollBound = Settings.DEFAULT_SCROLL_LIMIT;
    protected float scrollStart;
    protected float scrollDelta;
    protected int filterSizeCache;
    protected final GUI_Dropdown<CardSeries> SeriesDropdown;
    protected final GUI_Dropdown<CostFilter> CostDropdown;
    protected final GUI_Dropdown<AbstractCard.CardRarity> RaritiesDropdown;
    protected final GUI_Dropdown<AbstractCard.CardType> TypesDropdown;
    protected final GUI_Button clearButton;
    public final GUI_VerticalScrollBar scrollBar;
    public final GUI_Label affinitiesSectionLabel;
    public final GUI_Label keywordsSectionLabel;
    public final AdvancedHitbox hb;
    public boolean draggingScreen;
    public boolean autoShowScrollbar;
    public boolean isScreenDisabled;

    private boolean shouldSortByCount;

    public static ArrayList<EYBCardTooltip> GetAllTooltips(EYBCard eC) {
        ArrayList<EYBCardTooltip> dynamicTooltips = new ArrayList<>();
        if (eC != null) {
            eC.GenerateDynamicTooltips(dynamicTooltips);
            for (EYBCardTooltip tip : eC.tooltips)
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
        return JUtils.Filter(input, c -> {
                    //Series check
                    if (!CurrentSeries.isEmpty()) {
                        AnimatorCard aC = JUtils.SafeCast(c, AnimatorCard.class);
                        if (aC == null || aC.series == null || !CurrentSeries.contains(aC.series)) {
                            return false;
                        }
                    }

                    //Tooltips check
                    EYBCard eC = JUtils.SafeCast(c, EYBCard.class);
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
                    for (EYBCardAffinity cAffinity : CurrentAffinities.List) {
                        if (GameUtilities.GetAffinityLevel(c, cAffinity.type, true) < cAffinity.level) {
                            return false;
                        }
                    }
                    if (GameUtilities.GetAffinityLevel(c, Affinity.Star, true) < CurrentAffinities.GetLevel(Affinity.Star)) {
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
        return CurrentFilters.isEmpty() && CurrentSeries.isEmpty() && CurrentCosts.isEmpty() && CurrentRarities.isEmpty() && CurrentTypes.isEmpty() && CurrentAffinities.IsEmpty();
    }

    public CardKeywordFilters()
    {
        ArrayList<CardSeries> series = JUtils.Map(GR.Animator.Data.BaseLoadouts, loadout -> loadout.Series);

        isActive = false;
        hb = new AdvancedHitbox(DRAW_START_X, DRAW_START_Y, Scale(180), Scale(70)).SetIsPopupCompatible(true);
        clearButton = new GUI_Button(GR.Common.Images.HexagonalButton.Texture(), new DraggableHitbox(0, 0, Settings.WIDTH * 0.07f, Settings.HEIGHT * 0.07f).SetIsPopupCompatible(true))
                .SetBorder(GR.Common.Images.HexagonalButtonBorder.Texture(), Color.WHITE)
                .SetColor(Color.FIREBRICK)
                .SetPosition(Settings.WIDTH * 0.96f, Settings.HEIGHT * 0.86f).SetText("Clear")
                .SetOnClick(() -> this.Clear(true));
        this.scrollBar = new GUI_VerticalScrollBar(new Hitbox(ScreenW(0.03f), ScreenH(0.7f)))
                .SetOnScroll(this::OnScroll);

        SeriesDropdown = new GUI_Dropdown<CardSeries>(new AdvancedHitbox(hb.x - SPACING * 3, hb.y + SPACING * 3, Scale(240), Scale(48)), cs -> cs.LocalizedName)
                .SetOnOpenOrClose(isOpen -> {
                    isScreenDisabled = isOpen;
                    CardCrawlGame.isPopupOpen = this.isActive;
                })
                .SetOnChange(selectedSeries -> {
                    CurrentSeries.clear();
                    CurrentSeries.addAll(selectedSeries);
                    if (onClick != null) {
                        onClick.Invoke(null);
                    }
                })
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, GR.Animator.Strings.SeriesUI.SeriesUI)
                .SetIsMultiSelect(true)
                .SetCanAutosizeButton(true);

        CostDropdown = new GUI_Dropdown<CostFilter>(new AdvancedHitbox(hb.x + SeriesDropdown.hb.width + SPACING * 3, hb.y + SPACING * 3, Scale(240), Scale(48)), c -> c.name)
                .SetOnOpenOrClose(isOpen -> {
                    isScreenDisabled = isOpen;
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
                        return GR.Animator.Strings.Misc.Any;
                    }
                    if (items.size() > 1) {
                        return items.size() + " " + GR.Animator.Strings.SeriesUI.ItemsSelected;
                    }
                    return StringUtils.join(JUtils.Map(items, item -> item.name), ", ");
                }, null,false)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, GR.Animator.Strings.SeriesUI.Costs)
                .SetIsMultiSelect(true)
                .SetCanAutosizeButton(true)
                .SetItems(CostFilter.values());

        RaritiesDropdown = new GUI_Dropdown<AbstractCard.CardRarity>(new AdvancedHitbox(hb.x + CostDropdown.hb.width + SPACING * 3, hb.y + SPACING * 3, Scale(240), Scale(48))
                ,item -> StringUtils.capitalize(item.toString().toLowerCase()))
                .SetOnOpenOrClose(isOpen -> {
                    isScreenDisabled = isOpen;
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
                        return GR.Animator.Strings.Misc.Any;
                    }
                    if (items.size() > 1) {
                        return items.size() + " " + GR.Animator.Strings.SeriesUI.ItemsSelected;
                    }
                    return StringUtils.join(JUtils.Map(items, item -> StringUtils.capitalize(item.toString().toLowerCase())), ", ");
                }, null,false)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, GR.Animator.Strings.SeriesUI.Rarities)
                .SetIsMultiSelect(true)
                .SetCanAutosizeButton(true)
                .SetItems(AbstractCard.CardRarity.values());

        TypesDropdown = new GUI_Dropdown<AbstractCard.CardType>(new AdvancedHitbox(hb.x + RaritiesDropdown.hb.width + SPACING * 3, hb.y + SPACING * 3, Scale(240), Scale(48))
                ,item -> StringUtils.capitalize(item.toString().toLowerCase()))
                .SetOnOpenOrClose(isOpen -> {
                    isScreenDisabled = isOpen;
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
                        return GR.Animator.Strings.Misc.Any;
                    }
                    if (items.size() > 1) {
                        return items.size() + " " + GR.Animator.Strings.SeriesUI.ItemsSelected;
                    }
                    return StringUtils.join(JUtils.Map(items, item -> StringUtils.capitalize(item.toString().toLowerCase())), ", ");
                }, null,false)
                .SetHeader(EYBFontHelper.CardTitleFont_Small, 0.8f, Settings.GOLD_COLOR, GR.Animator.Strings.SeriesUI.Types)
                .SetIsMultiSelect(true)
                .SetCanAutosizeButton(true)
                .SetItems(AbstractCard.CardType.values());

        affinitiesSectionLabel = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
                new AdvancedHitbox(TypesDropdown.hb.x + TypesDropdown.hb.width + SPACING * 4, hb.y + SPACING * 6.1f, Scale(48), Scale(48)))
                .SetFont(EYBFontHelper.CardTitleFont_Small, 0.8f)
                .SetText(GR.Animator.Strings.SeriesUI.Affinities)
                .SetColor(Settings.GOLD_COLOR)
                .SetAlignment(0.5f, 0.0f, false);
        keywordsSectionLabel = affinitiesSectionLabel.MakeCopy().SetText(GR.Animator.Strings.SeriesUI.Keywords);

        for (int i = 0; i < Affinity.All().length; i++)
        {
            Affinity a = Affinity.All()[i];
            AffinityButtons.add(new AffinityKeywordButton(affinitiesSectionLabel.hb, a, CardKeywordFilters.CurrentAffinities.GetLevel(a)).SetIndex(i).SetOnClick((button) -> {
                CurrentAffinities.Set(button.Type, button.currentLevel);
                if (onClick != null) {
                    onClick.Invoke(null);
                }
            }));
        }

    }

    public CardKeywordFilters Initialize(ActionT1<CardKeywordButton> onClick, ArrayList<AbstractCard> cards) {
        CurrentFilters.clear();
        CurrentSeries.clear();
        CurrentCosts.clear();
        CurrentRarities.clear();
        CurrentTypes.clear();
        CurrentAffinities.Clear();
        CurrentFilterCounts.clear();
        FilterButtons.clear();

        HashSet<CardSeries> availableSeries = new HashSet<>();

        this.onClick = onClick;
        referenceCards = cards;
        for (AbstractCard card : referenceCards) {
            EYBCard eC = JUtils.SafeCast(card, EYBCard.class);
            if (eC != null) {
                for (EYBCardTooltip tooltip : GetAllTooltips(eC)) {
                    CurrentFilterCounts.merge(tooltip, 1, Integer::sum);
                }
                if (eC instanceof AnimatorCard && ((AnimatorCard) eC).series != null) {
                    availableSeries.add(((AnimatorCard) eC).series);
                }
            }
        }

        for (Map.Entry<EYBCardTooltip,Integer> filter : CurrentFilterCounts.entrySet())
        {
            FilterButtons.add(new CardKeywordButton(hb, filter.getKey()).SetOnClick(onClick).SetCardCount(filter.getValue()));
        }
        for (AffinityKeywordButton c : AffinityButtons)
        {
            c.Reset(false);
        }

        ArrayList<CardSeries> items = new ArrayList<CardSeries>(availableSeries);
        items.sort((a, b) -> StringUtils.compare(a.LocalizedName, b.LocalizedName));
        SeriesDropdown.SetItems(items);

        return this;
    }

    public void Open() {
        CardCrawlGame.isPopupOpen = true;
        SetActive(true);
    }

    public void Close() {
        InputHelper.justReleasedClickLeft = false;
        CardCrawlGame.isPopupOpen = false;
        SetActive(false);
    }


    public void Clear(boolean shouldInvoke) {
        CurrentFilters.clear();
        CurrentSeries.clear();
        CurrentCosts.clear();
        CurrentRarities.clear();
        CurrentTypes.clear();
        CurrentAffinities.Clear();
        SeriesDropdown.SetSelectionIndices(new int[]{}, false);
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

        if (referenceCards != null) {
            for (AbstractCard card : referenceCards) {
                EYBCard eC = JUtils.SafeCast(card, EYBCard.class);
                if (eC != null) {
                    for (EYBCardTooltip tooltip : GetAllTooltips(eC)) {
                        CurrentFilterCounts.merge(tooltip, 1, Integer::sum);
                    }
                }
            }
        }
        for (CardKeywordButton c : FilterButtons)
        {
            c.SetCardCount(CurrentFilterCounts.getOrDefault(c.Tooltip, 0));
        }

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
        CostDropdown.SetPosition(SeriesDropdown.hb.x + SeriesDropdown.hb.width + SPACING * 3, DRAW_START_Y + scrollDelta + SPACING * 3);
        RaritiesDropdown.SetPosition(CostDropdown.hb.x + CostDropdown.hb.width + SPACING * 3, DRAW_START_Y + scrollDelta + SPACING * 3);
        TypesDropdown.SetPosition(RaritiesDropdown.hb.x + RaritiesDropdown.hb.width + SPACING * 3, DRAW_START_Y + scrollDelta + SPACING * 3);
        affinitiesSectionLabel.SetPosition(TypesDropdown.hb.x + TypesDropdown.hb.width + SPACING * 4, DRAW_START_Y + scrollDelta + SPACING * 6.1f).Update();
        keywordsSectionLabel.SetPosition(hb.x- SPACING, DRAW_START_Y + scrollDelta + SPACING * 2).Update();
        hb.update();
        clearButton.TryUpdate();
        if (invalidated) {
            invalidated = false;
            RefreshButtons();
        }

        if (!isScreenDisabled) {
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

        SeriesDropdown.TryUpdate();
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
        clearButton.TryRender(sb);
        affinitiesSectionLabel.Render(sb);
        keywordsSectionLabel.Render(sb);
        for (CardKeywordButton c : FilterButtons)
        {
            c.TryRender(sb);
        }
        SeriesDropdown.TryRender(sb);
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
        if (!isScreenDisabled) {
            scrollDelta = MathHelper.valueFromPercentBetween(lowerScrollBound, upperScrollBound, newPercent);
        }
    }

    private void UpdateInput()
    {
        if (InputHelper.justClickedLeft)
        {
            if (clearButton.hb.hovered
                    || SeriesDropdown.AreAnyItemsHovered()
                    || CostDropdown.AreAnyItemsHovered()
                    || RaritiesDropdown.AreAnyItemsHovered()
                    || TypesDropdown.AreAnyItemsHovered()
                    || JUtils.Any(AffinityButtons, ab -> ab.background_button.hb.hovered)) {
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
            JUtils.LogInfo(this, "Closing");
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
