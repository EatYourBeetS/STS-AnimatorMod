package pinacolada.ui.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.EYBFontHelper;
import pinacolada.cards.base.PCLCardBase;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.misc.PCLCardSlot;
import pinacolada.resources.pcl.misc.PCLLoadout;
import pinacolada.resources.pcl.misc.PCLLoadoutData;
import pinacolada.resources.pcl.misc.PCLRelicSlot;
import pinacolada.ui.AbstractScreen;
import pinacolada.ui.controls.*;
import pinacolada.ui.hitboxes.AdvancedHitbox;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class PCLLoadoutEditor extends AbstractScreen
{
    public static final int MAX_CARD_SLOTS = 6;
    public static final int MAX_RELIC_SLOTS = 2;

    protected final static PCLLoadout.Validation val = new PCLLoadout.Validation();
    protected final ArrayList<PCLCardSlotEditor> slotsEditors = new ArrayList<>();
    protected final ArrayList<PCLRelicSlotEditor> relicsEditors = new ArrayList<>();
    protected final ArrayList<PCLBaseStatEditor> baseStatEditors = new ArrayList<>();
    protected final PCLLoadoutData[] presets = new PCLLoadoutData[PCLLoadout.MAX_PRESETS];
    protected PCLLoadout loadout;
    protected ActionT0 onClose;
    protected int preset;
    public PCLBaseStatEditor activeEditor;
    protected CharacterOption characterOption;

    protected PCLCardSlotSelectionEffect cardSelectionEffect;
    protected PCLRelicSlotSelectionEffect relicSelectionEffect;
    protected GUI_Label startingDeck;
    protected GUI_Label deck_text;
    protected GUI_Label relic_text;
    protected GUI_Label attributes_text;
    protected GUI_Image background_image;
    protected GUI_Button seriesButton;
    protected GUI_Button[] preset_buttons;
    protected GUI_Button cancel_button;
    protected GUI_Button clear_button;
    protected GUI_Button save_button;
    protected GUI_Toggle upgrade_toggle;
    protected GUI_TextBox ascensionRequirement;
    protected GUI_TextBox cardsCount_text;
    protected GUI_TextBox cardsValue_text;
    protected GUI_TextBox affinityValue_text;
    protected GUI_TextBox hindranceValue_text;
    protected PCLCardTooltip infoTip;

    public PCLLoadoutEditor()
    {
        final float buttonHeight = ScreenH(0.07f);
        final float labelHeight = ScreenH(0.04f);
        final float buttonWidth = ScreenW(0.18f);
        final float labelWidth = ScreenW(0.20f);
        final float button_cY = buttonHeight * 1.5f;

        background_image = new GUI_Image(GR.PCL.Images.FullSquare.Texture(), new Hitbox(ScreenW(1), ScreenH(1)))
        .SetPosition(ScreenW(0.5f), ScreenH(0.5f))
        .SetColor(0, 0, 0, 0.9f);

        startingDeck = new GUI_Label(null, new AdvancedHitbox(ScreenW(0.18f), ScreenH(0.05f))
                .SetPosition(ScreenW(0.08f), ScreenH(0.97f)))
                .SetFont(EYBFontHelper.CardDescriptionFont_Normal, 0.9f)
                .SetColor(Settings.CREAM_COLOR);

        deck_text = new GUI_Label(EYBFontHelper.CardTitleFont_Large,
                new Hitbox(ScreenW(0.1f), ScreenH(0.8f), buttonHeight, buttonHeight))
                .SetText(GR.PCL.Strings.CharSelect.DeckHeader)
                .SetFontScale(0.8f)
                .SetAlignment(0.5f, 0.5f);

        relic_text = new GUI_Label(EYBFontHelper.CardTitleFont_Large,
                new Hitbox(ScreenW(0.1f), ScreenH(0.4f), buttonHeight, buttonHeight))
                .SetText(GR.PCL.Strings.CharSelect.RelicsHeader)
                .SetFontScale(0.8f)
                .SetAlignment(0.5f, 0.5f);

        attributes_text = new GUI_Label(EYBFontHelper.CardTitleFont_Large,
                new Hitbox(ScreenW(0.57f), ScreenH(0.8f), buttonHeight, buttonHeight))
                .SetText(GR.PCL.Strings.CharSelect.AttributesHeader)
                .SetFontScale(0.8f)
                .SetAlignment(0.5f, 0.5f);

        seriesButton = new GUI_Button(GR.PCL.Images.Edit.Texture(), new AdvancedHitbox(0, 0, Scale(64), Scale(64)))
                .SetPosition(startingDeck.hb.x + Scale(80), startingDeck.hb.y - Scale(48)).SetText("")
                .SetTooltip(GR.PCL.Strings.CharSelect.SeriesEditor, GR.PCL.Strings.CharSelect.SeriesEditorInfo)
                .SetOnClick(this::OpenSeriesSelect);

        preset_buttons = new GUI_Button[PCLLoadout.MAX_PRESETS];
        for (int i = 0; i < preset_buttons.length; i++)
        {
            //noinspection SuspiciousNameCombination
            preset_buttons[i] = new GUI_Button(GR.PCL.Images.SquaredButton.Texture(), new Hitbox(0, 0, buttonHeight, buttonHeight))
            .SetPosition(ScreenW(0.45f) + ((i - 1f) * buttonHeight), ScreenH(1f) - (buttonHeight * 0.85f))
            .SetText(String.valueOf(i + 1))
            .SetOnClick(i, (preset, __) -> ChangePreset(preset));
        }

        cancel_button = CreateHexagonalButton(0, 0, buttonWidth, buttonHeight)
        .SetPosition(buttonWidth * 0.75f, button_cY)
        .SetColor(Color.FIREBRICK)
        .SetText(GridCardSelectScreen.TEXT[1])
        .SetOnClick(AbstractDungeon::closeCurrentScreen);

        save_button = CreateHexagonalButton(0, 0, buttonWidth, buttonHeight)
        .SetPosition(ScreenW(1) - (buttonWidth * 0.75f), button_cY)
        .SetColor(Color.FOREST)
        .SetText(GridCardSelectScreen.TEXT[0])
        .SetInteractable(false)
        .SetOnClick(this::Save);

        clear_button = CreateHexagonalButton(0, 0, buttonWidth, buttonHeight)
                .SetPosition(save_button.hb.cX, save_button.hb.y + save_button.hb.height + labelHeight * 0.8f)
                .SetColor(Color.WHITE)
                .SetText(GR.PCL.Strings.CharSelect.Clear)
                .SetOnClick(this::Clear);

        upgrade_toggle = new GUI_Toggle(new Hitbox(0, 0, labelWidth * 0.75f, labelHeight))
        .SetPosition(ScreenW(0.5f), ScreenH(0.055f))
        .SetBackground(GR.PCL.Images.Panel_Rounded.Texture(), new Color(0, 0, 0, 0.85f))
        .SetText(SingleCardViewPopup.TEXT[6])
        .SetOnToggle(this::ToggleViewUpgrades);

        cardsValue_text = new GUI_TextBox(GR.PCL.Images.Panel_Rounded.Texture(), new Hitbox(labelWidth, labelHeight))
        .SetColors(Settings.HALF_TRANSPARENT_BLACK_COLOR, Settings.CREAM_COLOR)
        .SetAlignment(0.5f, 0.5f)
        .SetPosition(save_button.hb.cX, ScreenH(0.65f))
        .SetFont(FontHelper.tipHeaderFont, 1);

        cardsCount_text = new GUI_TextBox(GR.PCL.Images.Panel_Rounded.Texture(), new Hitbox(labelWidth, labelHeight))
        .SetColors(Settings.HALF_TRANSPARENT_BLACK_COLOR, Settings.CREAM_COLOR)
        .SetAlignment(0.5f, 0.5f)
        .SetPosition(save_button.hb.cX, cardsValue_text.hb.y + cardsValue_text.hb.height + labelHeight * 0.5f)
        .SetFont(FontHelper.tipHeaderFont, 1);

        affinityValue_text = new GUI_TextBox(GR.PCL.Images.Panel_Rounded.Texture(), new Hitbox(labelWidth, labelHeight))
        .SetColors(Settings.HALF_TRANSPARENT_BLACK_COLOR, Settings.CREAM_COLOR)
        .SetAlignment(0.5f, 0.5f)
        .SetPosition(save_button.hb.cX, cardsCount_text.hb.y + cardsCount_text.hb.height + labelHeight * 0.5f)
        .SetFont(FontHelper.tipHeaderFont, 1);

        hindranceValue_text = new GUI_TextBox(GR.PCL.Images.Panel_Rounded.Texture(), new Hitbox(labelWidth, labelHeight))
                .SetColors(Settings.HALF_TRANSPARENT_BLACK_COLOR, Settings.CREAM_COLOR)
                .SetAlignment(0.5f, 0.5f)
                .SetPosition(save_button.hb.cX, affinityValue_text.hb.y + affinityValue_text.hb.height + labelHeight * 0.5f)
                .SetFont(FontHelper.tipHeaderFont, 1);

        for (int i = 0; i < MAX_CARD_SLOTS; i++) {
            slotsEditors.add(new PCLCardSlotEditor(this, ScreenW(0.1f), ScreenH(0.75f - (i * 0.05f))));
        }

        for (int i = 0; i < MAX_RELIC_SLOTS; i++) {
            relicsEditors.add(new PCLRelicSlotEditor(this, ScreenW(0.1f), ScreenH(0.35f - (i * 0.05f))));
        }

        final PCLBaseStatEditor.StatType[] statTypes = PCLBaseStatEditor.StatType.values();
        for (int i = 0; i < statTypes.length; i++) {
            baseStatEditors.add(new PCLBaseStatEditor(statTypes[i], ScreenW(0.6f), ScreenH(0.78f - i * 0.1f), this));
        }

        ascensionRequirement = new GUI_TextBox(GR.PCL.Images.Panel_Rounded.Texture(), new Hitbox(labelWidth, labelHeight * 4))
        .SetColors(Colors.Black(0.4f), Colors.Cream(0.9f))
        .SetText(GR.PCL.Strings.CharSelect.UnlocksAtAscension(PCLLoadout.GOLD_AND_HP_EDITOR_ASCENSION_REQUIRED))
        .SetAlignment(0.5f, 0.5f)
        .SetPosition(ScreenW(0.62f), ScreenH(0.78f))
        .SetFont(FontHelper.charDescFont, 0.9f);

        infoTip = new PCLCardTooltip("", "");
    }

    public void Open(PCLLoadout loadout, CharacterOption option, ActionT0 onClose)
    {
        super.Open();

        boolean enableHPAndGoldEditor = PCLGameUtilities.GetMaxAscensionLevel(option.c) >= PCLLoadout.GOLD_AND_HP_EDITOR_ASCENSION_REQUIRED;
        ascensionRequirement.SetActive(!enableHPAndGoldEditor);
        for (PCLBaseStatEditor beditor : baseStatEditors) {
            beditor.SetActive(enableHPAndGoldEditor);
            beditor.SetInteractable(enableHPAndGoldEditor);
        }

        for (int i = 0; i < loadout.Presets.length; i++)
        {
            presets[i] = loadout.GetPreset(i).MakeCopy();

            if (!enableHPAndGoldEditor)
            {
                presets[i].Values.put(PCLBaseStatEditor.StatType.HP, 0);
                presets[i].Values.put(PCLBaseStatEditor.StatType.Gold, 0);
            }
        }

        preset_buttons[0].SetInteractable(loadout.CanChangePreset(0));
        preset_buttons[1].SetInteractable(loadout.CanChangePreset(1));
        preset_buttons[2].SetInteractable(loadout.CanChangePreset(2));

        this.loadout = loadout;
        this.onClose = onClose;
        this.characterOption = option;

        startingDeck.SetText(GR.PCL.Strings.CharSelect.LeftText + " NL #y" + (loadout.Series.LocalizedName.replace(" ", " #y")));

        PCLCardBase.canCropPortraits = false;
        ToggleViewUpgrades(false);
        ChangePreset(loadout.Preset);
    }

    @Override
    public void Dispose()
    {
        super.Dispose();

        PCLCardBase.canCropPortraits = true;
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
        startingDeck.Update();
        deck_text.Update();
        relic_text.Update();
        attributes_text.Update();
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
        else if (relicSelectionEffect != null)
        {
            relicSelectionEffect.update();

            if (relicSelectionEffect.isDone)
            {
                relicSelectionEffect = null;
                SetSlotsActive(true);
            }
        }
        else
        {
            seriesButton.TryUpdate();

            for (int i = 0; i < preset_buttons.length; i++)
            {
                final GUI_Button button = preset_buttons[i];
                button
                .ShowTooltip(!button.interactable)
                .SetColor((i == preset) ? Color.SKY : button.interactable ? Color.LIGHT_GRAY : Color.DARK_GRAY)
                .TryUpdate();
            }

            ascensionRequirement.TryUpdate();

            for (PCLBaseStatEditor beditor : baseStatEditors) {
                if (activeEditor == null || activeEditor == beditor) {
                    beditor.SetEstimatedValue(val.Values.getOrDefault(beditor.type, 0)).TryUpdate();
                }
            }

            cancel_button.Update();
            clear_button.Update();
            save_button.Update();

            for (PCLCardSlotEditor editor : slotsEditors)
            {
                editor.TryUpdate();
            }
            for (PCLRelicSlotEditor editor : relicsEditors)
            {
                editor.TryUpdate();
            }
        }

        hindranceValue_text.SetText(GR.PCL.Strings.CharSelect.HindranceValue(val.HindranceLevel)).TryUpdate();
        affinityValue_text.SetText(GR.PCL.Strings.CharSelect.AffinityValue(val.AffinityLevel)).TryUpdate();
        cardsCount_text.SetText(GR.PCL.Strings.CharSelect.CardsCount(val.CardsCount.V1)).SetFontColor(val.CardsCount.V2 ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR).TryUpdate();
        cardsValue_text.SetText(GR.PCL.Strings.CharSelect.TotalValue(val.TotalValue.V1, PCLLoadout.MAX_VALUE)).SetFontColor(val.TotalValue.V2 ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR).TryUpdate();
        save_button.SetInteractable(val.IsValid).TryUpdate();

        if (hindranceValue_text.hb.hovered) {
            infoTip.SetText(hindranceValue_text.label.text, GR.PCL.Strings.CharSelect.HindranceDescription);
            PCLCardTooltip.QueueTooltip(infoTip);
        }
        else if (affinityValue_text.hb.hovered) {
            infoTip.SetText(affinityValue_text.label.text, GR.PCL.Strings.CharSelect.AffinityDescription);
            PCLCardTooltip.QueueTooltip(infoTip);
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        super.Render(sb);

        background_image.Render(sb);

        if (relicSelectionEffect != null)
        {
            relicSelectionEffect.render(sb);
        }
        else if (cardSelectionEffect != null)
        {
            cardSelectionEffect.render(sb);
        }
        else
        {
            seriesButton.TryRender(sb);

            for (GUI_Button button : preset_buttons)
            {
                button.TryRender(sb);
            }

            startingDeck.Render(sb);
            deck_text.Render(sb);
            relic_text.Render(sb);
            attributes_text.Render(sb);

            // All editors must be rendered from top to bottom to prevent dropdowns from overlapping
            for (int i = baseStatEditors.size() - 1; i >= 0; i--)
            {
                baseStatEditors.get(i).TryRender(sb);
            }

            ascensionRequirement.TryRender(sb);
            cancel_button.Render(sb);
            clear_button.Render(sb);
            save_button.Render(sb);
            upgrade_toggle.Render(sb);
            hindranceValue_text.TryRender(sb);
            affinityValue_text.TryRender(sb);
            cardsCount_text.TryRender(sb);
            cardsValue_text.TryRender(sb);

            for (int i = relicsEditors.size() - 1; i >= 0; i--)
            {
                relicsEditors.get(i).TryRender(sb);
            }

            for (int i = slotsEditors.size() - 1; i >= 0; i--)
            {
                slotsEditors.get(i).TryRender(sb);
            }
        }
    }

    private void OpenSeriesSelect()
    {
        if (characterOption != null)
        {
            GR.UI.SeriesSelection.Open(characterOption, this.onClose);
        }
    }

    public void RepositionSlotEditor(PCLCardSlotEditor cardSlotEditor, int index) {
        cardSlotEditor.Translate(ScreenW(0.1f), ScreenH(0.75f - (index * 0.05f)));
    }

    public void TrySelectCard(PCLCardSlot cardSlot)
    {
        cardSelectionEffect = new PCLCardSlotSelectionEffect(cardSlot);
        SetSlotsActive(false);
    }

    public void TrySelectRelic(PCLRelicSlot relicSlot)
    {
        relicSelectionEffect = new PCLRelicSlotSelectionEffect(relicSlot);
        SetSlotsActive(false);
    }

    public void ChangePreset(int preset)
    {
        this.preset = preset;
        SetSlotsActive(true);
    }

    public void Clear()
    {
        PCLLoadoutData defaultData = loadout.GetDefaultData(preset);
        presets[preset] = defaultData;
        SetSlotsActive(true);
    }

    public void Save()
    {
        for (int i = 0, presetsLength = presets.length; i < presetsLength; i++)
        {
            loadout.Presets[i] = presets[i];
        }

        loadout.Preset = preset;
        GR.PCL.Data.SaveLoadouts(true);
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
            final PCLLoadoutData data = presets[preset];
            for (int i = 0; i < slotsEditors.size(); i++)
            {
                final PCLCardSlotEditor editor = slotsEditors.get(i);
                editor.SetActive(data.CardsSize() > i);
                editor.SetSlot(editor.isActive ? data.GetCardSlot(i) : null);
            }
            for (int i = 0; i < relicsEditors.size(); i++)
            {
                final PCLRelicSlotEditor reditor = relicsEditors.get(i);
                reditor.SetActive(data.RelicsSize() > i);
                reditor.SetSlot(reditor.isActive ? data.GetRelicSlot(i) : null);
            }
            for (PCLBaseStatEditor beditor : baseStatEditors) {
                beditor.SetLoadout(data);
            }
            val.Refresh(presets[preset]);
        }
        else
        {
            for (PCLCardSlotEditor editor : slotsEditors)
            {
                editor.SetActive(false);
            }
        }
    }
}