package eatyourbeets.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.GameCursor.CursorType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;

import java.util.ArrayList;
import java.util.Iterator;

public class PurgingStoneScreen implements ScrollBarListener
{
    public CardGroup cardGroup;

    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private static float drawStartX;
    private static float drawStartY;
    private static float padX;
    private static float padY;
    private static final float SCROLL_BAR_THRESHOLD;
    private static final int CARDS_PER_LINE = 5;
    private boolean grabbedScreen = false;
    private float grabStartY = 0.0F;
    private float currentDiffY = 0.0F;
    private float scrollLowerBound;
    private float scrollUpperBound;
    private static final String HEADER_INFO;
    private AbstractCard hoveredCard;
    private AbstractCard clickStartedCard;
    private int prevDeckSize;
    private ScrollBar scrollBar;
    private AbstractCard controllerCard;

    public PurgingStoneScreen()
    {
        this.scrollLowerBound = -Settings.DEFAULT_SCROLL_LIMIT;
        this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        this.hoveredCard = null;
        this.clickStartedCard = null;
        this.prevDeckSize = 0;
        this.controllerCard = null;
        drawStartX = (float) Settings.WIDTH;
        drawStartX -= 5.0F * AbstractCard.IMG_WIDTH * 0.75F;
        drawStartX -= 4.0F * Settings.CARD_VIEW_PAD_X;
        drawStartX /= 2.0F;
        drawStartX += AbstractCard.IMG_WIDTH * 0.75F / 2.0F;
        padX = AbstractCard.IMG_WIDTH * 0.75F + Settings.CARD_VIEW_PAD_X;
        padY = AbstractCard.IMG_HEIGHT * 0.75F + Settings.CARD_VIEW_PAD_Y;
        this.scrollBar = new ScrollBar(this);
        this.scrollBar.move(0.0F, -30.0F * Settings.scale);
    }

    public void update()
    {
        this.updateControllerInput();
        if (Settings.isControllerMode && this.controllerCard != null && !CardCrawlGame.isPopupOpen && !AbstractDungeon.topPanel.selectPotionMode)
        {
            if ((float) Gdx.input.getY() > (float) Settings.HEIGHT * 0.7F)
            {
                this.currentDiffY += Settings.SCROLL_SPEED;
            }
            else if ((float) Gdx.input.getY() < (float) Settings.HEIGHT * 0.3F)
            {
                this.currentDiffY -= Settings.SCROLL_SPEED;
            }
        }

        boolean isDraggingScrollBar = false;
        if (this.shouldShowScrollBar())
        {
            isDraggingScrollBar = this.scrollBar.update();
        }

        if (!isDraggingScrollBar)
        {
            this.updateScrolling();
        }

        this.updatePositions();
        this.updateClicking();
        if (Settings.isControllerMode && this.controllerCard != null)
        {
            Gdx.input.setCursorPosition((int) this.controllerCard.hb.cX, (int) ((float) Settings.HEIGHT - this.controllerCard.hb.cY));
        }

    }

    private void updateControllerInput()
    {
        if (Settings.isControllerMode && !AbstractDungeon.topPanel.selectPotionMode)
        {
            boolean anyHovered = false;
            int index = 0;

            for (Iterator var4 = cardGroup.group.iterator(); var4.hasNext(); ++index)
            {
                AbstractCard c = (AbstractCard) var4.next();
                if (c.hb.hovered)
                {
                    anyHovered = true;
                    break;
                }
            }

            if (!anyHovered)
            {
                Gdx.input.setCursorPosition((int) ((AbstractCard) cardGroup.group.get(0)).hb.cX, (int) ((AbstractCard) cardGroup.group.get(0)).hb.cY);
                this.controllerCard = (AbstractCard) cardGroup.group.get(0);
            }
            else if ((CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && cardGroup.size() > 5)
            {
                index -= 5;
                if (index < 0)
                {
                    int wrap = cardGroup.size() / 5;
                    index += wrap * 5;
                    if (index + 5 < cardGroup.size())
                    {
                        index += 5;
                    }
                }

                Gdx.input.setCursorPosition((int) ((AbstractCard) cardGroup.group.get(index)).hb.cX, Settings.HEIGHT - (int) ((AbstractCard) cardGroup.group.get(index)).hb.cY);
                this.controllerCard = (AbstractCard) cardGroup.group.get(index);
            }
            else if ((CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) && cardGroup.size() > 5)
            {
                if (index < cardGroup.size() - 5)
                {
                    index += 5;
                }
                else
                {
                    index %= 5;
                }

                Gdx.input.setCursorPosition((int) ((AbstractCard) cardGroup.group.get(index)).hb.cX, Settings.HEIGHT - (int) ((AbstractCard) cardGroup.group.get(index)).hb.cY);
                this.controllerCard = (AbstractCard) cardGroup.group.get(index);
            }
            else if (!CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed())
            {
                if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed())
                {
                    if (index % 5 < 4)
                    {
                        ++index;
                        if (index > cardGroup.size() - 1)
                        {
                            index -= cardGroup.size() % 5;
                        }
                    }
                    else
                    {
                        index -= 4;
                        if (index < 0)
                        {
                            index = 0;
                        }
                    }

                    Gdx.input.setCursorPosition((int) ((AbstractCard) cardGroup.group.get(index)).hb.cX, Settings.HEIGHT - (int) ((AbstractCard) cardGroup.group.get(index)).hb.cY);
                    this.controllerCard = (AbstractCard) cardGroup.group.get(index);
                }
            }
            else
            {
                if (index % 5 > 0)
                {
                    --index;
                }
                else
                {
                    index += 4;
                    if (index > cardGroup.size() - 1)
                    {
                        index = cardGroup.size() - 1;
                    }
                }

                Gdx.input.setCursorPosition((int) ((AbstractCard) cardGroup.group.get(index)).hb.cX, Settings.HEIGHT - (int) ((AbstractCard) cardGroup.group.get(index)).hb.cY);
                this.controllerCard = (AbstractCard) cardGroup.group.get(index);
            }

        }
    }

    public void open()
    {
        if (Settings.isControllerMode)
        {
            Gdx.input.setCursorPosition(10, Settings.HEIGHT / 2);
            this.controllerCard = null;
        }

        AbstractDungeon.player.releaseCard();
        CardCrawlGame.sound.play("DECK_OPEN");
        this.currentDiffY = this.scrollLowerBound;
        this.grabStartY = this.scrollLowerBound;
        this.grabbedScreen = false;
        this.hideCards();
        AbstractDungeon.dynamicBanner.hide();
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = CurrentScreen.MASTER_DECK_VIEW;
        AbstractDungeon.overlayMenu.proceedButton.hide();
        AbstractDungeon.overlayMenu.hideCombatPanels();
        AbstractDungeon.overlayMenu.showBlackScreen();
        AbstractDungeon.overlayMenu.cancelButton.show(TEXT[1]);
        this.calculateScrollBounds();
    }

    private void updatePositions()
    {
        this.hoveredCard = null;
        int lineNum = 0;
        ArrayList<AbstractCard> cards = cardGroup.group;

        for (int i = 0; i < cards.size(); ++i)
        {
            int mod = i % 5;
            if (mod == 0 && i != 0)
            {
                ++lineNum;
            }

            ((AbstractCard) cards.get(i)).target_x = drawStartX + (float) mod * padX;
            ((AbstractCard) cards.get(i)).target_y = drawStartY + this.currentDiffY - (float) lineNum * padY;
            ((AbstractCard) cards.get(i)).update();
            ((AbstractCard) cards.get(i)).updateHoverLogic();
            if (((AbstractCard) cards.get(i)).hb.hovered)
            {
                this.hoveredCard = (AbstractCard) cards.get(i);
            }
        }

    }

    private void updateScrolling()
    {
        int y = InputHelper.mY;
        if (!this.grabbedScreen)
        {
            if (InputHelper.scrolledDown)
            {
                this.currentDiffY += Settings.SCROLL_SPEED;
            }
            else if (InputHelper.scrolledUp)
            {
                this.currentDiffY -= Settings.SCROLL_SPEED;
            }

            if (InputHelper.justClickedLeft)
            {
                this.grabbedScreen = true;
                this.grabStartY = (float) y - this.currentDiffY;
            }
        }
        else if (InputHelper.isMouseDown)
        {
            this.currentDiffY = (float) y - this.grabStartY;
        }
        else
        {
            this.grabbedScreen = false;
        }

        if (this.prevDeckSize != cardGroup.size())
        {
            this.calculateScrollBounds();
        }

        this.resetScrolling();
        this.updateBarPosition();
    }

    private void updateClicking()
    {
        if (this.hoveredCard != null)
        {
            CardCrawlGame.cursor.changeType(CursorType.INSPECT);
            if (InputHelper.justClickedLeft)
            {
                this.clickStartedCard = this.hoveredCard;
            }

            if (InputHelper.justReleasedClickLeft && this.hoveredCard == this.clickStartedCard || CInputActionSet.select.isJustPressed())
            {
                InputHelper.justReleasedClickLeft = false;
                CardCrawlGame.cardPopup.open(this.hoveredCard, cardGroup);
                this.clickStartedCard = null;
            }
        }
        else
        {
            this.clickStartedCard = null;
        }

    }

    private void calculateScrollBounds()
    {
        if (cardGroup.size() > 10)
        {
            int scrollTmp = cardGroup.size() / 5 - 2;
            if (AbstractDungeon.player.masterDeck.size() % 5 != 0)
            {
                ++scrollTmp;
            }

            this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT + (float) scrollTmp * padY;
        }
        else
        {
            this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        }

        this.prevDeckSize = cardGroup.size();
    }

    private void resetScrolling()
    {
        if (this.currentDiffY < this.scrollLowerBound)
        {
            this.currentDiffY = MathHelper.scrollSnapLerpSpeed(this.currentDiffY, this.scrollLowerBound);
        }
        else if (this.currentDiffY > this.scrollUpperBound)
        {
            this.currentDiffY = MathHelper.scrollSnapLerpSpeed(this.currentDiffY, this.scrollUpperBound);
        }

    }

    private void hideCards()
    {
        int lineNum = 0;
        ArrayList<AbstractCard> cards = cardGroup.group;

        for (int i = 0; i < cards.size(); ++i)
        {
            int mod = i % 5;
            if (mod == 0 && i != 0)
            {
                ++lineNum;
            }

            ((AbstractCard) cards.get(i)).current_x = drawStartX + (float) mod * padX;
            ((AbstractCard) cards.get(i)).current_y = drawStartY + this.currentDiffY - (float) lineNum * padY - MathUtils.random(100.0F * Settings.scale, 200.0F * Settings.scale);
            ((AbstractCard) cards.get(i)).targetDrawScale = 0.75F;
            ((AbstractCard) cards.get(i)).drawScale = 0.75F;
            ((AbstractCard) cards.get(i)).setAngle(0.0F, true);
        }

    }

    public void render(SpriteBatch sb)
    {
        if (this.hoveredCard == null)
        {
            cardGroup.renderMasterDeck(sb);
        }
        else
        {
            cardGroup.renderMasterDeckExceptOneCard(sb, this.hoveredCard);
            this.hoveredCard.renderHoverShadow(sb);
            this.hoveredCard.render(sb);
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
                tmp = null;
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
                tmp = null;
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
                tmp = null;
            }
        }

        cardGroup.renderTip(sb);
        FontHelper.renderDeckViewTip(sb, HEADER_INFO, 96.0F * Settings.scale, Settings.CREAM_COLOR);
        if (this.shouldShowScrollBar())
        {
            this.scrollBar.render(sb);
        }

    }

    public void scrolledUsingBar(float newPercent)
    {
        this.currentDiffY = MathHelper.valueFromPercentBetween(this.scrollLowerBound, this.scrollUpperBound, newPercent);
        this.updateBarPosition();
    }

    private void updateBarPosition()
    {
        float percent = MathHelper.percentFromValueBetween(this.scrollLowerBound, this.scrollUpperBound, this.currentDiffY);
        this.scrollBar.parentScrolledToPercent(percent);
    }

    private boolean shouldShowScrollBar()
    {
        return this.scrollUpperBound > SCROLL_BAR_THRESHOLD;
    }

    static
    {
        uiStrings = CardCrawlGame.languagePack.getUIString("MasterDeckViewScreen");
        TEXT = uiStrings.TEXT;
        drawStartY = (float) Settings.HEIGHT * 0.66F;
        SCROLL_BAR_THRESHOLD = 500.0F * Settings.scale;
        HEADER_INFO = TEXT[0];
    }
}
