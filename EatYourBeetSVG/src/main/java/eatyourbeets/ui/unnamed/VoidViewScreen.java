package eatyourbeets.ui.unnamed;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import eatyourbeets.powers.PlayerStatistics;

import java.util.ArrayList;
import java.util.Iterator;

public class VoidViewScreen implements ScrollBarListener
{
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private CardGroup voidCopy;
    public boolean isHovered;
    private static final int CARDS_PER_LINE = 5;
    private boolean grabbedScreen;
    private float grabStartY;
    private float currentDiffY;
    private static float drawStartX;
    private static float drawStartY;
    private static float padX;
    private static float padY;
    private float scrollLowerBound;
    private float scrollUpperBound;
    private static final String DESC;
    private AbstractCard hoveredCard;
    private int prevDeckSize;
    private static final float SCROLL_BAR_THRESHOLD;
    private ScrollBar scrollBar;
    private AbstractCard controllerCard;

    public VoidViewScreen()
    {
        this.voidCopy = new CardGroup(CardGroupType.UNSPECIFIED);
        this.isHovered = false;
        this.grabbedScreen = false;
        this.grabStartY = 0.0F;
        this.currentDiffY = 0.0F;
        this.scrollLowerBound = -Settings.DEFAULT_SCROLL_LIMIT;
        this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        this.hoveredCard = null;
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
        boolean isDraggingScrollBar = false;
        if (this.shouldShowScrollBar())
        {
            isDraggingScrollBar = this.scrollBar.update();
        }

        if (!isDraggingScrollBar)
        {
            this.updateScrolling();
        }

        if (this.voidCopy.group.size() > 0)
        {
            this.updateControllerInput();
        }

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

        this.updatePositions();
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

            for (Iterator var3 = this.voidCopy.group.iterator(); var3.hasNext(); ++index)
            {
                AbstractCard c = (AbstractCard) var3.next();
                if (c.hb.hovered)
                {
                    anyHovered = true;
                    break;
                }
            }

            if (!anyHovered)
            {
                Gdx.input.setCursorPosition((int) this.voidCopy.group.get(0).hb.cX, Settings.HEIGHT - (int) this.voidCopy.group.get(0).hb.cY);
                this.controllerCard = this.voidCopy.group.get(0);
            }
            else if ((CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && this.voidCopy.size() > 5)
            {
                index -= 5;
                if (index < 0)
                {
                    int wrap = this.voidCopy.size() / 5;
                    index += wrap * 5;
                    if (index + 5 < this.voidCopy.size())
                    {
                        index += 5;
                    }
                }

                Gdx.input.setCursorPosition((int) this.voidCopy.group.get(index).hb.cX, Settings.HEIGHT - (int) this.voidCopy.group.get(index).hb.cY);
                this.controllerCard = this.voidCopy.group.get(index);
            }
            else if ((CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) && this.voidCopy.size() > 5)
            {
                if (index < this.voidCopy.size() - 5)
                {
                    index += 5;
                }
                else
                {
                    index %= 5;
                }

                Gdx.input.setCursorPosition((int) this.voidCopy.group.get(index).hb.cX, Settings.HEIGHT - (int) this.voidCopy.group.get(index).hb.cY);
                this.controllerCard = this.voidCopy.group.get(index);
            }
            else if (!CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed())
            {
                if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed())
                {
                    if (index % 5 < 4)
                    {
                        ++index;
                        if (index > this.voidCopy.size() - 1)
                        {
                            index -= this.voidCopy.size() % 5;
                        }
                    }
                    else
                    {
                        index -= 4;
                    }

                    Gdx.input.setCursorPosition((int) this.voidCopy.group.get(index).hb.cX, Settings.HEIGHT - (int) this.voidCopy.group.get(index).hb.cY);
                    this.controllerCard = this.voidCopy.group.get(index);
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
                    if (index > this.voidCopy.size() - 1)
                    {
                        index = this.voidCopy.size() - 1;
                    }
                }

                Gdx.input.setCursorPosition((int) this.voidCopy.group.get(index).hb.cX, Settings.HEIGHT - (int) this.voidCopy.group.get(index).hb.cY);
                this.controllerCard = this.voidCopy.group.get(index);
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

        if (this.prevDeckSize != this.voidCopy.size())
        {
            this.calculateScrollBounds();
        }

        this.resetScrolling();
        this.updateBarPosition();
    }

    private void calculateScrollBounds()
    {
        if (this.voidCopy.size() > 10)
        {
            int scrollTmp = this.voidCopy.size() / 5 - 2;
            if (this.voidCopy.size() % 5 != 0)
            {
                ++scrollTmp;
            }

            this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT + (float) scrollTmp * padY;
        }
        else
        {
            this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        }

        this.prevDeckSize = this.voidCopy.size();
    }

    @SuppressWarnings("SuspiciousNameCombination")
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

    private void updatePositions()
    {
        this.hoveredCard = null;
        int lineNum = 0;
        ArrayList<AbstractCard> cards = this.voidCopy.group;

        for (int i = 0; i < cards.size(); ++i)
        {
            int mod = i % 5;
            if (mod == 0 && i != 0)
            {
                ++lineNum;
            }

            cards.get(i).target_x = drawStartX + (float) mod * padX;
            cards.get(i).target_y = drawStartY + this.currentDiffY - (float) lineNum * padY;
            cards.get(i).update();
            cards.get(i).updateHoverLogic();
            if (cards.get(i).hb.hovered)
            {
                this.hoveredCard = cards.get(i);
            }
        }

    }

    public void reopen()
    {
        AbstractDungeon.overlayMenu.cancelButton.show(TEXT[1]);
    }

    public void open()
    {
        if (PlayerStatistics.Void.isEmpty())
        {
            return;
        }

        CardCrawlGame.sound.play("DECK_OPEN");
        AbstractDungeon.overlayMenu.showBlackScreen();
        AbstractDungeon.overlayMenu.cancelButton.show(TEXT[1]);
        this.currentDiffY = 0.0F;
        this.grabStartY = 0.0F;
        this.grabbedScreen = false;
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = CurrentScreen.NO_INTERACT;
        this.voidCopy.clear();

        for (AbstractCard c : PlayerStatistics.Void.group)
        {
            AbstractCard toAdd = c.makeStatEquivalentCopy();
            toAdd.setAngle(0.0F, true);
            toAdd.targetDrawScale = 0.75F;
            toAdd.drawScale = 0.75F;
            toAdd.lighten(true);
            this.voidCopy.addToBottom(toAdd);
        }

        if (!AbstractDungeon.player.hasRelic("Frozen Eye"))
        {
            this.voidCopy.sortAlphabetically(true);
            this.voidCopy.sortByRarityPlusStatusCardType(true);
        }

        this.hideCards();
        if (this.voidCopy.group.size() <= 5)
        {
            drawStartY = (float) Settings.HEIGHT * 0.5F;
        }
        else
        {
            drawStartY = (float) Settings.HEIGHT * 0.66F;
        }

        this.calculateScrollBounds();
    }

    private void hideCards()
    {
        int lineNum = 0;
        ArrayList<AbstractCard> cards = this.voidCopy.group;

        for (int i = 0; i < cards.size(); ++i)
        {
            int mod = i % 5;
            if (mod == 0 && i != 0)
            {
                ++lineNum;
            }

            cards.get(i).current_x = drawStartX + (float) mod * padX;
            cards.get(i).current_y = drawStartY + this.currentDiffY - (float) lineNum * padY - MathUtils.random(100.0F * Settings.scale, 200.0F * Settings.scale);
            cards.get(i).targetDrawScale = 0.75F;
            cards.get(i).drawScale = 0.75F;
        }

    }

    public void render(SpriteBatch sb)
    {
        if (this.hoveredCard == null)
        {
            this.voidCopy.render(sb);
        }
        else
        {
            this.voidCopy.renderExceptOneCard(sb, this.hoveredCard);
            this.hoveredCard.renderHoverShadow(sb);
            this.hoveredCard.render(sb);
            this.hoveredCard.renderCardTip(sb);
        }

        FontHelper.renderDeckViewTip(sb, DESC, 96.0F * Settings.scale, Settings.CREAM_COLOR);
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
        uiStrings = CardCrawlGame.languagePack.getUIString("ExhaustViewScreen");
        TEXT = uiStrings.TEXT;
        DESC = TEXT[0];
        SCROLL_BAR_THRESHOLD = 500.0F * Settings.scale;
    }
}