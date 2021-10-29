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
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_VerticalScrollBar;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.ui.hitboxes.DraggableHitbox;
import eatyourbeets.utilities.JUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CardKeywordFilters extends GUIElement
{
    private static final Color FADE_COLOR = new Color(0f, 0f, 0f, 0.8f);
    protected static final float DRAW_START_X = (float) Settings.WIDTH * 0.15f;
    protected static final float DRAW_START_Y = (float) Settings.HEIGHT * 0.92f;
    protected static final float PAD_X = AbstractCard.IMG_WIDTH * 0.75f + Settings.CARD_VIEW_PAD_X;
    protected static final float PAD_Y = Scale(10);
    protected static final float SCROLL_BAR_THRESHOLD = 500f * Settings.scale;

    public static final int ROW_SIZE = 8;
    public static final HashSet<EYBCardTooltip> CurrentFilters = new HashSet<>();
    protected final HashMap<EYBCardTooltip,Integer> CurrentFilterCounts = new HashMap<>();
    protected final ArrayList<CardKeywordButton> FilterButtons = new ArrayList<>();
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
    protected final GUI_Button clearButton;
    public final GUI_VerticalScrollBar scrollBar;
    public final AdvancedHitbox hb;
    public boolean draggingScreen;
    public boolean autoShowScrollbar;


    private boolean shouldSortByCount;

    public CardKeywordFilters()
    {
        isActive = false;
        hb = new AdvancedHitbox(DRAW_START_X, DRAW_START_Y, Scale(180), Scale(70)).SetIsPopupCompatible(true);
        clearButton = new GUI_Button(GR.Common.Images.HexagonalButton.Texture(), new DraggableHitbox(0, 0, Settings.WIDTH * 0.07f, Settings.HEIGHT * 0.07f).SetIsPopupCompatible(true))
                .SetBorder(GR.Common.Images.HexagonalButtonBorder.Texture(), Color.WHITE)
                .SetColor(Color.FIREBRICK)
                .SetPosition(Settings.WIDTH * 0.96f, Settings.HEIGHT * 0.86f).SetText("Clear")
                .SetOnClick(this::Clear);
        this.scrollBar = new GUI_VerticalScrollBar(new Hitbox(ScreenW(0.03f), ScreenH(0.7f)))
                .SetOnScroll(this::OnScroll);
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

    public void Clear() {
        CurrentFilters.clear();
        if (onClick != null) {
            onClick.Invoke(null);
        }
    }

    public void Refresh(ArrayList<AbstractCard> cards, ActionT1<CardKeywordButton> onClick){
        this.onClick = onClick;
        Refresh(cards);
    }

    public void Refresh(ArrayList<AbstractCard> cards)
    {
        referenceCards = cards;
        invalidated = true;
    }

    public void RefreshButtons() {
        CurrentFilterCounts.clear();
        FilterButtons.clear();

        if (referenceCards != null) {
            for (AbstractCard card : referenceCards) {
                if (card instanceof EYBCard) {
                    for (EYBCardTooltip tooltip : ((EYBCard) card).tooltips) {
                        CurrentFilterCounts.merge(tooltip, 1, Integer::sum);
                    }
                }
            }
        }

        for (Map.Entry<EYBCardTooltip,Integer> filter : CurrentFilterCounts.entrySet())
        {
            FilterButtons.add(new CardKeywordButton(hb, filter.getKey()).SetOnClick(onClick).SetCardCount(filter.getValue()));
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
        hb.update();
        clearButton.TryUpdate();
        if (invalidated) {
            invalidated = false;
            RefreshButtons();
        }

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

    @Override
    public void Render(SpriteBatch sb) {
        sb.setColor(FADE_COLOR);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
        sb.setColor(Color.WHITE);
        hb.render(sb);
        clearButton.TryRender(sb);
        for (CardKeywordButton c : FilterButtons)
        {
            c.TryRender(sb);
        }
    }

    protected void OnScroll(float newPercent)
    {
        scrollDelta = MathHelper.valueFromPercentBetween(lowerScrollBound, upperScrollBound, newPercent);
    }

    private void UpdateInput()
    {
        if (InputHelper.justClickedLeft)
        {
            if (clearButton.hb.hovered) {
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
