package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorCardSlot;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_TextBox;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;

public class AnimatorCardSlotEditor extends GUIElement
{
    protected static final float CARD_SCALE = 0.75f;
    public static final float PREVIEW_OFFSET_X = AbstractCard.IMG_WIDTH * 0.6f;
    public static final float PREVIEW_OFFSET_Y = - AbstractCard.IMG_HEIGHT * 0.57f;
    public static final float ITEM_HEIGHT = AbstractCard.IMG_HEIGHT * 0.15f;

    public AnimatorCardSlot slot;
    public AnimatorLoadoutEditor loadoutEditor;

    protected GUI_TextBox cardName_text;
    protected GUI_TextBox cardValue_text;
    protected GUI_TextBox cardAmount_text;
    protected GUI_Button add_button;
    protected GUI_Button decrement_button;
    protected GUI_Button change_button;
    protected GUI_Button clear_button;
    protected AbstractCard card;

    public AnimatorCardSlotEditor(AnimatorLoadoutEditor loadoutEditor, float cX, float cY)
    {
        this.loadoutEditor = loadoutEditor;

        cardValue_text = new GUI_TextBox(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new AdvancedHitbox(cX, cY, AbstractCard.IMG_WIDTH * 0.2f, ITEM_HEIGHT))
                .SetBackgroundTexture(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new Color(0.5f, 0.5f, 0.5f , 1f), 1.05f)
                .SetColors(new Color(0, 0, 0, 0.85f), Settings.CREAM_COLOR)
                .SetAlignment(0.5f, 0.5f)
                .SetFont(FontHelper.cardEnergyFont_L, 0.75f);

        cardAmount_text = new GUI_TextBox(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new AdvancedHitbox(cardValue_text.hb.x + cardValue_text.hb.width, cY, AbstractCard.IMG_HEIGHT * 0.15f, ITEM_HEIGHT ))
        .SetColors(Settings.HALF_TRANSPARENT_BLACK_COLOR, Settings.CREAM_COLOR)
        .SetAlignment(0.5f, 0.5f)
        .SetFont(FontHelper.cardTitleFont, 1);

        cardName_text = new GUI_TextBox(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new AdvancedHitbox(cardAmount_text.hb.x + cardAmount_text.hb.width, cY, AbstractCard.IMG_WIDTH, ITEM_HEIGHT))
                .SetColors(Settings.HALF_TRANSPARENT_BLACK_COLOR, Settings.GOLD_COLOR)
                .SetAlignment(0.5f, 0.5f)
                .SetFont(FontHelper.cardTitleFont, 1);

        decrement_button = new GUI_Button(GR.Common.Images.Minus.Texture(), new AdvancedHitbox(cardName_text.hb.x + cardName_text.hb.width, cardName_text.hb.y,64,64))
                .SetClickDelay(0.02f);
        add_button = new GUI_Button(GR.Common.Images.Plus.Texture(), new AdvancedHitbox(decrement_button.hb.x + decrement_button.hb.width, cardName_text.hb.y,64,64))
                .SetClickDelay(0.02f);
        clear_button = new GUI_Button(GR.Common.Images.X.Texture(), new AdvancedHitbox(add_button.hb.x + add_button.hb.width, cardName_text.hb.y,64,64))
                .SetClickDelay(0.02f);
        change_button = new GUI_Button(GR.Common.Images.Edit.Texture(), new AdvancedHitbox(clear_button.hb.x + clear_button.hb.width, cardName_text.hb.y,64,64))
                .SetClickDelay(0.02f);

        SetSlot(null);
    }

    public AnimatorCardSlotEditor SetSlot(AnimatorCardSlot slot)
    {
        if (slot == null)
        {
            this.slot = null;
            this.card = null;
            this.cardAmount_text.SetActive(false);
            this.cardName_text.SetActive(false);
            this.cardValue_text.SetActive(false);
            this.add_button.SetActive(false);
            this.decrement_button.SetActive(false);
            this.change_button.SetActive(false);
            this.clear_button.SetActive(false);
            return this;
        }

        final boolean add = card != null && slot.max > 1;
        final boolean change = slot.Cards.Count() > 1;
        final boolean remove = card != null && slot.max > slot.min;

        this.slot = slot;
        this.card = slot.GetCard(true);
        this.cardName_text.SetText(card != null ? card.name : "").SetActive(true);
        this.cardValue_text.SetActive(card != null);
        this.cardAmount_text.SetActive(card != null);
        this.add_button.SetOnClick(this.slot::Add).SetInteractable(slot.CanAdd()).SetActive(card != null);
        this.decrement_button.SetOnClick(this.slot::Decrement).SetInteractable(slot.CanDecrement()).SetActive(card != null);
        this.clear_button.SetOnClick(this.slot::Clear).SetInteractable(slot.CanRemove()).SetActive(card != null);
        this.change_button.SetOnClick(() -> loadoutEditor.TrySelectCard(this.slot)).SetActive(change);

        return this;
    }

    @Override
    public void Update()
    {
        if (slot == null)
        {
            return;
        }
        cardName_text.TryUpdate();

        if (change_button.isActive && cardName_text.hb.hovered)
        {
            if (InputHelper.justClickedLeft)
            {
                cardName_text.hb.clickStarted = true;
            }

            if (cardName_text.hb.clicked)
            {
                cardName_text.hb.clicked = false;
                loadoutEditor.TrySelectCard(this.slot);
                return;
            }

            cardName_text.SetFontColor(Color.WHITE);
        }
        else
        {
            cardName_text.SetFontColor(Color.GOLD);
        }

        card = slot.GetCard(false);
        if (card != null)
        {
            card.current_x = card.target_x = card.hb.x = InputHelper.mX + PREVIEW_OFFSET_X;
            card.current_y = card.target_y = card.hb.y = InputHelper.mY + PREVIEW_OFFSET_Y;
            card.update();
            card.updateHoverLogic();
            card.drawScale = card.targetDrawScale = CARD_SCALE * ((card.hb.hovered) ? 0.97f : 0.95f);
            cardAmount_text.SetText(slot.amount + "x ").Update();
        }
        else {
            cardAmount_text.SetText("").Update();
        }

        int value = slot.GetEstimatedValue();
        cardValue_text.SetText(value)
        .SetFontColor(value == 0 ? Settings.CREAM_COLOR : value < 0 ? Settings.RED_TEXT_COLOR : Settings.GREEN_TEXT_COLOR)
        .TryUpdate();

        if (add_button.isActive)
        {
            add_button.SetInteractable(slot.CanAdd()).Update();
        }
        if (decrement_button.isActive)
        {
            decrement_button.SetInteractable(slot.CanDecrement()).Update();
        }
        if (change_button.isActive)
        {
            change_button.Update();
        }
        if (clear_button.isActive)
        {
            clear_button.SetInteractable(slot.CanRemove()).Update();
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        cardName_text.TryRender(sb);
        cardValue_text.TryRender(sb);
        cardAmount_text.TryRender(sb);
        add_button.TryRender(sb);
        decrement_button.TryRender(sb);
        change_button.TryRender(sb);
        clear_button.TryRender(sb);
        if (cardName_text.hb.hovered && card != null) {
            card.renderInLibrary(sb);
            card.renderCardTip(sb);
        }
    }
}