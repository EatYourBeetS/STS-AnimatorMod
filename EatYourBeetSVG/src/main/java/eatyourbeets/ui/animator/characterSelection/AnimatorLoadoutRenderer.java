package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Dropdown;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class AnimatorLoadoutRenderer extends GUIElement
{
    protected static final FieldInfo<String> _hp = JUtils.GetField("hp", CharacterOption.class);
    protected static final FieldInfo<Integer> _gold = JUtils.GetField("gold", CharacterOption.class);
    protected static final float POS_X = 250f * Settings.scale;
    protected static final float POS_Y = ((float) Settings.HEIGHT / 2f) - (20 * Settings.scale);
    protected static final float ROW_OFFSET = 60 * Settings.scale;

    protected static final Random RNG = new Random();

    protected final ArrayList<AnimatorLoadout> availableLoadouts;
    protected final ArrayList<AnimatorLoadout> loadouts;

    protected GUI_Dropdown<AnimatorLoadout> SeriesDropdown;
    protected GUI_Button SeriesLeftButton;
    protected GUI_Button SeriesRightButton;
    protected GUI_Button RandomizeButton;
    protected GUI_Button LoadoutEditorButton;
    protected GUI_Label StartingCardsLabel;
    protected GUI_Label StartingCardsListLabel;
    protected GUI_Label SeriesLabel;
    protected CharacterSelectScreen selectScreen;
    protected CharacterOption characterOption;
    protected AnimatorLoadout loadout;

    protected float textScale;

    public AnimatorLoadoutRenderer()
    {
        final float leftTextWidth = FontHelper.getSmartWidth(FontHelper.cardTitleFont, GR.Animator.Strings.CharSelect.LeftText, 9999f, 0f); // Ascension
        final float rightTextWidth = FontHelper.getSmartWidth(FontHelper.cardTitleFont, GR.Animator.Strings.CharSelect.RightText, 9999f, 0f); // Level 22

        textScale = Settings.scale;

        //Need to prevent text from disappearing from scaling too big on 4K resolutions
        if (textScale > 1)
        {
            textScale = 1;
        }

        float POS_X = 330f * Settings.scale;
        float POS_Y = ((float) Settings.HEIGHT / 2f) - (20 * Settings.scale);

        loadouts = new ArrayList<>();
        availableLoadouts = new ArrayList<>();

        StartingCardsLabel = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
                new AdvancedHitbox(POS_X, POS_Y, leftTextWidth, 50f * Settings.scale))
                .SetColor(Settings.GOLD_COLOR)
                .SetAlignment(0.5f, 0.5f, false)
                .SetText(GR.Animator.Strings.CharSelect.LeftText);

        SeriesLabel = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
                new AdvancedHitbox(POS_X, POS_Y - ROW_OFFSET, leftTextWidth, 50f * Settings.scale))
                .SetColor(Settings.GOLD_COLOR)
                .SetAlignment(0.5f, 0.5f, false)
                .SetText(GR.Animator.Strings.CharSelect.RightText);

        SeriesLeftButton = new GUI_Button(ImageMaster.CF_LEFT_ARROW, new AdvancedHitbox(StartingCardsLabel.hb.x + StartingCardsLabel.hb.width, SeriesLabel.hb.y, Scale(48), Scale(48)))
                .SetText("")
                .SetOnClick(() -> {
                    ChangeLoadout(loadouts.indexOf(loadout) - 1);
                });

        SeriesDropdown = new GUI_Dropdown<AnimatorLoadout>(new AdvancedHitbox(SeriesLeftButton.hb.x + SeriesLeftButton.hb.width + (20 * Settings.scale), SeriesLabel.hb.y, Scale(240), Scale(48)), cs -> cs.Series.LocalizedName)
                .SetOnChange(selectedSeries -> {
                    ChangeLoadout(selectedSeries.get(0));
                });

        SeriesRightButton = new GUI_Button(ImageMaster.CF_RIGHT_ARROW, new AdvancedHitbox(SeriesDropdown.hb.x + SeriesDropdown.hb.width + (10 * Settings.scale), SeriesLabel.hb.y, Scale(48), Scale(48)))
                .SetText("")
                .SetOnClick(() -> {
                    ChangeLoadout(loadouts.indexOf(loadout) + 1);
                });

        StartingCardsListLabel = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
                new AdvancedHitbox(SeriesDropdown.hb.x, POS_Y, leftTextWidth, 50f * Settings.scale))
                .SetFont(EYBFontHelper.CardTitleFont_Small, 0.8f)
                .SetAlignment(0.5f, 0.5f, false);

        RandomizeButton = new GUI_Button(GR.Common.Images.Randomize.Texture(), new AdvancedHitbox(0, 0, Scale(64), Scale(64)))
                .SetPosition(SeriesRightButton.hb.x + SeriesRightButton.hb.width + Scale(40), SeriesLabel.hb.y + Scale(25)).SetText("")
                .SetOnClick(this::RandomizeLoadout);

        LoadoutEditorButton = new GUI_Button(GR.Common.Images.SwapCards.Texture(), new AdvancedHitbox(0, 0, Scale(64), Scale(64)))
                .SetPosition(SeriesRightButton.hb.x + SeriesRightButton.hb.width + Scale(40), StartingCardsLabel.hb.y + Scale(25)).SetText("")
                .SetTooltip(GR.Animator.Strings.CharSelect.DeckEditor, GR.Animator.Strings.CharSelect.DeckEditorInfo)
                .SetOnRightClick(this::ChangePreset)
                .SetOnClick(this::OpenLoadoutEditor);
    }

    private void OpenLoadoutEditor()
    {
        if (loadout != null && characterOption != null)
        {
            GR.UI.LoadoutEditor.Open(loadout, characterOption, () -> RefreshInternal(false));
        }
    }

    private void ChangeLoadout(int index)
    {
        int actualIndex = index % loadouts.size();
        if (actualIndex < 0) {
            actualIndex = loadouts.size() - 1;
        }
        GR.Animator.Data.SelectedLoadout = loadouts.get(actualIndex);
        Refresh(selectScreen, characterOption);
    }

    private void ChangeLoadout(AnimatorLoadout loadout)
    {
        GR.Animator.Data.SelectedLoadout = loadout;
        Refresh(selectScreen, characterOption);
    }

    private void ChangePreset()
    {
        final int preset = loadout.CanChangePreset(loadout.Preset + 1) ? (loadout.Preset + 1) : 0;
        if (preset != loadout.Preset)
        {
            loadout.Preset = preset;
            RefreshInternal(false);
        }
    }

    public void RandomizeLoadout()
    {
        if (availableLoadouts.size() > 1)
        {
            while (loadout == GR.Animator.Data.SelectedLoadout)
            {
                GR.Animator.Data.SelectedLoadout = GameUtilities.GetRandomElement(availableLoadouts, RNG);
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

        final int unlockLevel = GR.Animator.GetUnlockLevel();
        for (AnimatorLoadout loadout : GR.Animator.Data.BaseLoadouts)
        {
            this.loadouts.add(loadout);
            if (unlockLevel >= loadout.UnlockLevel)
            {
                this.availableLoadouts.add(loadout);
            }
        }

        if (GR.Animator.Config.DisplayBetaSeries.Get())
        {
            for (AnimatorLoadout loadout : GR.Animator.Data.BetaLoadouts)
            {
                if (loadout.GetPreset().Size() > 0)
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
            final int level = GR.Animator.GetUnlockLevel();
            final int levelA = a.UnlockLevel - level;
            final int levelB = b.UnlockLevel - level;
            if (levelA > 0 || levelB > 0)
            {
                return diff + Integer.compare(levelA, levelB) * 1313;
            }

            return diff;
        });

        this.loadout = GR.Animator.Data.SelectedLoadout;
        if (this.loadout.GetStartingDeck().isEmpty() || !loadouts.contains(this.loadout))
        {
            this.loadout = GR.Animator.Data.SelectedLoadout = loadouts.get(0);
        }

        RefreshInternal(true);

        RandomizeButton.SetActive(availableLoadouts.size() > 1);
        AnimatorCharacterSelectScreen.TrophiesRenderer.Refresh(loadout);
        AnimatorCharacterSelectScreen.SpecialTrophiesRenderer.Refresh();

        this.SeriesDropdown.SetItems(this.loadouts);
        this.SeriesDropdown.SetSelection(GR.Animator.Data.SelectedLoadout, false);
    }

    public void RefreshInternal(boolean refreshPortrait)
    {
        _gold.Set(characterOption, loadout.GetGold());
        _hp.Set(characterOption, String.valueOf(loadout.GetHP()));

        if (refreshPortrait)
        {
            selectScreen.bgCharImg = GR.Animator.Images.GetCharacterPortrait(loadout.ID);
        }

        int currentLevel = GR.Animator.GetUnlockLevel();
        if (currentLevel < loadout.UnlockLevel)
        {
            StartingCardsListLabel.SetText(GR.Animator.Strings.CharSelect.UnlocksAtLevel(loadout.UnlockLevel, currentLevel)).SetColor(Settings.RED_TEXT_COLOR);
            LoadoutEditorButton.SetInteractable(false);
            selectScreen.confirmButton.isDisabled = true;
        }
        else if (!loadout.Validate().IsValid)
        {
            StartingCardsListLabel.SetText(GR.Animator.Strings.CharSelect.InvalidLoadout).SetColor(Settings.RED_TEXT_COLOR);
            LoadoutEditorButton.SetInteractable(true);
            selectScreen.confirmButton.isDisabled = true;
        }
        else
        {
            StartingCardsListLabel.SetText(loadout.GetDeckPreviewString(true)).SetColor(Settings.GREEN_TEXT_COLOR);
            LoadoutEditorButton.SetInteractable(true);
            selectScreen.confirmButton.isDisabled = false;
        }
    }

    public void Update()
    {
        RandomizeButton.TryUpdate();
        LoadoutEditorButton.TryUpdate();
        SeriesDropdown.TryUpdate();
        SeriesLeftButton.TryUpdate();
        SeriesRightButton.TryUpdate();
        StartingCardsLabel.Update();
        StartingCardsListLabel.Update();
        SeriesLabel.Update();
    }

    public void Render(SpriteBatch sb)
    {
        RandomizeButton.TryRender(sb);
        LoadoutEditorButton.TryRender(sb);
        SeriesDropdown.TryRender(sb);
        SeriesLeftButton.TryRender(sb);
        SeriesRightButton.TryRender(sb);
        StartingCardsLabel.Render(sb);
        StartingCardsListLabel.Render(sb);
        SeriesLabel.Render(sb);
    }
}