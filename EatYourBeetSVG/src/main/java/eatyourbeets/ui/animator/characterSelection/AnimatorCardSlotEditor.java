package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
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

    protected static final float CARD_SCALE = 0.75f;
    protected AdvancedHitbox hb;
    protected GUI_Image background_image;
    protected GUI_TextBox cardValue_text;
    protected GUI_TextBox cardAmount_text;
    protected GUI_Button add_button;
    protected GUI_Button change_button;
    protected GUI_Button remove_button;
    protected AbstractCard card;

    public AnimatorCardSlotEditor(float cX, float cY)
    {
        hb = new AdvancedHitbox(Scale(AbstractCard.RAW_W) * CARD_SCALE, Scale(AbstractCard.RAW_H) * CARD_SCALE);

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
    }

    public AnimatorCardSlotEditor SetSlot(CardSlot slot)
    {
        final boolean add = slot.max > 1;
        final boolean change = slot.cards.Count() > 1;
        final boolean remove = slot.max > slot.min;

        this.slot = slot;
        this.card = slot.GetCard();
        this.cardAmount_text.SetActive(add);
        this.add_button.SetOnClick(this.slot::Add).SetActive(add);
        this.change_button.SetOnClick(this.slot::Next).SetActive(change);
        this.remove_button.SetOnClick(this.slot::Remove).SetActive(remove);

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

        if (slot != null)
        {
            card = slot.GetCard();

            int value = slot.GetEstimatedValue();
            cardValue_text.SetText(value)
            .SetFontColor(value == 0 ? Settings.CREAM_COLOR : value < 0 ? Settings.RED_TEXT_COLOR : Settings.GREEN_TEXT_COLOR)
            .Update();

            if (add_button.isActive)
            {
                add_button.SetInteractable(slot.CanAdd()).Update();
                cardAmount_text.SetText("x" + slot.amount).Update();
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
        else
        {
            card = null;
            cardValue_text.SetText(0).SetFontColor(Settings.CREAM_COLOR).Update();
        }

        if (card != null)
        {
            card.drawScale = card.targetDrawScale = CARD_SCALE * 0.97f;
            card.current_x = card.target_x = card.hb.cX = background_image.hb.cX;
            card.current_y = card.target_y = card.hb.cY = background_image.hb.cY;
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        background_image.Render(sb);
        cardValue_text.TryRender(sb);
        cardAmount_text.Render(sb);
        add_button.TryRender(sb);
        change_button.TryRender(sb);
        remove_button.TryRender(sb);

        if (card != null)
        {
            card.renderInLibrary(sb);
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