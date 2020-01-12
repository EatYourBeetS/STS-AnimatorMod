package eatyourbeets.screens.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
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
import eatyourbeets.screens.AbstractScreen;

import java.util.ArrayList;
import java.util.Iterator;

public class GridSelectionScreen extends AbstractScreen implements ScrollBarListener
{
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    private static final float DRAW_START_X;
    private static final float DRAW_START_Y;
    private static final float PAD_X;
    private static final float PAD_Y;

    private static final int CARDS_PER_LINE = 5;
    private static final float SCROLL_BAR_THRESHOLD;
    private float grabStartY = 0.0F;
    private float currentDiffY = 0.0F;
    public ArrayList<AbstractCard> selectedCards = new ArrayList<>();
    private CardGroup targetGroup;
    private AbstractCard hoveredCard = null;
    private int numCards = 0;
    private int cardSelectAmount = 0;
    private float scrollLowerBound;
    private float scrollUpperBound;
    private boolean grabbedScreen;
    private boolean canCancel;
    public boolean confirmScreenUp;
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
        DRAW_START_Y = (float) Settings.HEIGHT * 0.66F;
        DRAW_START_X = (Settings.WIDTH - (5.0F * AbstractCard.IMG_WIDTH * 0.75F) - (4.0F * Settings.CARD_VIEW_PAD_X) + AbstractCard.IMG_WIDTH * 0.75F) / 2f;
        PAD_X = AbstractCard.IMG_WIDTH * 0.75F + Settings.CARD_VIEW_PAD_X;
        PAD_Y = AbstractCard.IMG_HEIGHT * 0.75F + Settings.CARD_VIEW_PAD_Y;
    }

    public GridSelectionScreen()
    {
        this.scrollLowerBound = -Settings.DEFAULT_SCROLL_LIMIT;
        this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        this.grabbedScreen = false;
        this.canCancel = true;
        this.confirmScreenUp = false;
        this.confirmButton = new GridSelectConfirmButton(TEXT[0]);
        this.tipMsg = "";
        this.prevDeckSize = 0;
        this.cancelWasOn = false;
        this.anyNumber = false;
        this.scrollBar = new ScrollBar(this);
        this.scrollBar.move(0.0F, -30.0F * Settings.scale);
    }

    @Override
    public void Update()
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

        this.confirmButton.update();
        Iterator var2;
        AbstractCard c;
        if (this.anyNumber && this.confirmButton.hb.clicked)
        {
            this.confirmButton.hb.clicked = false;
            AbstractDungeon.closeCurrentScreen();
        }
        else
        {
            if (!this.confirmScreenUp)
            {
                this.updateCardPositionsAndHoverLogic();
                if (this.hoveredCard != null && InputHelper.justClickedLeft)
                {
                    this.hoveredCard.hb.clickStarted = true;
                }

                if (this.hoveredCard != null && (this.hoveredCard.hb.clicked || CInputActionSet.select.isJustPressed()))
                {
                    this.hoveredCard.hb.clicked = false;
                    if (!this.selectedCards.contains(this.hoveredCard))
                    {
                        this.selectedCards.add(this.hoveredCard);
                        this.hoveredCard.beginGlowing();
                        this.hoveredCard.targetDrawScale = 0.75F;
                        this.hoveredCard.drawScale = 0.875F;
                        ++this.cardSelectAmount;
                        CardCrawlGame.sound.play("CARD_SELECT");
                        if (this.numCards == this.cardSelectAmount)
                        {

                            AbstractDungeon.closeCurrentScreen();
                            if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.SHOP)
                            {
                                AbstractDungeon.overlayMenu.cancelButton.hide();
                            }
                            else
                            {
                                AbstractDungeon.overlayMenu.cancelButton.show(TEXT[3]);
                            }

                            var2 = this.selectedCards.iterator();

                            while (var2.hasNext())
                            {
                                c = (AbstractCard) var2.next();
                                c.stopGlowing();
                            }

                            if (this.targetGroup.type == CardGroup.CardGroupType.DISCARD_PILE)
                            {
                                var2 = this.targetGroup.group.iterator();

                                while (var2.hasNext())
                                {
                                    c = (AbstractCard) var2.next();
                                    c.drawScale = 0.12F;
                                    c.targetDrawScale = 0.12F;
                                    c.teleportToDiscardPile();
                                    c.lighten(true);
                                }
                            }
                        }
                    }
                    else if (this.selectedCards.contains(this.hoveredCard))
                    {
                        this.hoveredCard.stopGlowing();
                        this.selectedCards.remove(this.hoveredCard);
                        --this.cardSelectAmount;
                    }
                }
            }
            else
            {

                if (this.confirmButton.hb.clicked || CInputActionSet.topPanel.isJustPressed())
                {
                    CInputActionSet.select.unpress();
                    this.confirmButton.hb.clicked = false;
                    AbstractDungeon.overlayMenu.cancelButton.hide();
                    this.confirmScreenUp = false;
                    this.selectedCards.add(this.hoveredCard);
                    AbstractDungeon.closeCurrentScreen();
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
            card.target_y = DRAW_START_Y + this.currentDiffY - (float) lineNum * PAD_Y;
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

    public void open(CardGroup group, int numCards, String tipMsg, boolean canCancel)
    {
        this.targetGroup = group;
        this.callOnOpen();
        this.canCancel = canCancel;
        this.tipMsg = tipMsg;
        this.numCards = numCards;

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

    private void callOnOpen()
    {
        super.Open();

        this.anyNumber = false;
        this.canCancel = false;
        this.confirmScreenUp = false;
        this.hoveredCard = null;
        this.selectedCards.clear();
        this.cardSelectAmount = 0;
        this.currentDiffY = 0.0F;
        this.grabStartY = 0.0F;
        this.grabbedScreen = false;
        this.hideCards();
        this.confirmButton.hideInstantly();
    }

    public void hide()
    {
        if (!AbstractDungeon.overlayMenu.cancelButton.isHidden)
        {
            this.cancelWasOn = true;
            this.cancelText = AbstractDungeon.overlayMenu.cancelButton.buttonText;
        }
    }

    private void updateScrolling()
    {
        int y = InputHelper.mY;
        boolean isDraggingScrollBar = this.scrollBar.update();
        if (!isDraggingScrollBar)
        {
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
        }

        if (this.prevDeckSize != this.targetGroup.size())
        {
            this.calculateScrollBounds();
        }

        this.resetScrolling();
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
            card.current_y = DRAW_START_Y + this.currentDiffY - (float) lineNum * PAD_Y - MathUtils.random(100.0F * Settings.scale, 200.0F * Settings.scale);
            card.targetDrawScale = 0.75F;
            card.drawScale = 0.75F;
        }
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

        if (this.confirmScreenUp)
        {
            sb.setColor(new Color(0.0F, 0.0F, 0.0F, 0.8F));
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float) Settings.WIDTH, (float) Settings.HEIGHT - 64.0F * Settings.scale);

            this.hoveredCard.current_x = (float) Settings.WIDTH / 2.0F;
            this.hoveredCard.current_y = (float) Settings.HEIGHT / 2.0F;
            this.hoveredCard.render(sb);
            this.hoveredCard.updateHoverLogic();

        }

        if (this.anyNumber)
        {
            this.confirmButton.render(sb);
        }

        FontHelper.renderDeckViewTip(sb, this.tipMsg, 96.0F * Settings.scale, Settings.CREAM_COLOR);
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
        return !this.confirmScreenUp && this.scrollUpperBound > SCROLL_BAR_THRESHOLD;
    }
}
