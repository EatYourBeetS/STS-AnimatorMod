package eatyourbeets.ui.controls;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;

import java.util.ArrayList;
import java.util.Collection;

// This class is incomplete
public class GUI_DynamicCardGrid extends GUIElement
{
    private static final float PAD_X = AbstractCard.IMG_WIDTH * 0.75f + Settings.CARD_VIEW_PAD_X;
    private static final float PAD_Y = AbstractCard.IMG_HEIGHT * 0.75f + Settings.CARD_VIEW_PAD_Y;

    public final ArrayList<AbstractCard> cards;
    public boolean autoShowScrollbar = true;
    public AbstractCard hoveredCard = null;
    public String message = null;
    public float scale;

    public int rowSize = 5;
    public boolean draggingScreen;
    public float drawStart_x;
    public float drawStart_y;
    public float pad_x;
    public float pad_y;

    protected ActionT1<AbstractCard> onCardClick;
    protected ActionT1<AbstractCard> onCardHovered;
    protected ActionT2<SpriteBatch, AbstractCard> onCardRender;
    protected float lowerScrollBound = -Settings.DEFAULT_SCROLL_LIMIT;
    protected float upperScrollBound = Settings.DEFAULT_SCROLL_LIMIT;
    protected float scrollStart;
    protected float scrollDelta;
    protected int deckSizeCache;

    public GUI_DynamicCardGrid()
    {
        this.scale = 1;
        this.cards = new ArrayList<>();
        this.drawStart_x = (Settings.WIDTH - (5f * AbstractCard.IMG_WIDTH * 0.75f) - (4f * Settings.CARD_VIEW_PAD_X) + AbstractCard.IMG_WIDTH * 0.75f) * 0.4f;
        this.drawStart_y = ScreenH(0.7f);
        this.pad_x = PAD_X * scale;
        this.pad_y = PAD_Y * scale;
    }

    public GUI_DynamicCardGrid SetOnCardHover(ActionT1<AbstractCard> onCardHovered)
    {
        this.onCardHovered = onCardHovered;

        return this;
    }

    public GUI_DynamicCardGrid SetOnCardClick(ActionT1<AbstractCard> onCardClicked)
    {
        this.onCardClick = onCardClicked;

        return this;
    }

    public GUI_DynamicCardGrid SetOnCardRender(ActionT2<SpriteBatch, AbstractCard> onCardRender)
    {
        this.onCardRender = onCardRender;

        return this;
    }

    public GUI_DynamicCardGrid SetRowSize(int size)
    {
        if (size < 1)
        {
            throw new IllegalArgumentException("Row size must be greater than 0");
        }

        this.rowSize = size;

        return this;
    }

    public GUI_DynamicCardGrid UseScrollbar(boolean autoShowScrollbar)
    {
        this.autoShowScrollbar = autoShowScrollbar;

        return this;
    }

    public GUI_DynamicCardGrid SetDrawStart(float x, float y)
    {
        this.drawStart_x = x;
        this.drawStart_y = y;

        return this;
    }

    public GUI_DynamicCardGrid AddPadY(float padY)
    {
        this.pad_y += padY * scale;

        return this;
    }

    public GUI_DynamicCardGrid SetScale(float scale)
    {
        this.scale = scale;
        this.pad_x = PAD_X * scale;
        this.pad_y = PAD_Y * scale;

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

    public GUI_DynamicCardGrid AddCards(Collection<AbstractCard> cards)
    {
        for (AbstractCard card : cards)
        {
            AddCard(card);
        }

        return this;
    }

    public GUI_DynamicCardGrid AddCard(AbstractCard card)
    {
        card.targetDrawScale = card.drawScale = (0.75f * scale);
        card.setAngle(0, true);
        card.lighten(true);
        cards.add(card);

        return this;
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        for (AbstractCard card : cards)
        {
            if (card != hoveredCard)
            {
                RenderCard(sb, card);
            }
        }

        if (hoveredCard != null)
        {
            hoveredCard.renderHoverShadow(sb);
            RenderCard(sb, hoveredCard);

            if (!AfterLifeMod.IsAdded(hoveredCard))
            {
                //Prevent Afterlife cards from showing card tooltips when in the control pile which can cause a crash
                hoveredCard.renderCardTip(sb);
            }
        }

        if (message != null)
        {
            FontHelper.renderDeckViewTip(sb, message, Scale(96f), Settings.CREAM_COLOR);
        }
    }

    @Override
    public void Update()
    {
        UpdateCards();

        if (hoveredCard != null && GR.UI.TryHover(hoveredCard.hb))
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
            card.target_x = drawStart_x + (column * pad_x);
            card.target_y = drawStart_y + scrollDelta - (row * pad_y);
            card.drawScale = card.targetDrawScale = (0.75f * scale);
            card.fadingOut = false;
            card.update();
            card.updateHoverLogic();

            if (card.hb.hovered)
            {
                hoveredCard = card;
                card.drawScale = card.targetDrawScale = (0.8f * scale);
            }

            column += 1;
            if (column >= rowSize)
            {
                column = 0;
                row += 1;
            }
        }
    }

    protected void RenderCard(SpriteBatch sb, AbstractCard card)
    {
        card.render(sb);

        if (onCardRender != null)
        {
            onCardRender.Invoke(sb, card);
        }
    }

    protected void RefreshDeckSize()
    {
        deckSizeCache = cards.size();
    }
}
