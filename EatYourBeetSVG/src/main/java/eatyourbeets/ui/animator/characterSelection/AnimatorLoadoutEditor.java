package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.CardSlot;
import eatyourbeets.ui.AbstractScreen;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_TextBox;

import java.util.ArrayList;

public class AnimatorLoadoutEditor extends AbstractScreen
{
    protected final ArrayList<AnimatorCardSlotEditor> slotsEditors = new ArrayList<>();
    protected GUI_Image background_image;
    protected GUI_Button cancel_button;
    protected GUI_Button save_button;
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
        .SetOnClick(AbstractDungeon::closeCurrentScreen);

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

        slotsEditors.add(new AnimatorCardSlotEditor(ScreenW(0.135f), ScreenH(0.75f)));
        slotsEditors.add(new AnimatorCardSlotEditor(ScreenW(0.335f), ScreenH(0.75f)));
        slotsEditors.add(new AnimatorCardSlotEditor(ScreenW(0.135f), ScreenH(0.35f)));
        slotsEditors.add(new AnimatorCardSlotEditor(ScreenW(0.335f), ScreenH(0.35f)));
        slotsEditors.add(new AnimatorCardSlotEditor(ScreenW(0.635f), ScreenH(0.75f)));
        slotsEditors.add(new AnimatorCardSlotEditor(ScreenW(0.835f), ScreenH(0.75f)));
    }

    public void Open(AnimatorLoadout loadout)
    {
        super.Open();

        slotsEditors.get(0).SetSlot(loadout.Slot_Defend);
        slotsEditors.get(1).SetSlot(loadout.Slot_Strike);
        slotsEditors.get(2).SetSlot(loadout.Slot_ImprovedDefend);
        slotsEditors.get(3).SetSlot(loadout.Slot_ImprovedStrike);
        slotsEditors.get(4).SetSlot(loadout.Slot_Series1);
        slotsEditors.get(5).SetSlot(loadout.Slot_Series2);
    }

    @Override
    public void Update()
    {
        super.Update();

        background_image.Update();
        cancel_button.Update();
        save_button.Update();

        int cards = 0;
        int value = CardSlot.MAX_VALUE;
        int affinityLevel = 0;
        EYBCardAffinities affinities = new EYBCardAffinities(null);
        for (AnimatorCardSlotEditor editor : slotsEditors)
        {
            editor.TryUpdate();

            if (editor.slot != null)
            {
                affinities.AddLevels(editor.slot.GetAffinities(), 1);
                value += editor.slot.GetEstimatedValue();
                cards += editor.slot.amount;
            }
        }

        for (AffinityType t : AffinityType.BasicTypes())
        {
            int level = affinities.GetLevel(t, false);
            if (level > 2)
            {
                affinityLevel += (level - 2);
            }
        }

        value += affinityLevel;

        affinityValue_text.SetText("Affinity: +" + affinityLevel)
        .SetActive(affinityLevel > 0).TryUpdate();
        cardsCount_text.SetText("Cards: {0}", cards)
        .SetFontColor(cards >= 10 ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR).TryUpdate();
        cardsValue_text.SetText("Value: {0}/{1}", value, CardSlot.MAX_VALUE)
        .SetFontColor(value <= CardSlot.MAX_VALUE ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR).TryUpdate();
        save_button.SetInteractable(value <= CardSlot.MAX_VALUE && cards >= 10).TryUpdate();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        super.Render(sb);

        background_image.Render(sb);
        cancel_button.Render(sb);
        save_button.Render(sb);
        affinityValue_text.TryRender(sb);
        cardsCount_text.TryRender(sb);
        cardsValue_text.TryRender(sb);

        for (AnimatorCardSlotEditor editor : slotsEditors)
        {
            editor.TryRender(sb);
        }
    }
}