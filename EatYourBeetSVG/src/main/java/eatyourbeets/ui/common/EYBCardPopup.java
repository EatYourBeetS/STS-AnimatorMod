package eatyourbeets.ui.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputAction;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.effects.SFX;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Toggle;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RenderHelpers;

import java.util.ArrayList;

public class EYBCardPopup extends GUIElement
{
    // TODO: This method cannot be found in certain unknown circumstances
    //private static final MethodInfo _canToggleBetaArt = JavaUtilities.GetMethod("canToggleBetaArt", SingleCardViewPopup.class);
    private static final String[] TEXT = SingleCardViewPopup.TEXT;
    private static final String LAST_DOWNGRADE_FLOOR_KEY = EYBCardPopup.class.getSimpleName() + ".LastDowngradeFloor";

    private final GUI_Toggle upgrade_toggle;
    private final GUI_Toggle betaArt_toggle;
    private final GUI_Button downgradeCard_button;
    private final GUI_Button specialAction_button;

    private final Hitbox nextHb;
    private final Hitbox prevHb;
    private final Hitbox cardHb;
    private final Hitbox upgradeHb;
    private final Hitbox betaArtHb;
    private final Hitbox downgradeHB;
    private final Hitbox specialHB;

    private EYBCard card;
    private EYBCard upgradedCard;
    private int downgradeValue;
    private CardGroup group;
    private AbstractCard prevCard;
    private AbstractCard nextCard;
    private boolean viewBetaArt;
    private float fadeTimer;
    private Color fadeColor;

    public static int GetDowngradeValue(AbstractCard card)
    {
        if (card != null && card.upgraded && !card.hasTag(GR.Enums.CardTags.UNIQUE)
            && !GameUtilities.IsHindrance(card) && AbstractDungeon.player != null && AbstractDungeon.player.masterDeck != null)
        {
            final Integer lastFloor = GR.Common.Dungeon.GetInteger(LAST_DOWNGRADE_FLOOR_KEY, -1);
            if (lastFloor == AbstractDungeon.floorNum)
            {
                return 0;
            }

            for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
            {
                if (c.uuid.equals(card.uuid))
                {
                    switch (card.rarity)
                    {
                        case BASIC:    return 15;
                        case COMMON:   return 20;
                        case UNCOMMON: return 25;
                        case RARE:     return 35;
                        case SPECIAL:  return 40;
                        default:       return 0;
                    }
                }
            }
        }

        return 0;
    }

    public EYBCardPopup()
    {
        this.fadeColor = Color.BLACK.cpy();
        this.upgradeHb = new Hitbox(250f * Settings.scale, 80f * Settings.scale);
        this.betaArtHb = new Hitbox(250f * Settings.scale, 80f * Settings.scale);
        this.prevHb = new Hitbox(160f * Settings.scale, 160f * Settings.scale);
        this.nextHb = new Hitbox(160f * Settings.scale, 160f * Settings.scale);
        this.cardHb = new Hitbox(550f * Settings.scale, 770f * Settings.scale);
        this.downgradeHB = new Hitbox(EYBCardTooltip.BOX_W, EYBCardTooltip.BOX_BODY_H);
        this.specialHB = new Hitbox(EYBCardTooltip.BOX_W, EYBCardTooltip.BOX_BODY_H);
        this.viewBetaArt = false;
        this.isActive = false;

        this.upgrade_toggle = new GUI_Toggle(upgradeHb).SetText(TEXT[6])
        .SetBackground(RenderHelpers.ForTexture(ImageMaster.CHECKBOX))
        .SetTickImage(null, RenderHelpers.ForTexture(ImageMaster.TICK), 64)
        .SetFontColors(Settings.GOLD_COLOR, Settings.BLUE_TEXT_COLOR)
        .SetControllerAction(CInputActionSet.proceed)
        .SetFont(FontHelper.cardTitleFont, 1)
        .SetOnToggle(this::ToggleUpgrade);

        this.betaArt_toggle = new GUI_Toggle(betaArtHb).SetText(TEXT[14])
        .SetBackground(RenderHelpers.ForTexture(ImageMaster.CHECKBOX))
        .SetTickImage(null, RenderHelpers.ForTexture(ImageMaster.TICK), 64)
        .SetFontColors(Settings.GOLD_COLOR, Settings.BLUE_TEXT_COLOR)
        .SetControllerAction(CInputActionSet.proceed)
        .SetFont(FontHelper.cardTitleFont, 1)
        .SetOnToggle(this::ToggleBetaArt);

        this.downgradeCard_button = new GUI_Button(GR.Common.Images.Panel_Rounded_Half_H.Texture(), downgradeHB)
        .SetBorder(GR.Common.Images.Panel_Rounded_Half_H_Border.Texture(), Colors.Gold(1))
        .SetColor(new Color(0.15f, 0.15f, 0.15f, 1f), Colors.White(0.15f))
        .SetClickDelay(0.3f)
        .SetPosition((Settings.WIDTH * 0.025f) + (downgradeHB.width * 0.5f), Settings.HEIGHT * 0.725f)
        .SetText(GR.Animator.Strings.SpecialActions.Downgrade_T(10), true)
        .SetFont(FontHelper.buttonLabelFont, 0.95f)
        .SetTextColor(Colors.Cream(1f))
        .SetTooltip(GR.Common.Strings.Terms.Downgrade, GR.Animator.Strings.SpecialActions.D_Downgrade_F0, true)
        .SetOnClick(this::DowngradeCard);

        this.specialAction_button = new GUI_Button(GR.Common.Images.Panel_Rounded_Half_H.Texture(), specialHB)
        .SetBorder(GR.Common.Images.Panel_Rounded_Half_H_Border.Texture(), Colors.Gold(1))
        .SetColor(new Color(0.15f, 0.15f, 0.15f, 1f), Colors.White(0.15f))
        .SetClickDelay(0.3f)
        .SetPosition((Settings.WIDTH * 0.025f) + (specialHB.width * 0.5f), downgradeHB.cY - downgradeHB.height)
        .SetText("", true)
        .SetFont(FontHelper.buttonLabelFont, 0.95f)
        .SetTextColor(Colors.Cream(1f));
    }

    public void Open(EYBCard card, CardGroup group)
    {
        CardCrawlGame.isPopupOpen = true;

        this.specialAction_button.onLeftClick = null;
        this.card = card.MakePopupCopy(this);

        for (EYBCardPopupAction action : card.cardData.popupActions)
        {
            action.Initialize(card);
            specialAction_button
            .SetText(action.name)
            .SetTooltip(action.tooltip, true)
            .SetInteractable(action.CanExecute())
            .SetOnClick(action::Execute);
            break; // TODO: support for multiple actions
        }

        this.downgradeValue = GetDowngradeValue(card);
        this.downgradeCard_button.SetText(GR.Animator.Strings.SpecialActions.Downgrade_T(downgradeValue)).SetActive(downgradeValue > 0);
        this.upgradedCard = null;
        this.isActive = true;
        this.prevCard = null;
        this.nextCard = null;
        this.group = group;

        if (group != null)
        {
            for (int i = 0; i < group.size(); ++i)
            {
                if (group.group.get(i).uuid == card.uuid)
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

            this.prevHb.move((float) Settings.WIDTH / 2f - 400f * Settings.scale, (float) Settings.HEIGHT / 2f);
            this.nextHb.move((float) Settings.WIDTH / 2f + 400f * Settings.scale, (float) Settings.HEIGHT / 2f);
        }

        this.cardHb.move((float) Settings.WIDTH / 2f, (float) Settings.HEIGHT / 2f);

        this.fadeTimer = 0.25f;
        this.fadeColor.a = 0f;

        this.betaArt_toggle.SetActive(false);// (boolean)_canToggleBetaArt.Invoke(CardCrawlGame.cardPopup));
        this.upgrade_toggle.SetActive(SingleCardViewPopup.enableUpgradeToggle && card.canUpgrade());

        if (betaArt_toggle.isActive)
        {
            this.viewBetaArt = UnlockTracker.betaCardPref.getBoolean(card.cardID, false);

            if (upgrade_toggle.isActive)
            {
                this.betaArtHb.move((float) Settings.WIDTH / 2f + 270f * Settings.scale, 70f * Settings.scale);
                this.upgradeHb.move((float) Settings.WIDTH / 2f - 180f * Settings.scale, 70f * Settings.scale);
            }
            else
            {
                this.betaArtHb.move((float) Settings.WIDTH / 2f, 70f * Settings.scale);
            }
        }
        else
        {
            this.upgradeHb.move((float) Settings.WIDTH / 2f, 70f * Settings.scale);
        }
    }

    public EYBCard GetCard(boolean canUpgrade)
    {
        if (SingleCardViewPopup.isViewingUpgrade && canUpgrade)
        {
            if (upgradedCard == null)
            {
                upgradedCard = card.MakePopupCopy(null);
                upgradedCard.upgrade();
                upgradedCard.displayUpgrades();
            }

            return upgradedCard;
        }
        else
        {
            return card;
        }
    }

    public void Close()
    {
        if (AbstractDungeon.player != null)
        {
            SingleCardViewPopup.isViewingUpgrade = false;
        }

        InputHelper.justReleasedClickLeft = false;
        CardCrawlGame.isPopupOpen = false;
        this.specialAction_button.onLeftClick = null;
        this.upgradedCard = null;
        this.isActive = false;
        this.card = null;
    }

    @Override
    public void Update()
    {
        this.cardHb.update();
        this.UpdateArrows();
        this.UpdateInput();

        this.fadeTimer = Math.max(0, fadeTimer - Gdx.graphics.getDeltaTime());
        this.fadeColor.a = Interpolation.pow2In.apply(0.9f, 0f, this.fadeTimer * 4f);

        this.upgrade_toggle.SetToggle(SingleCardViewPopup.isViewingUpgrade).TryUpdate();
        this.betaArt_toggle.SetToggle(viewBetaArt).TryUpdate();
        this.specialAction_button.SetActive(specialAction_button.onLeftClick != null).TryUpdate();
        this.downgradeCard_button.SetActive(downgradeValue > 0).TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        sb.setColor(this.fadeColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0f, 0f, (float) Settings.WIDTH, (float) Settings.HEIGHT);
        sb.setColor(Color.WHITE);

        final EYBCard card = GetCard(true);

        card.renderInLibrary(sb);
        card.renderCardTip(sb);

        if (this.prevCard != null)
        {
            RenderArrow(sb, prevHb, CInputActionSet.pageLeftViewDeck, false);
        }

        if (this.nextCard != null)
        {
            RenderArrow(sb, nextHb, CInputActionSet.pageRightViewExhaust, true);
        }

        this.cardHb.render(sb);

        FontHelper.cardTitleFont.getData().setScale(1);
        if (upgrade_toggle.isActive)
        {
            upgrade_toggle.Render(sb);

            if (Settings.isControllerMode)
            {
                sb.draw(CInputActionSet.proceed.getKeyImg(), this.upgradeHb.cX - 132f * Settings.scale - 32f, -32f + 67f * Settings.scale, 32f, 32f, 64f, 64f, Settings.scale, Settings.scale, 0f, 0, 0, 64, 64, false, false);
            }
        }
        if (betaArt_toggle.isActive)
        {
            betaArt_toggle.Render(sb);

            if (Settings.isControllerMode)
            {
                sb.draw(CInputActionSet.topPanel.getKeyImg(), this.betaArtHb.cX - 132f * Settings.scale - 32f, -32f + 67f * Settings.scale, 32f, 32f, 64f, 64f, Settings.scale, Settings.scale, 0f, 0, 0, 64, 64, false, false);
            }
        }

        this.downgradeCard_button.TryRender(sb);
        this.specialAction_button.TryRender(sb);
    }

    private void ToggleBetaArt(boolean value)
    {
        this.viewBetaArt = value;
        UnlockTracker.betaCardPref.putBoolean(this.card.cardID, this.viewBetaArt);
        UnlockTracker.betaCardPref.flush();
    }

    private void ToggleUpgrade(boolean value)
    {
        SingleCardViewPopup.isViewingUpgrade = value;
    }

    private void UpdateArrows()
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
                this.prevHb.clicked = false;
                CInputActionSet.pageLeftViewDeck.unpress();
                OpenNext(prevCard);
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
                this.nextHb.clicked = false;
                CInputActionSet.pageRightViewExhaust.unpress();
                OpenNext(nextCard);
            }
        }
    }

    private void UpdateInput()
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

            if (!this.cardHb.hovered && !this.upgradeHb.hovered
            && (!this.downgradeCard_button.isActive || !this.downgradeHB.hovered)
            && (!this.specialAction_button.isActive || !this.specialHB.hovered)
            && (this.betaArtHb == null || !this.betaArtHb.hovered))
            {
                Close();
                InputHelper.justClickedLeft = false;
            }
        }
        else if (InputHelper.pressedEscape || CInputActionSet.cancel.isJustPressed())
        {
            CInputActionSet.cancel.unpress();
            InputHelper.pressedEscape = false;
            Close();
        }

        if (this.prevCard != null && InputActionSet.left.isJustPressed())
        {
            OpenNext(prevCard);
        }
        else if (this.nextCard != null && InputActionSet.right.isJustPressed())
        {
            OpenNext(nextCard);
        }
    }

    private void OpenNext(AbstractCard card)
    {
        boolean tmp = SingleCardViewPopup.isViewingUpgrade;
        this.Close();
        CardCrawlGame.cardPopup.open(card, this.group);
        SingleCardViewPopup.isViewingUpgrade = tmp;
        this.fadeTimer = 0f;
        this.fadeColor.a = 0.9f;
    }

    private void RenderArrow(SpriteBatch sb, Hitbox hb, CInputAction action, boolean flipX)
    {
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.POPUP_ARROW, hb.cX - 128f, hb.cY - 128f, 128f, 128f, 256f, 256f, Settings.scale, Settings.scale, 0f, 0, 0, 256, 256, flipX, false);
        if (Settings.isControllerMode)
        {
            sb.draw(action.getKeyImg(), hb.cX - 32f, hb.cY - 32f + 100f * Settings.scale, 32f, 32f, 64f, 64f, Settings.scale, Settings.scale, 0f, 0, 0, 64, 64, false, false);
        }

        if (hb.hovered)
        {
            sb.setBlendFunction(770, 1);
            sb.setColor(new Color(1f, 1f, 1f, 0.5f));
            sb.draw(ImageMaster.POPUP_ARROW, hb.cX - 128f, hb.cY - 128f, 128f, 128f, 256f, 256f, Settings.scale, Settings.scale, 0f, 0, 0, 256, 256, flipX, false);
            sb.setColor(Color.WHITE);
            sb.setBlendFunction(770, 771);
        }

        hb.render(sb);
    }

    private void DowngradeCard()
    {
        final int gold = GetDowngradeValue(card);
        if (gold <= 0)
        {
            throw new RuntimeException("Invalid downgrade value: " + card.cardID);
        }

        int index = -1;
        AbstractCard originalCard = null;
        final ArrayList<AbstractCard> cards = AbstractDungeon.player.masterDeck.group;
        for (int i = 0; i < cards.size(); i++)
        {
            final AbstractCard c = cards.get(i);
            if (c.uuid.equals(card.uuid))
            {
                originalCard = c;
                index = i;
                break;
            }
        }

        if (index < 0)
        {
            throw new RuntimeException("Card not found in masterDeck: " + card.cardID);
        }

        final EYBCard replacement = (EYBCard) originalCard.makeCopy();
        replacement.misc = originalCard.misc;
        GameUtilities.CopyVisualProperties(replacement, originalCard);
        AbstractDungeon.player.masterDeck.group.set(index, replacement);

        GR.Common.Dungeon.SetData(LAST_DOWNGRADE_FLOOR_KEY, AbstractDungeon.floorNum);
        replacement.flash();
        Close();

        AbstractDungeon.player.gainGold(gold);
        SFX.Play(SFX.GOLD_GAIN);
    }
}
