package eatyourbeets.screens.controls;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import com.megacrit.cardcrawl.ui.buttons.GridSelectConfirmButton;
import eatyourbeets.interfaces.UIControl;
import eatyourbeets.interfaces.csharp.ActionT1;
import eatyourbeets.resources.GR;

import java.util.ArrayList;

public class GridLayoutControl implements UIControl, ScrollBarListener
{
    private ActionT1<AbstractCard> onCardClicked;

    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    private static final float DRAW_START_X;
    private static final float DRAW_START_Y;
    private static final float PAD_X;
    private static final float PAD_Y;

    private static final int CARDS_PER_LINE = 5;
    private static final float SCROLL_BAR_THRESHOLD;
    private float grabStartY = 0.0F;
    private float scrollDelta = 0.0F;
    public ArrayList<AbstractCard> selectedCards = new ArrayList<>();
    public CardGroup targetGroup;
    private AbstractCard hoveredCard = null;
    private int numCards = 0;
    private int cardSelectAmount = 0;
    private float scrollLowerBound;
    private float scrollUpperBound;
    private boolean grabbedScreen;
    private boolean canCancel;
    public GridSelectConfirmButton confirmButton;
    private String tipMsg;
    private int prevDeckSize;
    public boolean cancelWasOn;
    public boolean anyNumber;
    public String cancelText;
    private ScrollBar scrollBar;
    private static final int ARROW_W = 64;

    static
    {
        uiStrings = CardCrawlGame.languagePack.getUIString("GridCardSelectScreen");
        TEXT = uiStrings.TEXT;
        SCROLL_BAR_THRESHOLD = 500.0F * Settings.scale;
        DRAW_START_X = (Settings.WIDTH - (5.0F * AbstractCard.IMG_WIDTH * 0.75F) - (4.0F * Settings.CARD_VIEW_PAD_X) + AbstractCard.IMG_WIDTH * 0.75F) * 0.4f; // 0.5f
        DRAW_START_Y = (float) Settings.HEIGHT * 0.7F;
        PAD_X = AbstractCard.IMG_WIDTH * 0.75F + Settings.CARD_VIEW_PAD_X;
        PAD_Y = AbstractCard.IMG_HEIGHT * 0.75F + Settings.CARD_VIEW_PAD_Y;
    }

    public GridLayoutControl()
    {
        this.scrollLowerBound = -Settings.DEFAULT_SCROLL_LIMIT;
        this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        this.grabbedScreen = false;
        this.canCancel = true;
        this.confirmButton = new GridSelectConfirmButton(TEXT[0]);
        this.tipMsg = "";
        this.prevDeckSize = 0;
        this.cancelWasOn = false;
        this.anyNumber = false;
        this.scrollBar = new ScrollBar(this);
        this.scrollBar.move(0.0F, -30.0F * Settings.scale);
    }

    public void Open(CardGroup group, ActionT1<AbstractCard> onCardClicked)
    {
        this.targetGroup = group;

        this.onCardClicked = onCardClicked;

        this.anyNumber = false;
        this.canCancel = false;
        this.hoveredCard = null;
        this.selectedCards.clear();
        this.cardSelectAmount = 0;
        this.scrollDelta = 0.0F;
        this.grabStartY = 0.0F;
        this.grabbedScreen = false;
        this.hideCards();

        if (anyNumber)
        {
            this.confirmButton.show();
            this.confirmButton.isDisabled = false;
        }
        else
        {
            this.confirmButton.hideInstantly();
        }

        this.tipMsg = null;
        this.numCards = 9;

        if (canCancel)
        {
            AbstractDungeon.overlayMenu.cancelButton.show(TEXT[1]);
        }
        else
        {
            AbstractDungeon.overlayMenu.cancelButton.hide();
        }

        this.calculateScrollBounds();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        if (this.shouldShowScrollBar())
        {
            this.scrollBar.render(sb);
        }

        if (this.hoveredCard != null)
        {
            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT)
            {
                this.targetGroup.renderExceptOneCard(sb, this.hoveredCard);
            }
            else
            {
                this.targetGroup.renderExceptOneCardShowBottled(sb, this.hoveredCard);
            }

            this.hoveredCard.renderHoverShadow(sb);
            this.hoveredCard.render(sb);
            if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT)
            {
                AbstractRelic tmp;
                float prevX;
                float prevY;
                if (this.hoveredCard.inBottleFlame)
                {
                    tmp = RelicLibrary.getRelic("Bottled Flame");
                    prevX = tmp.currentX;
                    prevY = tmp.currentY;
                    tmp.currentX = this.hoveredCard.current_x + 130.0F * Settings.scale;
                    tmp.currentY = this.hoveredCard.current_y + 182.0F * Settings.scale;
                    tmp.scale = this.hoveredCard.drawScale * Settings.scale * 1.5F;
                    tmp.render(sb);
                    tmp.currentX = prevX;
                    tmp.currentY = prevY;
                }
                else if (this.hoveredCard.inBottleLightning)
                {
                    tmp = RelicLibrary.getRelic("Bottled Lightning");
                    prevX = tmp.currentX;
                    prevY = tmp.currentY;
                    tmp.currentX = this.hoveredCard.current_x + 130.0F * Settings.scale;
                    tmp.currentY = this.hoveredCard.current_y + 182.0F * Settings.scale;
                    tmp.scale = this.hoveredCard.drawScale * Settings.scale * 1.5F;
                    tmp.render(sb);
                    tmp.currentX = prevX;
                    tmp.currentY = prevY;
                }
                else if (this.hoveredCard.inBottleTornado)
                {
                    tmp = RelicLibrary.getRelic("Bottled Tornado");
                    prevX = tmp.currentX;
                    prevY = tmp.currentY;
                    tmp.currentX = this.hoveredCard.current_x + 130.0F * Settings.scale;
                    tmp.currentY = this.hoveredCard.current_y + 182.0F * Settings.scale;
                    tmp.scale = this.hoveredCard.drawScale * Settings.scale * 1.5F;
                    tmp.render(sb);
                    tmp.currentX = prevX;
                    tmp.currentY = prevY;
                }
            }

            this.hoveredCard.renderCardTip(sb);
        }
        else if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT)
        {
            this.targetGroup.render(sb);
        }
        else
        {
            this.targetGroup.renderShowBottled(sb);
        }

        if (this.anyNumber)
        {
            this.confirmButton.render(sb);
        }

        if (this.tipMsg != null)
        {
            FontHelper.renderDeckViewTip(sb, this.tipMsg, 96.0F * Settings.scale, Settings.CREAM_COLOR);
        }
    }

    @Override
    public void Update()
    {
        boolean isDraggingScrollBar = false;

        if (this.shouldShowScrollBar())
        {
            isDraggingScrollBar = this.scrollBar.update();
        }

        this.updateScrolling(isDraggingScrollBar);

        this.confirmButton.update();

        if (this.anyNumber && this.confirmButton.hb.clicked)
        {
            this.confirmButton.hb.clicked = false;
            AbstractDungeon.closeCurrentScreen();
        }
        else
        {
            this.updateCardPositionsAndHoverLogic();
            if (this.hoveredCard != null)
            {
                if (InputHelper.justClickedLeft)
                {
                    this.hoveredCard.hb.clickStarted = true;
                }

                if (this.hoveredCard.hb.clicked || CInputActionSet.select.isJustPressed())
                {
                    this.hoveredCard.hb.clicked = false;

                    if (onCardClicked != null)
                    {
                        onCardClicked.Invoke(hoveredCard);
                    }

//                    if (!this.selectedCards.contains(this.hoveredCard))
//                    {
//                        this.selectedCards.add(this.hoveredCard);
//
//                        this.hoveredCard.targetTransparency = 1;
//                        this.hoveredCard.beginGlowing();
//
//                        this.hoveredCard.targetDrawScale = 0.75F;
//                        this.hoveredCard.drawScale = 0.875F;
//                        ++this.cardSelectAmount;
//                        CardCrawlGame.sound.play("CARD_SELECT");
//
//                        if (this.numCards == this.cardSelectAmount)
//                        {
//                            AbstractDungeon.closeCurrentScreen();
//
//                            if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.SHOP)
//                            {
//                                AbstractDungeon.overlayMenu.cancelButton.hide();
//                            }
//                            else
//                            {
//                                AbstractDungeon.overlayMenu.cancelButton.show(TEXT[3]);
//                            }
//
//                            for (AbstractCard card : this.selectedCards)
//                            {
//                                card.stopGlowing();
//                            }
//
//                            if (this.targetGroup.type == CardGroup.CardGroupType.DISCARD_PILE)
//                            {
//                                for (AbstractCard card : this.targetGroup.group)
//                                {
//                                    card.drawScale = 0.12F;
//                                    card.targetDrawScale = 0.12F;
//                                    card.teleportToDiscardPile();
//                                    card.lighten(true);
//                                }
//                            }
//                        }
//                    }
//                    else if (this.selectedCards.contains(this.hoveredCard))
//                    {
//                        this.hoveredCard.stopGlowing();
//                        this.hoveredCard.targetTransparency = 0.7f;
//
//                        this.selectedCards.remove(this.hoveredCard);
//                        --this.cardSelectAmount;
//                    }
                }
            }
        }
    }

    private void updateCardPositionsAndHoverLogic()
    {
        int lineNum = 0;
        ArrayList<AbstractCard> cards = this.targetGroup.group;
        for (int i = 0; i < cards.size(); ++i)
        {
            int mod = i % 5;
            if (mod == 0 && i != 0)
            {
                ++lineNum;
            }

            AbstractCard card = cards.get(i);

            card.target_x = DRAW_START_X + (float) mod * PAD_X;
            card.target_y = DRAW_START_Y + this.scrollDelta - (float) lineNum * PAD_Y;
            card.fadingOut = false;
            card.update();
            card.updateHoverLogic();
            this.hoveredCard = null;

            for (AbstractCard c : cards)
            {
                if (c.hb.hovered)
                {
                    this.hoveredCard = c;
                }
            }
        }
    }

    public void hide()
    {
        if (!AbstractDungeon.overlayMenu.cancelButton.isHidden)
        {
            this.cancelWasOn = true;
            this.cancelText = AbstractDungeon.overlayMenu.cancelButton.buttonText;
        }
    }

    private void updateScrolling(boolean isDraggingScrollBar)
    {
        int y = InputHelper.mY;

        if (!isDraggingScrollBar)
        {
            if (!this.grabbedScreen)
            {
                if (InputHelper.scrolledDown)
                {
                    this.scrollDelta += Settings.SCROLL_SPEED;
                }
                else if (InputHelper.scrolledUp)
                {
                    this.scrollDelta -= Settings.SCROLL_SPEED;
                }

                if (InputHelper.justClickedLeft)
                {
                    if (GR.Screens.TryDragging())
                    {
                        this.grabbedScreen = true;
                        this.grabStartY = (float) y - this.scrollDelta;
                    }
                }
            }
            else if (InputHelper.isMouseDown)
            {
                if (GR.Screens.TryDragging())
                {
                    this.scrollDelta = (float) y - this.grabStartY;
                }
            }
            else
            {
                this.grabbedScreen = false;
            }
        }

        if (this.prevDeckSize != this.targetGroup.size())
        {
            this.calculateScrollBounds();
        }

        if (this.scrollDelta < this.scrollLowerBound)
        {
            this.scrollDelta = MathHelper.scrollSnapLerpSpeed(this.scrollDelta, this.scrollLowerBound);
        }
        else if (this.scrollDelta > this.scrollUpperBound)
        {
            this.scrollDelta = MathHelper.scrollSnapLerpSpeed(this.scrollDelta, this.scrollUpperBound);
        }

        this.updateBarPosition();
    }

    private void calculateScrollBounds()
    {
        if (this.targetGroup.size() > 10)
        {
            int scrollTmp = this.targetGroup.size() / 5 - 2;
            if (this.targetGroup.size() % 5 != 0)
            {
                ++scrollTmp;
            }

            this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT + (float) scrollTmp * PAD_Y;
        }
        else
        {
            this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        }

        this.prevDeckSize = this.targetGroup.size();
    }

    private void hideCards()
    {
        int lineNum = 0;
        ArrayList<AbstractCard> cards = this.targetGroup.group;

        for (int i = 0; i < cards.size(); ++i)
        {
            AbstractCard card = cards.get(i);

            card.setAngle(0.0F, true);
            int mod = i % 5;
            if (mod == 0 && i != 0)
            {
                ++lineNum;
            }

            card.lighten(true);
            card.current_x = DRAW_START_X + (float) mod * PAD_X;
            card.current_y = DRAW_START_Y + this.scrollDelta - (float) lineNum * PAD_Y - MathUtils.random(100.0F * Settings.scale, 200.0F * Settings.scale);
            card.targetDrawScale = 0.75F;
            card.drawScale = 0.75F;
        }
    }

    public void scrolledUsingBar(float newPercent)
    {
        this.scrollDelta = MathHelper.valueFromPercentBetween(this.scrollLowerBound, this.scrollUpperBound, newPercent);
        this.updateBarPosition();
    }

    private void updateBarPosition()
    {
        float percent = MathHelper.percentFromValueBetween(this.scrollLowerBound, this.scrollUpperBound, this.scrollDelta);
        this.scrollBar.parentScrolledToPercent(percent);
    }

    private boolean shouldShowScrollBar()
    {
        return false;// this.scrollUpperBound > SCROLL_BAR_THRESHOLD;
    }
}
