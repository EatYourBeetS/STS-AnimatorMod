package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import eatyourbeets.cards.base.EYBCardBase;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorCardSlot;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorLoadoutData;
import eatyourbeets.resources.animator.misc.AnimatorTrophies;
import eatyourbeets.ui.AbstractScreen;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_TextBox;
import eatyourbeets.ui.controls.GUI_Toggle;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class AnimatorLoadoutEditor extends AbstractScreen
{
    protected final static EYBCardTooltip bronzeRequiredTooltip = new EYBCardTooltip("Locked", "");
    protected final static AnimatorLoadout.Validation val = new AnimatorLoadout.Validation();
    protected final ArrayList<AnimatorCardSlotEditor> slotsEditors = new ArrayList<>();
    protected final AnimatorLoadoutData[] presets = new AnimatorLoadoutData[AnimatorLoadout.MAX_PRESETS];
    protected AnimatorBaseStatEditor goldEditor;
    protected AnimatorBaseStatEditor hpEditor;
    protected AnimatorLoadout loadout;
    protected ActionT0 onClose;
    protected int preset;

    protected AnimatorCardSlotSelectionEffect cardSelectionEffect;
    protected GUI_Image background_image;
    protected GUI_Button[] preset_buttons;
    protected GUI_Button cancel_button;
    protected GUI_Button save_button;
    protected GUI_Toggle upgrade_toggle;
    protected GUI_TextBox ascensionRequirement;
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

        preset_buttons = new GUI_Button[AnimatorLoadout.MAX_PRESETS];
        for (int i = 0; i < preset_buttons.length; i++)
        {
            //noinspection SuspiciousNameCombination
            preset_buttons[i] = new GUI_Button(GR.Common.Images.SquaredButton.Texture(), new Hitbox(0, 0, buttonHeight, buttonHeight))
            .SetPosition(ScreenW(0.5f) + ((i - 1f) * buttonHeight), ScreenH(1f) - (buttonHeight * 0.85f))
            .SetText(String.valueOf(i + 1))
            .SetOnClick(i, (preset, __) -> ChangePreset(preset));
        }

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
        slotsEditors.add(new AnimatorCardSlotEditor(this, ScreenW(0.685f), ScreenH(0.75f)));
        slotsEditors.add(new AnimatorCardSlotEditor(this, ScreenW(0.885f), ScreenH(0.75f)));

        hpEditor = new AnimatorBaseStatEditor(AnimatorBaseStatEditor.Type.HP, ScreenW(0.666f), ScreenH(0.432f));
        goldEditor = new AnimatorBaseStatEditor(AnimatorBaseStatEditor.Type.Gold, ScreenW(0.666f), ScreenH(0.343f));

        ascensionRequirement = new GUI_TextBox(GR.Common.Images.Panel_Rounded.Texture(), new Hitbox(labelWidth, labelHeight * 4))
        .SetColors(Colors.Black(0.4f), Colors.Cream(0.9f))
        .SetText(GR.Animator.Strings.CharSelect.UnlocksAtAscension(AnimatorLoadout.GOLD_AND_HP_EDITOR_ASCENSION_REQUIRED))
        .SetAlignment(0.5f, 0.5f)
        .SetPosition(ScreenW(0.666f), ScreenH(0.37f))
        .SetFont(FontHelper.charDescFont, 0.9f);
    }

    public void Open(AnimatorLoadout loadout, CharacterOption option, ActionT0 onClose)
    {
        super.Open();

        boolean enableHPAndGoldEditor = GameUtilities.GetMaxAscensionLevel(option.c) >= AnimatorLoadout.GOLD_AND_HP_EDITOR_ASCENSION_REQUIRED;
        ascensionRequirement.SetActive(!enableHPAndGoldEditor);
        goldEditor.SetInteractable(enableHPAndGoldEditor);
        hpEditor.SetInteractable(enableHPAndGoldEditor);

        for (int i = 0; i < loadout.Presets.length; i++)
        {
            presets[i] = loadout.GetPreset(i).MakeCopy();

            if (!enableHPAndGoldEditor)
            {
                presets[i].Gold = AnimatorLoadout.BASE_GOLD;
                presets[i].HP = AnimatorLoadout.BASE_HP;
            }
        }

        final AnimatorTrophies trophies = loadout.GetTrophies();
        final int bronze = trophies == null ? 20 : trophies.Trophy1;
        preset_buttons[0].SetInteractable(true);
        preset_buttons[1].SetInteractable(bronze >= AnimatorLoadout.BRONZE_REQUIRED_PRESET_SLOT_2);
        preset_buttons[2].SetInteractable(bronze >= AnimatorLoadout.BRONZE_REQUIRED_PRESET_SLOT_3);

        this.loadout = loadout;
        this.onClose = onClose;

        EYBCardBase.canCropPortraits = false;
        ToggleViewUpgrades(false);
        ChangePreset(loadout.Preset);
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

        val.Refresh(presets[preset]);
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
            for (int i = 0; i < preset_buttons.length; i++)
            {
                final GUI_Button button = preset_buttons[i];
                button.SetColor((i == preset) ? Color.SKY : button.interactable ? Color.LIGHT_GRAY : Color.DARK_GRAY).TryUpdate();

                if (button.hb.hovered && !button.interactable)
                {
                    // TODO: localization
                    final int ascension = (i == 1) ? AnimatorLoadout.BRONZE_REQUIRED_PRESET_SLOT_2 : AnimatorLoadout.BRONZE_REQUIRED_PRESET_SLOT_3;
                    bronzeRequiredTooltip.description = JUtils.Format("Obtain #yBronze #yTrophy at ascension #b{0} to unlock.", ascension);
                    EYBCardTooltip.QueueTooltip(bronzeRequiredTooltip, InputHelper.mX + (button.hb.width * 0.5f), InputHelper.mY - (button.hb.height * 0.5f));
                }
            }

            ascensionRequirement.TryUpdate();
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
            for (GUI_Button button : preset_buttons)
            {
                button.TryRender(sb);
            }

            hpEditor.Render(sb);
            goldEditor.Render(sb);
            ascensionRequirement.TryRender(sb);
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

    public void TrySelectCard(AnimatorCardSlot cardSlot)
    {
        cardSelectionEffect = new AnimatorCardSlotSelectionEffect(cardSlot);
        SetSlotsActive(false);
    }

    public void ChangePreset(int preset)
    {
        if (!preset_buttons[2].interactable && preset >= 2)
        {
            preset = 1;
        }
        if (!preset_buttons[1].interactable && preset == 1)
        {
            preset = 0;
        }

        this.preset = preset;
        this.hpEditor.SetLoadout(presets[preset]);
        this.goldEditor.SetLoadout(presets[preset]);
        SetSlotsActive(true);
    }

    public void Save()
    {
        for (int i = 0, presetsLength = presets.length; i < presetsLength; i++)
        {
            loadout.Presets[i] = presets[i];
        }

        loadout.Preset = preset;
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
                final AnimatorLoadoutData data = presets[preset];
                final AnimatorCardSlotEditor editor = slotsEditors.get(i);
                editor.SetActive(data.Size() > i);
                editor.SetSlot(editor.isActive ? data.GetCardSlot(i) : null);
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