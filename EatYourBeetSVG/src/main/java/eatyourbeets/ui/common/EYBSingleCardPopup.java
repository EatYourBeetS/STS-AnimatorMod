package eatyourbeets.ui.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

// TODO: Improve this
public class EYBSingleCardPopup extends GUIElement
{
    private AbstractCard card;
    private AbstractCard upgradedCard;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private CardGroup group;
    private AbstractCard prevCard;
    private AbstractCard nextCard;
    private Texture portraitImg = null;
    private Hitbox nextHb;
    private Hitbox prevHb;
    private Hitbox cardHb;
    private float fadeTimer = 0.0F;
    private Color fadeColor;
    private static final float LINE_SPACING = 1.53F;
    private static final float DESC_OFFSET_Y2 = -12.0F;
    private static final Color CARD_TYPE_COLOR;
    private static final GlyphLayout gl;
    public static boolean isViewingUpgrade;
    public static boolean enableUpgradeToggle;
    private Hitbox upgradeHb;
    private Hitbox betaArtHb;
    private boolean viewBetaArt;

    public EYBSingleCardPopup()
    {
        this.fadeColor = Color.BLACK.cpy();
        this.upgradeHb = new Hitbox(250.0F * Settings.scale, 80.0F * Settings.scale);
        this.betaArtHb = null;
        this.viewBetaArt = false;
        this.prevHb = new Hitbox(200.0F * Settings.scale, 70.0F * Settings.scale);
        this.nextHb = new Hitbox(200.0F * Settings.scale, 70.0F * Settings.scale);
        this.isActive = false;
    }

    public void Open(AbstractCard card)
    {
        Open(card, null);
    }

    public void Open(AbstractCard card, CardGroup group)
    {
        CardCrawlGame.isPopupOpen = true;

        if (group == null)
        {
            this.prevCard = null;
            this.nextCard = null;
            this.prevHb = null;
            this.nextHb = null;
        }
        else
        {
            for (int i = 0; i < group.size(); ++i)
            {
                if (group.group.get(i) == card)
                {
                    if (i != 0)
                    {
                        this.prevCard = group.group.get(i - 1);
                    }

                    if (i != group.size() - 1)
                    {
                        this.nextCard = group.group.get(i + 1);
                    }
                    break;
                }
            }

            this.prevHb = new Hitbox(160.0F * Settings.scale, 160.0F * Settings.scale);
            this.nextHb = new Hitbox(160.0F * Settings.scale, 160.0F * Settings.scale);
            this.prevHb.move((float) Settings.WIDTH / 2.0F - 400.0F * Settings.scale, (float) Settings.HEIGHT / 2.0F);
            this.nextHb.move((float) Settings.WIDTH / 2.0F + 400.0F * Settings.scale, (float) Settings.HEIGHT / 2.0F);
        }

        this.group = group;
        this.cardHb = new Hitbox(550.0F * Settings.scale, 770.0F * Settings.scale);
        this.cardHb.move((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F);
        this.card = card.makeStatEquivalentCopy();
        this.upgradedCard = null;
        this.loadPortraitImg();

        this.isActive = true;
        this.fadeTimer = 0.25F;
        this.fadeColor.a = 0.0F;
        this.card.current_x = (float) Settings.WIDTH / 2.0F;
        this.card.current_y = (float) Settings.HEIGHT / 2.0F;
        this.card.drawScale = this.card.targetDrawScale = 2f;

        if (canToggleBetaArt())
        {
            if (allowUpgradePreview())
            {
                this.betaArtHb = new Hitbox(250.0F * Settings.scale, 80.0F * Settings.scale);
                this.betaArtHb.move((float) Settings.WIDTH / 2.0F + 270.0F * Settings.scale, 70.0F * Settings.scale);
                this.upgradeHb.move((float) Settings.WIDTH / 2.0F - 180.0F * Settings.scale, 70.0F * Settings.scale);
            }
            else
            {
                this.betaArtHb = new Hitbox(250.0F * Settings.scale, 80.0F * Settings.scale);
                this.betaArtHb.move((float) Settings.WIDTH / 2.0F, 70.0F * Settings.scale);
            }

            this.viewBetaArt = UnlockTracker.betaCardPref.getBoolean(card.cardID, false);
        }
        else
        {
            this.upgradeHb.move((float) Settings.WIDTH / 2.0F, 70.0F * Settings.scale);
            this.betaArtHb = null;
        }
    }

    public AbstractCard GetCard()
    {
        if (isViewingUpgrade)
        {
            if (upgradedCard == null)
            {
                upgradedCard = card.makeStatEquivalentCopy();
                upgradedCard.upgrade();
                upgradedCard.current_x = card.current_x;
                upgradedCard.current_y = card.current_y;
                upgradedCard.drawScale = card.drawScale;
                upgradedCard.displayUpgrades();
            }

            return upgradedCard;
        }
        else
        {
            return card;
        }
    }

    private boolean canToggleBetaArt()
    {
        if (UnlockTracker.isAchievementUnlocked("THE_ENDING"))
        {
            return true;
        }
        else
        {
            switch (this.card.color)
            {
                case RED:
                    return UnlockTracker.isAchievementUnlocked("RUBY_PLUS");
                case GREEN:
                    return UnlockTracker.isAchievementUnlocked("EMERALD_PLUS");
                case BLUE:
                    return UnlockTracker.isAchievementUnlocked("SAPPHIRE_PLUS");
                case PURPLE:
                    return UnlockTracker.isAchievementUnlocked("AMETHYST_PLUS");
                default:
                    return false;
            }
        }
    }

    private void loadPortraitImg()
    {
        if (!Settings.PLAYTESTER_ART_MODE && !UnlockTracker.betaCardPref.getBoolean(this.card.cardID, false))
        {
            this.portraitImg = ImageMaster.loadImage("images/1024Portraits/" + this.card.assetUrl + "_p.png");
            if (this.portraitImg == null)
            {
                this.portraitImg = ImageMaster.loadImage("images/1024PortraitsBeta/" + this.card.assetUrl + "_p.png");
            }
        }
        else
        {
            this.portraitImg = ImageMaster.loadImage("images/1024PortraitsBeta/" + this.card.assetUrl + "_p.png");
        }
    }

    public void close()
    {
        isViewingUpgrade = false;
        InputHelper.justReleasedClickLeft = false;
        CardCrawlGame.isPopupOpen = false;
        this.isActive = false;
        if (this.portraitImg != null)
        {
            this.portraitImg.dispose();
            this.portraitImg = null;
        }
    }

    @Override
    public void Update()
    {
        this.cardHb.update();
        this.updateArrows();
        this.updateInput();
        this.updateFade();
        if (this.allowUpgradePreview())
        {
            this.updateUpgradePreview();
        }

        if (this.betaArtHb != null && this.canToggleBetaArt())
        {
            this.updateBetaArtToggler();
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
//        AbstractCard copy = null;
//        if (isViewingUpgrade)
//        {
//            copy = this.card.makeStatEquivalentCopy();
//            this.card.upgrade();
//            this.card.displayUpgrades();
//        }

        sb.setColor(this.fadeColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float) Settings.WIDTH, (float) Settings.HEIGHT);
        sb.setColor(Color.WHITE);
//        this.renderCardBack(sb);
//        this.renderPortrait(sb);
//        this.renderFrame(sb);
//        this.renderCardBanner(sb);
//        this.renderCardTypeText(sb);
//        if (Settings.lineBreakViaCharacter)
//        {
//            this.renderDescriptionCN(sb);
//        }
//        else
//        {
//            this.renderDescription(sb);
//        }
//
//        this.renderTitle(sb);
//        this.renderCost(sb);

        GetCard().renderInLibrary(sb);

        this.renderArrows(sb);
        //this.card.renderCardTip(sb);
        //this.renderTips(sb);
        this.cardHb.render(sb);
        if (this.nextHb != null)
        {
            this.nextHb.render(sb);
        }

        if (this.prevHb != null)
        {
            this.prevHb.render(sb);
        }

        FontHelper.cardTitleFont.getData().setScale(1.0F);
        if (this.canToggleBetaArt())
        {
            this.renderBetaArtToggle(sb);
        }

        if (this.allowUpgradePreview())
        {
            this.renderUpgradeViewToggle(sb);
            if (Settings.isControllerMode)
            {
                sb.draw(CInputActionSet.proceed.getKeyImg(), this.upgradeHb.cX - 132.0F * Settings.scale - 32.0F, -32.0F + 67.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            }
        }

        if (this.betaArtHb != null && Settings.isControllerMode)
        {
            sb.draw(CInputActionSet.topPanel.getKeyImg(), this.betaArtHb.cX - 132.0F * Settings.scale - 32.0F, -32.0F + 67.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        }

//        if (copy != null)
//        {
//            this.card = copy;
//        }

    }

    private void renderTips(SpriteBatch sb)
    {
        ArrayList<PowerTip> t = new ArrayList<>();
        if (this.card.isLocked)
        {
            t.add(new PowerTip(TEXT[4], GameDictionary.keywords.get(TEXT[4].toLowerCase())));
        }
        else if (!this.card.isSeen)
        {
            t.add(new PowerTip(TEXT[5], GameDictionary.keywords.get(TEXT[5].toLowerCase())));
        }
        else
        {
            for (String s : this.card.keywords)
            {
                if (!s.equals("[R]") && !s.equals("[G]") && !s.equals("[B]") && !s.equals("[W]"))
                {
                    t.add(new PowerTip(TipHelper.capitalize(s), GameDictionary.keywords.get(s)));
                }
            }
        }

        if (!t.isEmpty())
        {
            TipHelper.queuePowerTips(1300.0F * Settings.scale, 420.0F * Settings.scale, t);
        }

        if (this.card.cardsToPreview != null)
        {
            this.card.renderCardPreviewInSingleView(sb);
        }

    }

    private void updateBetaArtToggler()
    {
        this.betaArtHb.update();
        if (this.betaArtHb.hovered && InputHelper.justClickedLeft)
        {
            this.betaArtHb.clickStarted = true;
        }

        if (this.betaArtHb.clicked || CInputActionSet.topPanel.isJustPressed())
        {
            CInputActionSet.topPanel.unpress();
            this.betaArtHb.clicked = false;
            this.viewBetaArt = !this.viewBetaArt;
            UnlockTracker.betaCardPref.putBoolean(this.card.cardID, this.viewBetaArt);
            UnlockTracker.betaCardPref.flush();
            if (this.portraitImg != null)
            {
                this.portraitImg.dispose();
            }

            this.loadPortraitImg();
        }

    }

    private void updateUpgradePreview()
    {
        this.upgradeHb.update();
        if (this.upgradeHb.hovered && InputHelper.justClickedLeft)
        {
            this.upgradeHb.clickStarted = true;
        }

        if (this.upgradeHb.clicked || CInputActionSet.proceed.isJustPressed())
        {
            CInputActionSet.proceed.unpress();
            this.upgradeHb.clicked = false;
            isViewingUpgrade = !isViewingUpgrade;
        }

    }

    private boolean allowUpgradePreview()
    {
        return enableUpgradeToggle && card.canUpgrade();
    }

    private void updateArrows()
    {
        if (this.prevCard != null)
        {
            this.prevHb.update();
            if (this.prevHb.justHovered)
            {
                CardCrawlGame.sound.play("UI_HOVER");
            }

            if (this.prevHb.clicked || this.prevCard != null && CInputActionSet.pageLeftViewDeck.isJustPressed())
            {
                CInputActionSet.pageLeftViewDeck.unpress();
                this.openPrev();
            }
        }

        if (this.nextCard != null)
        {
            this.nextHb.update();
            if (this.nextHb.justHovered)
            {
                CardCrawlGame.sound.play("UI_HOVER");
            }

            if (this.nextHb.clicked || this.nextCard != null && CInputActionSet.pageRightViewExhaust.isJustPressed())
            {
                CInputActionSet.pageRightViewExhaust.unpress();
                this.openNext();
            }
        }

    }

    private void updateInput()
    {
        if (InputHelper.justClickedLeft)
        {
            if (this.prevCard != null && this.prevHb.hovered)
            {
                this.prevHb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
                return;
            }

            if (this.nextCard != null && this.nextHb.hovered)
            {
                this.nextHb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
                return;
            }
        }

        if (InputHelper.justClickedLeft)
        {
            if (!this.cardHb.hovered && !this.upgradeHb.hovered && (this.betaArtHb == null || !this.betaArtHb.hovered))
            {
                JavaUtilities.Log(this, "Closing");
                this.close();
                InputHelper.justClickedLeft = false;
            }
        }
        else if (InputHelper.pressedEscape || CInputActionSet.cancel.isJustPressed())
        {
            CInputActionSet.cancel.unpress();
            InputHelper.pressedEscape = false;
            this.close();
        }

        if (this.prevCard != null && InputActionSet.left.isJustPressed())
        {
            this.openPrev();
        }
        else if (this.nextCard != null && InputActionSet.right.isJustPressed())
        {
            this.openNext();
        }
    }

    private void openPrev()
    {
        boolean tmp = isViewingUpgrade;
        this.close();
        this.Open(this.prevCard, this.group);
        isViewingUpgrade = tmp;
        this.fadeTimer = 0.0F;
        this.fadeColor.a = 0.9F;
    }

    private void openNext()
    {
        boolean tmp = isViewingUpgrade;
        this.close();
        this.Open(this.nextCard, this.group);
        isViewingUpgrade = tmp;
        this.fadeTimer = 0.0F;
        this.fadeColor.a = 0.9F;
    }

    private void updateFade()
    {
        this.fadeTimer -= Gdx.graphics.getDeltaTime();
        if (this.fadeTimer < 0.0F)
        {
            this.fadeTimer = 0.0F;
        }

        this.fadeColor.a = Interpolation.pow2In.apply(0.9F, 0.0F, this.fadeTimer * 4.0F);
    }

    private void renderHelper(SpriteBatch sb, float x, float y, TextureAtlas.AtlasRegion img)
    {
        if (img != null)
        {
            sb.draw(img, x + img.offsetX - (float) img.originalWidth / 2.0F, y + img.offsetY - (float) img.originalHeight / 2.0F, (float) img.originalWidth / 2.0F - img.offsetX, (float) img.originalHeight / 2.0F - img.offsetY, (float) img.packedWidth, (float) img.packedHeight, Settings.scale, Settings.scale, 0.0F);
        }
    }

    private void renderArrows(SpriteBatch sb)
    {
        if (this.prevCard != null)
        {
            sb.draw(ImageMaster.POPUP_ARROW, this.prevHb.cX - 128.0F, this.prevHb.cY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, false, false);
            if (Settings.isControllerMode)
            {
                sb.draw(CInputActionSet.pageLeftViewDeck.getKeyImg(), this.prevHb.cX - 32.0F + 0.0F * Settings.scale, this.prevHb.cY - 32.0F + 100.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            }

            if (this.prevHb.hovered)
            {
                sb.setBlendFunction(770, 1);
                sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.5F));
                sb.draw(ImageMaster.POPUP_ARROW, this.prevHb.cX - 128.0F, this.prevHb.cY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, false, false);
                sb.setColor(Color.WHITE);
                sb.setBlendFunction(770, 771);
            }
        }

        if (this.nextCard != null)
        {
            sb.draw(ImageMaster.POPUP_ARROW, this.nextHb.cX - 128.0F, this.nextHb.cY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, true, false);
            if (Settings.isControllerMode)
            {
                sb.draw(CInputActionSet.pageRightViewExhaust.getKeyImg(), this.nextHb.cX - 32.0F + 0.0F * Settings.scale, this.nextHb.cY - 32.0F + 100.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            }

            if (this.nextHb.hovered)
            {
                sb.setBlendFunction(770, 1);
                sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.5F));
                sb.draw(ImageMaster.POPUP_ARROW, this.nextHb.cX - 128.0F, this.nextHb.cY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, true, false);
                sb.setColor(Color.WHITE);
                sb.setBlendFunction(770, 771);
            }
        }

    }

    private void renderBetaArtToggle(SpriteBatch sb)
    {
        if (this.betaArtHb != null)
        {
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.CHECKBOX, this.betaArtHb.cX - 80.0F * Settings.scale - 32.0F, this.betaArtHb.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            if (this.betaArtHb.hovered)
            {
                FontHelper.renderFont(sb, FontHelper.cardTitleFont, TEXT[14], this.betaArtHb.cX - 45.0F * Settings.scale, this.betaArtHb.cY + 10.0F * Settings.scale, Settings.BLUE_TEXT_COLOR);
            }
            else
            {
                FontHelper.renderFont(sb, FontHelper.cardTitleFont, TEXT[14], this.betaArtHb.cX - 45.0F * Settings.scale, this.betaArtHb.cY + 10.0F * Settings.scale, Settings.GOLD_COLOR);
            }

            if (this.viewBetaArt)
            {
                sb.setColor(Color.WHITE);
                sb.draw(ImageMaster.TICK, this.betaArtHb.cX - 80.0F * Settings.scale - 32.0F, this.betaArtHb.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
            }

            this.betaArtHb.render(sb);
        }
    }

    private void renderUpgradeViewToggle(SpriteBatch sb)
    {
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.CHECKBOX, this.upgradeHb.cX - 80.0F * Settings.scale - 32.0F, this.upgradeHb.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        if (this.upgradeHb.hovered)
        {
            FontHelper.renderFont(sb, FontHelper.cardTitleFont, TEXT[6], this.upgradeHb.cX - 45.0F * Settings.scale, this.upgradeHb.cY + 10.0F * Settings.scale, Settings.BLUE_TEXT_COLOR);
        }
        else
        {
            FontHelper.renderFont(sb, FontHelper.cardTitleFont, TEXT[6], this.upgradeHb.cX - 45.0F * Settings.scale, this.upgradeHb.cY + 10.0F * Settings.scale, Settings.GOLD_COLOR);
        }

        if (isViewingUpgrade)
        {
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.TICK, this.upgradeHb.cX - 80.0F * Settings.scale - 32.0F, this.upgradeHb.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        }

        this.upgradeHb.render(sb);
    }

    static
    {
        uiStrings = CardCrawlGame.languagePack.getUIString("SingleCardViewPopup");
        TEXT = uiStrings.TEXT;
        CARD_TYPE_COLOR = new Color(0.35F, 0.35F, 0.35F, 1.0F);
        gl = new GlyphLayout();
        isViewingUpgrade = false;
        enableUpgradeToggle = true;
    }
}
