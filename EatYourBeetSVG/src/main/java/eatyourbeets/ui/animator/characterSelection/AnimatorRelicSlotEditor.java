package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorRelicSlot;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Relic;
import eatyourbeets.ui.controls.GUI_TextBox;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;

public class AnimatorRelicSlotEditor extends GUIElement
{
    protected static final float CARD_SCALE = 0.75f;
    public static final float SPACING = 64f * Settings.scale;
    public static final float ITEM_HEIGHT = AbstractCard.IMG_HEIGHT * 0.15f;

    public AnimatorRelicSlot slot;
    public AnimatorLoadoutEditor loadoutEditor;

    protected GUI_TextBox relicName_text;
    protected GUI_TextBox relicValue_text;
    protected GUI_Button change_button;
    protected GUI_Button clear_button;
    protected GUI_Relic relicImage;
    protected EYBRelic relic;

    public AnimatorRelicSlotEditor(AnimatorLoadoutEditor loadoutEditor, float cX, float cY)
    {
        this.loadoutEditor = loadoutEditor;

        relicValue_text = new GUI_TextBox(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new AdvancedHitbox(cX, cY, AbstractCard.IMG_WIDTH * 0.2f, ITEM_HEIGHT))
                .SetBackgroundTexture(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new Color(0.5f, 0.5f, 0.5f , 1f), 1.05f)
                .SetColors(new Color(0, 0, 0, 0.85f), Settings.CREAM_COLOR)
                .SetAlignment(0.5f, 0.5f)
                .SetFont(FontHelper.cardEnergyFont_L, 0.75f);

        relicName_text = new GUI_TextBox(GR.Common.Images.Panel_Rounded_Half_H.Texture(), new AdvancedHitbox(relicValue_text.hb.x + relicValue_text.hb.width + SPACING, cY, AbstractCard.IMG_WIDTH, ITEM_HEIGHT))
                .SetColors(Settings.HALF_TRANSPARENT_BLACK_COLOR, Settings.GOLD_COLOR)
                .SetAlignment(0.5f, 0.5f)
                .SetFont(FontHelper.cardTitleFont, 1);

        clear_button = new GUI_Button(GR.Common.Images.X.Texture(), new AdvancedHitbox(relicName_text.hb.x + relicName_text.hb.width, relicName_text.hb.y,64,64))
                .SetClickDelay(0.02f);
        change_button = new GUI_Button(GR.Common.Images.Edit.Texture(), new AdvancedHitbox(clear_button.hb.x + clear_button.hb.width, relicName_text.hb.y,64,64))
                .SetClickDelay(0.02f);

        SetSlot(null);
    }

    public AnimatorRelicSlotEditor SetSlot(AnimatorRelicSlot slot)
    {
        if (slot == null)
        {
            this.slot = null;
            this.relic = null;
            this.relicName_text.SetActive(false);
            this.relicValue_text.SetActive(false);
            this.change_button.SetActive(false);
            this.clear_button.SetActive(false);
            return this;
        }

        final boolean change = slot.Relics.Count() > 1;

        this.slot = slot;
        this.relic = slot.GetRelic();
        this.relicName_text.SetText(relic != null ? relic.name : "").SetActive(true);
        this.relicValue_text.SetActive(true);
        this.clear_button.SetOnClick(this.slot::Clear).SetInteractable(slot.CanRemove()).SetActive(relic != null);
        this.change_button.SetOnClick(() -> loadoutEditor.TrySelectRelic(this.slot)).SetActive(change);
        if (relic != null) {
            this.relicImage = new GUI_Relic(relic, new AdvancedHitbox(relicValue_text.hb.x + relicValue_text.hb.width + SPACING / 2, relicValue_text.hb.y, relic.hb.width, relic.hb.height));
        }

        return this;
    }

    public AnimatorRelicSlotEditor Translate(float cX, float cY) {
        relicValue_text.SetPosition(cX, cY);
        relicName_text.SetPosition(relicValue_text.hb.x + relicValue_text.hb.width + SPACING, cY);
        clear_button.SetPosition(relicName_text.hb.x + relicName_text.hb.width, cY);
        change_button.SetPosition(clear_button.hb.x + clear_button.hb.width, cY);
        if (relic != null && this.relicImage != null) {
            this.relicImage.Translate(relicValue_text.hb.x + relicValue_text.hb.width + SPACING / 2, relicValue_text.hb.y);
        }

        return this;
    }

    @Override
    public void Update()
    {
        if (slot == null)
        {
            return;
        }
        relicName_text.TryUpdate();

        if (change_button.isActive && relicName_text.hb.hovered)
        {
            if (InputHelper.justClickedLeft)
            {
                relicName_text.hb.clickStarted = true;
            }

            if (relicName_text.hb.clicked)
            {
                relicName_text.hb.clicked = false;
                loadoutEditor.TrySelectRelic(this.slot);
                return;
            }

            relicName_text.SetFontColor(Color.WHITE);
        }
        else
        {
            relicName_text.SetFontColor(Color.GOLD);
        }

        relic = slot.GetRelic();
        if (relic != null && this.relicImage != null) {
            relicImage.Translate(relicValue_text.hb.x + relicValue_text.hb.width, relicValue_text.hb.y);
            relicImage.Update();
        }

        int value = slot.GetEstimatedValue();
        relicValue_text.SetText(value)
        .SetFontColor(value == 0 ? Settings.CREAM_COLOR : value < 0 ? Settings.RED_TEXT_COLOR : Settings.GREEN_TEXT_COLOR)
        .TryUpdate();

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
        relicName_text.TryRender(sb);
        if (this.relicImage != null) {
            relicImage.Render(sb);
        }
        relicValue_text.TryRender(sb);
        change_button.TryRender(sb);
        clear_button.TryRender(sb);
    }
}