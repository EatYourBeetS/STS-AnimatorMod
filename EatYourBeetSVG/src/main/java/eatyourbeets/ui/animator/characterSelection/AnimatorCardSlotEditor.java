package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.CardSlot;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_TextBox;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.ui.hitboxes.RelativeHitbox;

public class AnimatorCardSlotEditor extends GUIElement
{
    public CardSlot slot;
    public AnimatorLoadoutEditor loadoutEditor;

    protected static final float CARD_SCALE = 0.75f;
    protected AdvancedHitbox hb;
    protected GUI_Image background_image;
    protected GUI_TextBox cardValue_text;
    protected GUI_TextBox cardAmount_text;
    protected GUI_Button add_button;
    protected GUI_Button change_button;
    protected GUI_Button remove_button;
    protected AbstractCard card;

    public AnimatorCardSlotEditor(AnimatorLoadoutEditor loadoutEditor, float cX, float cY)
    {
        this.loadoutEditor = loadoutEditor;
        this.hb = new AdvancedHitbox(Scale(AbstractCard.RAW_W) * CARD_SCALE, Scale(AbstractCard.RAW_H) * CARD_SCALE);

        background_image = new GUI_Image(GR.Common.Images.Panel_Rounded.Texture(), hb)
        .SetBackgroundTexture(GR.Common.Images.Panel_Rounded.Texture(), new Color(0.5f, 0.5f, 0.5f , 1f), 1.03f)
        .SetColor(0, 0, 0, 0.85f)
        .SetPosition(cX, cY);

        cardValue_text = new GUI_TextBox(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new RelativeHitbox(hb, 0.33f, 0.2f, -0.175f, 0.9f))
        .SetBackgroundTexture(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new Color(0.5f, 0.5f, 0.5f , 1f), 1.05f)
        .SetColors(new Color(0, 0, 0, 0.85f), Settings.CREAM_COLOR)
        .SetAlignment(0.5f, 0.5f)
        .SetFont(FontHelper.cardEnergyFont_L, 0.75f);

        cardAmount_text = new GUI_TextBox(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new RelativeHitbox(hb, 0.35f, 0.2f, -0.175f, 0.75f))
        .SetColors(Settings.HALF_TRANSPARENT_BLACK_COLOR, Settings.CREAM_COLOR)
        .SetAlignment(0.5f, 0.5f)
        .SetFont(FontHelper.charDescFont, 1);

        add_button = CreateButton(0.7f, Color.FOREST, "+");
        change_button = CreateButton(0.5f, Color.SKY, "Next").SetFont(null, 0.75f);
        remove_button = CreateButton(0.3f, Color.FIREBRICK, "-");

        SetSlot(null);
    }

    public AnimatorCardSlotEditor SetSlot(CardSlot slot)
    {
        if (slot == null)
        {
            this.slot = null;
            this.card = null;
            this.cardAmount_text.SetActive(false);
            this.cardValue_text.SetActive(false);
            this.add_button.SetActive(false);
            this.change_button.SetActive(false);
            this.remove_button.SetActive(false);
            return this;
        }

        final boolean add = slot.max > 1;
        final boolean change = slot.Cards.Count() > 1;
        final boolean remove = slot.max > slot.min;

        this.slot = slot;
        this.card = slot.GetCard(true);
        this.cardValue_text.SetActive(true);
        this.cardAmount_text.SetActive(add);
        this.add_button.SetOnClick(this.slot::Add).SetActive(add);
        this.remove_button.SetOnClick(this.slot::Remove).SetActive(remove);
        this.change_button.SetOnClick(this.slot::Next).SetActive(change);

        float cY = 0.75f;
        if (add)
        {
            RelativeHitbox.SetPercentageOffset(add_button.hb, null, cY);
            cY -= 0.15f;
        }
        if (change)
        {
            RelativeHitbox.SetPercentageOffset(change_button.hb, null, cY);
            cY -= 0.15f;
        }
        if (remove)
        {
            RelativeHitbox.SetPercentageOffset(remove_button.hb, null, cY);
            cY -= 0.15f;
        }
        if (add)
        {
            RelativeHitbox.SetPercentageOffset(cardAmount_text.hb, null, cY);
        }

        return this;
    }

    @Override
    public void Update()
    {
        background_image.Update();

        if (slot == null)
        {
            return;
        }

        if (change_button.isActive && background_image.hb.hovered)
        {
            if (InputHelper.justClickedLeft)
            {
                background_image.hb.clickStarted = true;
            }

            if (background_image.hb.clicked)
            {
                background_image.hb.clicked = false;
                loadoutEditor.TrySelectCard(this.slot);
                return;
            }

            background_image.color.a = 0.7f;
        }
        else
        {
            background_image.color.a = 0.85f;
        }

        card = slot.GetCard(false);
        if (card != null)
        {
            card.current_x = card.target_x = card.hb.cX = background_image.hb.cX;
            card.current_y = card.target_y = card.hb.cY = background_image.hb.cY;
            card.update();
            card.updateHoverLogic();
            card.drawScale = card.targetDrawScale = CARD_SCALE * ((card.hb.hovered) ? 0.97f : 0.95f);
        }

        int value = slot.GetEstimatedValue();
        cardValue_text.SetText(value)
        .SetFontColor(value == 0 ? Settings.CREAM_COLOR : value < 0 ? Settings.RED_TEXT_COLOR : Settings.GREEN_TEXT_COLOR)
        .TryUpdate();

        if (add_button.isActive)
        {
            add_button.SetInteractable(slot.CanAdd()).Update();
            cardAmount_text.SetText("x " + slot.amount).Update();
        }
        if (change_button.isActive)
        {
            change_button.Update();
        }
        if (remove_button.isActive)
        {
            remove_button.SetInteractable(slot.CanRemove()).Update();
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        background_image.Render(sb);
        cardValue_text.TryRender(sb);
        cardAmount_text.TryRender(sb);
        add_button.TryRender(sb);
        change_button.TryRender(sb);
        remove_button.TryRender(sb);

        if (card != null)
        {
            card.renderInLibrary(sb);

            if (card.hb.hovered)
            {
                card.renderCardTip(sb);
            }
        }
    }

    private GUI_Button CreateButton(float cY, Color color, String text)
    {
        final Texture buttonTexture = GR.Common.Images.SquaredButton.Texture();
        return new GUI_Button(buttonTexture, new RelativeHitbox(hb, 0.35f, 0.15f, -0.175f, cY))
        .SetClickDelay(0.02f)
        .SetTextColor(Settings.CREAM_COLOR)
        .SetFont(FontHelper.charDescFont, 1f)
        .SetColor(color.cpy().lerp(Color.BLACK, 0.2f))
        .SetText(text);
    }
}