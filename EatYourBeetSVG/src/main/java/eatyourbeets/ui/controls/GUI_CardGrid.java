package eatyourbeets.ui.controls;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;

import java.util.ArrayList;
import java.util.Collection;

public class GUI_CardGrid extends GUIElement
{
    private static final float DRAW_START_X = (Settings.WIDTH - (5f * AbstractCard.IMG_WIDTH * 0.75f) - (4f * Settings.CARD_VIEW_PAD_X) + AbstractCard.IMG_WIDTH * 0.75f) * 0.4f; // 0.5f
    private static final float DRAW_START_Y = (float) Settings.HEIGHT * 0.7f;
    private static final float PAD_X = AbstractCard.IMG_WIDTH * 0.75f + Settings.CARD_VIEW_PAD_X;
    private static final float PAD_Y = AbstractCard.IMG_HEIGHT * 0.75f + Settings.CARD_VIEW_PAD_Y;
    private static final float SCROLL_BAR_THRESHOLD = 500f * Settings.scale;
    private static final int ROW_SIZE = 5;

    public final GUI_VerticalScrollBar scrollBar;
    public final ArrayList<AbstractCard> cards;
    public boolean autoShowScrollbar = true;
    public AbstractCard hoveredCard = null;
    public String message = null;

    protected ActionT1<AbstractCard> onCardClick;
    protected ActionT1<AbstractCard> onCardHovered;
    protected boolean draggingScreen;
    protected float lowerScrollBound = -Settings.DEFAULT_SCROLL_LIMIT;
    protected float upperScrollBound = Settings.DEFAULT_SCROLL_LIMIT;
    protected float scrollStart;
    protected float scrollDelta;
    protected int deckSizeCache;

    public GUI_CardGrid()
    {
        this.cards = new ArrayList<>();
        this.scrollBar = new GUI_VerticalScrollBar(new Hitbox(ScreenW(0.03f), ScreenH(0.7f)))
        .SetPosition(ScreenW(0.05f), ScreenH(0.5f))
        .SetOnScroll(this::OnScroll);
    }

    public GUI_CardGrid SetOnCardHover(ActionT1<AbstractCard> onCardHovered)
    {
        this.onCardHovered = onCardHovered;

        return this;
    }

    public GUI_CardGrid SetOnCardClick(ActionT1<AbstractCard> onCardClicked)
    {
        this.onCardClick = onCardClicked;

        return this;
    }

    public void Clear()
    {
        this.deckSizeCache = 0;
        this.hoveredCard = null;
        this.scrollDelta = 0f;
        this.scrollStart = 0f;
        this.draggingScreen = false;
        this.message = null;
        this.cards.clear();

        RefreshDeckSize();
    }

    public GUI_CardGrid AddCards(Collection<AbstractCard> cards)
    {
        for (AbstractCard card : cards)
        {
            AddCard(card);
        }

        return this;
    }

    public GUI_CardGrid AddCard(AbstractCard card)
    {
        card.targetDrawScale = card.drawScale = 0.75f;
        card.setAngle(0, true);
        card.lighten(true);
        cards.add(card);

        return this;
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        if (ShouldShowScrollbar())
        {
            scrollBar.Render(sb);
        }

        for (AbstractCard card : cards)
        {
            if (card != hoveredCard)
            {
                card.render(sb);
            }
        }

        if (hoveredCard != null)
        {
            hoveredCard.renderHoverShadow(sb);
            hoveredCard.render(sb);
            hoveredCard.renderCardTip(sb);
        }

        if (message != null)
        {
            FontHelper.renderDeckViewTip(sb, message, Scale(96f), Settings.CREAM_COLOR);
        }
    }

    @Override
    public void Update()
    {
        if (ShouldShowScrollbar())
        {
            scrollBar.Update();
            UpdateScrolling(scrollBar.isDragging);
        }
        else
        {
            UpdateScrolling(false);
        }

        UpdateCards();

        if (hoveredCard != null)
        {
            if (InputHelper.justClickedLeft)
            {
                hoveredCard.hb.clickStarted = true;
            }

            if (hoveredCard.hb.clicked || CInputActionSet.select.isJustPressed())
            {
                hoveredCard.hb.clicked = false;

                if (onCardClick != null)
                {
                    onCardClick.Invoke(hoveredCard);
                }
            }
        }
    }

    protected void UpdateCards()
    {
        hoveredCard = null;

        int row = 0;
        int column = 0;
        for (AbstractCard card : cards)
        {
            card.target_x = DRAW_START_X + (column * PAD_X);
            card.target_y = DRAW_START_Y + scrollDelta - (row * PAD_Y);
            card.fadingOut = false;
            card.update();
            card.updateHoverLogic();

            if (card.hb.hovered)
            {
                hoveredCard = card;
            }

            column += 1;
            if (column >= ROW_SIZE)
            {
                column = 0;
                row += 1;
            }
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

                if (InputHelper.justClickedLeft && GR.UI.TryDragging())
                {
                    draggingScreen = true;
                    scrollStart = InputHelper.mY - scrollDelta;
                }
            }
        }

        if (deckSizeCache != cards.size())
        {
            RefreshDeckSize();
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

    protected void RefreshDeckSize()
    {
        deckSizeCache = cards.size();
        upperScrollBound = Settings.DEFAULT_SCROLL_LIMIT;

        if (deckSizeCache > 10)
        {
            int offset = ((deckSizeCache / ROW_SIZE) - ((deckSizeCache % ROW_SIZE > 0) ? 1 : 2));
            upperScrollBound += PAD_Y * offset;
        }
    }

    protected void OnScroll(float newPercent)
    {
        scrollDelta = MathHelper.valueFromPercentBetween(lowerScrollBound, upperScrollBound, newPercent);
    }

    protected boolean ShouldShowScrollbar()
    {
        return autoShowScrollbar && upperScrollBound > SCROLL_BAR_THRESHOLD;
    }
}
