package pinacolada.ui.characterSelection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.FieldInfo;
import pinacolada.blights.common.AbstractGlyphBlight;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.misc.PCLLoadout;
import pinacolada.ui.controls.GUI_Button;
import pinacolada.ui.controls.GUI_Label;
import pinacolada.ui.controls.GUI_TextBox;
import pinacolada.ui.hitboxes.AdvancedHitbox;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

import static pinacolada.resources.pcl.misc.PCLLoadout.BRONZE_REQUIRED_EXPANSION;

public class PCLLoadoutRenderer extends GUIElement
{
    private final static PCLCardTooltip ExpansionLockedTooltip = new PCLCardTooltip(GR.PCL.Strings.SeriesSelection.ExpansionHeader, PCLJUtils.Format(GR.PCL.Strings.SeriesSelection.ExpansionSeriesLocked, BRONZE_REQUIRED_EXPANSION));
    private final static PCLCardTooltip ExpansionUnlockedTooltip = new PCLCardTooltip(GR.PCL.Strings.SeriesSelection.ExpansionHeader, GR.PCL.Strings.SeriesSelection.ExpansionSeriesUnlocked);

    protected static final FieldInfo<String> _hp = PCLJUtils.GetField("hp", CharacterOption.class);
    protected static final FieldInfo<Integer> _gold = PCLJUtils.GetField("gold", CharacterOption.class);
    protected static final FieldInfo<CharSelectInfo> _charSelectInfo = PCLJUtils.GetField("charInfo", CharacterOption.class);
    protected static final float POS_X = 170f * Settings.scale;
    protected static final float POS_Y = ((float) Settings.HEIGHT / 2f) - (20 * Settings.scale);
    protected static final float ROW_OFFSET = 60 * Settings.scale;

    protected static final Random RNG = new Random();

    protected final ArrayList<PCLLoadout> availableLoadouts;
    protected final ArrayList<PCLLoadout> loadouts;
    protected final ArrayList<PCLGlyphEditor> glyphEditors;

    protected GUI_Button SeriesButton;
    protected GUI_Button LoadoutEditorButton;
    protected GUI_Label StartingCardsLabel;
    protected GUI_Label AscensionGlyphsLabel;
    protected GUI_TextBox StartingCardsListLabel;
    protected CharacterSelectScreen selectScreen;
    protected CharacterOption characterOption;
    protected PCLLoadout loadout;

    protected float textScale;

    public PCLLoadoutRenderer()
    {
        final float leftTextWidth = FontHelper.getSmartWidth(FontHelper.cardTitleFont, GR.PCL.Strings.CharSelect.LeftText, 9999f, 0f); // Ascension
        final float rightTextWidth = FontHelper.getSmartWidth(FontHelper.cardTitleFont, GR.PCL.Strings.CharSelect.RightText, 9999f, 0f); // Level 22

        textScale = Settings.scale;

        //Need to prevent text from disappearing from scaling too big on 4K resolutions
        if (textScale > 1)
        {
            textScale = 1;
        }

        loadouts = new ArrayList<>();
        availableLoadouts = new ArrayList<>();
        glyphEditors = new ArrayList<>();

        StartingCardsLabel = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
                new AdvancedHitbox(POS_X, POS_Y, leftTextWidth, 50f * Settings.scale))
                .SetColor(Settings.GOLD_COLOR)
                .SetAlignment(0.5f, 0.5f, false)
                .SetText(GR.PCL.Strings.CharSelect.LeftText);

        StartingCardsListLabel = new GUI_TextBox(GR.PCL.Images.Panel_Rounded.Texture(),
                new AdvancedHitbox(POS_X + ROW_OFFSET * 3.5f, POS_Y, leftTextWidth, 50f * Settings.scale))
                .SetColors(Settings.HALF_TRANSPARENT_BLACK_COLOR, Settings.GREEN_TEXT_COLOR)
                .SetFont(EYBFontHelper.CardTitleFont_Small, 0.8f)
                .SetAlignment(0.5f, 0.5f, false);

        SeriesButton = new GUI_Button(GR.PCL.Images.Edit.Texture(), new AdvancedHitbox(0, 0, Scale(64), Scale(64)))
                .SetPosition(StartingCardsListLabel.hb.x + Scale(240), StartingCardsListLabel.hb.y + Scale(192)).SetText("")
                .SetTooltip(GR.PCL.Strings.CharSelect.SeriesEditor, GR.PCL.Strings.CharSelect.SeriesEditorInfo)
                .SetOnClick(this::OpenSeriesSelect);

        LoadoutEditorButton = new GUI_Button(GR.PCL.Images.SwapCards.Texture(), new AdvancedHitbox(0, 0, Scale(64), Scale(64)))
                .SetPosition(SeriesButton.hb.x + SeriesButton.hb.width + Scale(40), StartingCardsListLabel.hb.y + Scale(192)).SetText("")
                .SetTooltip(GR.PCL.Strings.CharSelect.DeckEditor, GR.PCL.Strings.CharSelect.DeckEditorInfo)
                .SetOnRightClick(this::ChangePreset)
                .SetOnClick(this::OpenLoadoutEditor);

        AscensionGlyphsLabel = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
                new AdvancedHitbox(POS_X * 6, POS_Y, leftTextWidth, 50f * Settings.scale))
                .SetColor(Settings.GOLD_COLOR)
                .SetAlignment(0.5f, 0.5f, false)
                .SetText(GR.PCL.Strings.CharSelect.AscensionGlyph);

        float xOffset = AscensionGlyphsLabel.hb.x + ROW_OFFSET * 4f;
        for (AbstractGlyphBlight glyph : GR.PCL.Data.Glyphs) {
            glyphEditors.add(new PCLGlyphEditor(glyph, new AdvancedHitbox(xOffset, POS_Y, glyph.hb.width, glyph.hb.height)));
            xOffset += ROW_OFFSET * 1.7f;
        }
    }

    private void OpenLoadoutEditor()
    {
        if (loadout != null && characterOption != null)
        {
            GR.UI.LoadoutEditor.Open(loadout, characterOption, () -> Refresh(selectScreen, characterOption));
        }
    }

    private void OpenSeriesSelect()
    {
        if (characterOption != null)
        {
            GR.UI.SeriesSelection.Open(characterOption, () -> Refresh(selectScreen, characterOption));
        }
    }

    private void ChangeLoadout(int index)
    {
        int actualIndex = index % loadouts.size();
        if (actualIndex < 0) {
            actualIndex = loadouts.size() - 1;
        }
        GR.PCL.Data.SelectedLoadout = loadouts.get(actualIndex);
        Refresh(selectScreen, characterOption);
    }

    private void ChangeLoadout(PCLLoadout loadout)
    {
        GR.PCL.Data.SelectedLoadout = loadout;
        Refresh(selectScreen, characterOption);
    }

    private void ChangePreset()
    {
        final int preset = loadout.CanChangePreset(loadout.Preset + 1) ? (loadout.Preset + 1) : 0;
        if (preset != loadout.Preset)
        {
            loadout.Preset = preset;
            RefreshInternal();
        }
    }

    public void RandomizeLoadout()
    {
        if (availableLoadouts.size() > 1)
        {
            while (loadout == GR.PCL.Data.SelectedLoadout)
            {
                GR.PCL.Data.SelectedLoadout = PCLGameUtilities.GetRandomElement(availableLoadouts, RNG);
            }

            Refresh(selectScreen, characterOption);
        }
    }

    public void Refresh(CharacterSelectScreen selectScreen, CharacterOption characterOption)
    {
        this.selectScreen = selectScreen;
        this.characterOption = characterOption;

        this.loadouts.clear();
        this.availableLoadouts.clear();

        final int unlockLevel = GR.PCL.GetUnlockLevel();
        for (PCLLoadout loadout : GR.PCL.Data.BaseLoadouts)
        {
            this.loadouts.add(loadout);
            if (unlockLevel >= loadout.UnlockLevel)
            {
                this.availableLoadouts.add(loadout);
            }
        }

        if (GR.PCL.Config.DisplayBetaSeries.Get())
        {
            for (PCLLoadout loadout : GR.PCL.Data.BetaLoadouts)
            {
                if (loadout.GetPreset().CardsSize() > 0)
                {
                    this.loadouts.add(loadout);
                    if (unlockLevel >= loadout.UnlockLevel)
                    {
                        this.availableLoadouts.add(loadout);
                    }
                }
            }
        }

        this.loadouts.sort((a, b) ->
        {
            final int diff = a.Name.compareTo(b.Name);
            final int level = GR.PCL.GetUnlockLevel();
            final int levelA = a.UnlockLevel - level;
            final int levelB = b.UnlockLevel - level;
            if (levelA > 0 || levelB > 0)
            {
                return diff + Integer.compare(levelA, levelB) * 1313;
            }

            return diff;
        });

        this.loadout = GR.PCL.Data.SelectedLoadout;
        if (this.loadout.GetStartingDeck().isEmpty() || !loadouts.contains(this.loadout))
        {
            this.loadout = GR.PCL.Data.SelectedLoadout = loadouts.get(0);
        }

        for (PCLGlyphEditor geditor : glyphEditors) {
            geditor.Refresh(selectScreen.ascensionLevel);
        }

        RefreshInternal();

        SeriesButton.SetActive(true);
    }

    public void RefreshInternal()
    {
        _gold.Set(characterOption, loadout.GetGold());
        _hp.Set(characterOption, String.valueOf(loadout.GetHP()));
        _charSelectInfo.Get(characterOption).relics = loadout.GetStartingRelics();


        int currentLevel = GR.PCL.GetUnlockLevel();
        if (currentLevel < loadout.UnlockLevel)
        {
            StartingCardsListLabel.SetText(GR.PCL.Strings.CharSelect.UnlocksAtLevel(loadout.UnlockLevel, currentLevel)).SetFontColor(Settings.RED_TEXT_COLOR);
            LoadoutEditorButton.SetInteractable(false);
            selectScreen.confirmButton.isDisabled = true;
        }
        else if (!loadout.Validate().IsValid)
        {
            StartingCardsListLabel.SetText(GR.PCL.Strings.CharSelect.InvalidLoadout).SetFontColor(Settings.RED_TEXT_COLOR);
            LoadoutEditorButton.SetInteractable(true);
            selectScreen.confirmButton.isDisabled = true;
        }
        else
        {
            StartingCardsListLabel.SetText(loadout.GetDeckPreviewString(true)).SetFontColor(Settings.GREEN_TEXT_COLOR);
            LoadoutEditorButton.SetInteractable(true);
            selectScreen.confirmButton.isDisabled = false;
        }
    }

    public void Update()
    {
        SeriesButton.TryUpdate();
        LoadoutEditorButton.TryUpdate();
        StartingCardsLabel.Update();
        StartingCardsListLabel.Update();
        AscensionGlyphsLabel.Update();
        for (PCLGlyphEditor geditor : glyphEditors) {
            geditor.Update();
        }
    }

    public void Render(SpriteBatch sb)
    {
        SeriesButton.TryRender(sb);
        LoadoutEditorButton.TryRender(sb);
        StartingCardsLabel.Render(sb);
        StartingCardsListLabel.Render(sb);
        AscensionGlyphsLabel.Render(sb);
        for (PCLGlyphEditor geditor : glyphEditors) {
            geditor.Render(sb);
        }
    }
}