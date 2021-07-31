package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.cards.base.EYBCardBase;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.CardSlot;
import eatyourbeets.resources.animator.misc.AnimatorLoadoutData;
import eatyourbeets.ui.AbstractScreen;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_TextBox;
import eatyourbeets.ui.controls.GUI_Toggle;

import java.util.ArrayList;

public class AnimatorLoadoutEditor extends AbstractScreen
{
    protected final static AnimatorLoadout.Validation val = new AnimatorLoadout.Validation();
    protected final ArrayList<AnimatorCardSlotEditor> slotsEditors = new ArrayList<>();
    protected AnimatorBaseStatEditor goldEditor;
    protected AnimatorBaseStatEditor hpEditor;
    protected AnimatorLoadout loadout;
    protected AnimatorLoadoutData data;
    protected ActionT0 onClose;

    protected AnimatorCardSlotSelectionEffect cardSelectionEffect;
    protected GUI_Image background_image;
    protected GUI_Button cancel_button;
    protected GUI_Button save_button;
    protected GUI_Toggle upgrade_toggle;
    protected GUI_TextBox cardsCount_text;
    protected GUI_TextBox cardsValue_text;
    protected GUI_TextBox affinityValue_text;

    public AnimatorLoadoutEditor()
    {
        final float buttonHeight = ScreenH(0.07f);
        final float labelHeight = ScreenH(0.04f);
        final float buttonWidth = ScreenW(0.18f);
        final float labelWidth = ScreenW(0.20f);
        final float button_cY = buttonHeight * 1.5f;

        background_image = new GUI_Image(GR.Common.Images.FullSquare.Texture(), new Hitbox(ScreenW(1), ScreenH(1)))
        .SetPosition(ScreenW(0.5f), ScreenH(0.5f))
        .SetColor(0, 0, 0, 0.85f);

        cancel_button = CreateHexagonalButton(0, 0, buttonWidth, buttonHeight)
        .SetPosition(buttonWidth * 0.75f, button_cY)
        .SetColor(Color.FIREBRICK)
        .SetText("Cancel")
        .SetOnClick(AbstractDungeon::closeCurrentScreen);

        save_button = CreateHexagonalButton(0, 0, buttonWidth, buttonHeight)
        .SetPosition(ScreenW(1) - (buttonWidth * 0.75f), button_cY)
        .SetColor(Color.FOREST)
        .SetText("Save")
        .SetInteractable(false)
        .SetOnClick(this::Save);

        upgrade_toggle = new GUI_Toggle(new Hitbox(0, 0, labelWidth * 0.75f, labelHeight))
        .SetPosition(ScreenW(0.5f), ScreenH(0.055f))
        .SetBackground(GR.Common.Images.Panel_Rounded.Texture(), new Color(0, 0, 0, 0.85f))
        .SetText(SingleCardViewPopup.TEXT[6])
        .SetOnToggle(this::ToggleViewUpgrades);

        cardsValue_text = new GUI_TextBox(GR.Common.Images.Panel_Rounded.Texture(), new Hitbox(labelWidth, labelHeight))
        .SetColors(Settings.HALF_TRANSPARENT_BLACK_COLOR, Settings.CREAM_COLOR)
        .SetAlignment(0.5f, 0.5f)
        .SetPosition(save_button.hb.cX, save_button.hb.y + save_button.hb.height + labelHeight * 0.8f)
        .SetFont(FontHelper.charDescFont, 1);

        cardsCount_text = new GUI_TextBox(GR.Common.Images.Panel_Rounded.Texture(), new Hitbox(labelWidth, labelHeight))
        .SetColors(Settings.HALF_TRANSPARENT_BLACK_COLOR, Settings.CREAM_COLOR)
        .SetAlignment(0.5f, 0.5f)
        .SetPosition(save_button.hb.cX, cardsValue_text.hb.y + cardsValue_text.hb.height + labelHeight * 0.8f)
        .SetFont(FontHelper.charDescFont, 1);

        affinityValue_text = new GUI_TextBox(GR.Common.Images.Panel_Rounded.Texture(), new Hitbox(labelWidth, labelHeight))
        .SetColors(Settings.HALF_TRANSPARENT_BLACK_COLOR, Settings.CREAM_COLOR)
        .SetAlignment(0.5f, 0.5f)
        .SetPosition(save_button.hb.cX, cardsCount_text.hb.y + cardsCount_text.hb.height + labelHeight * 0.8f)
        .SetFont(FontHelper.charDescFont, 1);

        slotsEditors.add(new AnimatorCardSlotEditor(this, ScreenW(0.135f), ScreenH(0.75f)));
        slotsEditors.add(new AnimatorCardSlotEditor(this, ScreenW(0.335f), ScreenH(0.75f)));
        slotsEditors.add(new AnimatorCardSlotEditor(this, ScreenW(0.135f), ScreenH(0.35f)));
        slotsEditors.add(new AnimatorCardSlotEditor(this, ScreenW(0.335f), ScreenH(0.35f)));
        slotsEditors.add(new AnimatorCardSlotEditor(this, ScreenW(0.635f), ScreenH(0.75f)));
        slotsEditors.add(new AnimatorCardSlotEditor(this, ScreenW(0.835f), ScreenH(0.75f)));

        hpEditor = new AnimatorBaseStatEditor(AnimatorBaseStatEditor.Type.HP, ScreenW(0.666f), ScreenH(0.442f));
        goldEditor = new AnimatorBaseStatEditor(AnimatorBaseStatEditor.Type.Gold, ScreenW(0.666f), ScreenH(0.353f));
    }

    public void Open(AnimatorLoadout loadout, ActionT0 onClose)
    {
        super.Open();

        EYBCardBase.canCropPortraits = false;
        ToggleViewUpgrades(false);

        this.loadout = loadout;
        this.data = loadout.Data.MakeCopy();
        this.onClose = onClose;
        this.hpEditor.SetLoadout(data);
        this.goldEditor.SetLoadout(data);
        SetSlotsActive(true);
    }

    @Override
    public void Dispose()
    {
        super.Dispose();

        EYBCardBase.canCropPortraits = true;
        ToggleViewUpgrades(false);

        if (onClose != null)
        {
            onClose.Invoke();
        }
    }

    @Override
    public void Update()
    {
        super.Update();

        val.Refresh(data);
        background_image.Update();
        upgrade_toggle.SetToggle(SingleCardViewPopup.isViewingUpgrade).Update();

        if (cardSelectionEffect != null)
        {
            cardSelectionEffect.update();

            if (cardSelectionEffect.isDone)
            {
                cardSelectionEffect = null;
                SetSlotsActive(true);
            }
        }
        else
        {
            hpEditor.SetEstimatedValue(val.HpValue).Update();
            goldEditor.SetEstimatedValue(val.GoldValue).Update();
            cancel_button.Update();
            save_button.Update();
        }

        for (AnimatorCardSlotEditor editor : slotsEditors)
        {
            editor.TryUpdate();
        }

        affinityValue_text.SetText("Affinity: +" + val.AffinityLevel).SetActive(val.AffinityLevel > 0).TryUpdate();
        cardsCount_text.SetText("Cards: {0}", val.CardsCount.V1).SetFontColor(val.CardsCount.V2 ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR).TryUpdate();
        cardsValue_text.SetText("Value: {0}/{1}", val.TotalValue.V1, AnimatorLoadout.MAX_VALUE).SetFontColor(val.TotalValue.V2 ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR).TryUpdate();
        save_button.SetInteractable(val.IsValid).TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        super.Render(sb);

        background_image.Render(sb);

        if (cardSelectionEffect != null)
        {
            cardSelectionEffect.render(sb);
        }
        else
        {
            hpEditor.Render(sb);
            goldEditor.Render(sb);
            cancel_button.Render(sb);
            save_button.Render(sb);
        }

        upgrade_toggle.Render(sb);
        affinityValue_text.TryRender(sb);
        cardsCount_text.TryRender(sb);
        cardsValue_text.TryRender(sb);

        for (AnimatorCardSlotEditor editor : slotsEditors)
        {
            editor.TryRender(sb);
        }
    }

    public void TrySelectCard(CardSlot cardSlot)
    {
        cardSelectionEffect = new AnimatorCardSlotSelectionEffect(cardSlot);
        SetSlotsActive(false);
    }

    public void Save()
    {
        loadout.Data = data;
        GR.Animator.Data.SaveLoadouts(true);
        AbstractDungeon.closeCurrentScreen();
    }

    public void ToggleViewUpgrades(boolean value)
    {
        SingleCardViewPopup.isViewingUpgrade = value;
    }

    public void SetSlotsActive(boolean active)
    {
        if (active)
        {
            for (int i = 0; i < slotsEditors.size(); i++)
            {
                AnimatorCardSlotEditor editor = slotsEditors.get(i);
                editor.SetActive(data.Size() > i);
                editor.SetSlot(editor.isActive ? data.Get(i) : null);
            }
        }
        else
        {
            for (AnimatorCardSlotEditor editor : slotsEditors)
            {
                editor.SetActive(false);
            }
        }
    }
}