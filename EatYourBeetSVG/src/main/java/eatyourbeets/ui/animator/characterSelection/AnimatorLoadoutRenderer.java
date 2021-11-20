package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Label;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

import static eatyourbeets.resources.animator.misc.AnimatorLoadout.BRONZE_REQUIRED_EXPANSION;

public class AnimatorLoadoutRenderer extends GUIElement
{
    private final static EYBCardTooltip ExpansionLockedTooltip = new EYBCardTooltip(GR.Animator.Strings.SeriesSelection.ExpansionHeader, JUtils.Format(GR.Animator.Strings.SeriesSelection.ExpansionSeriesLocked, BRONZE_REQUIRED_EXPANSION));
    private final static EYBCardTooltip ExpansionUnlockedTooltip = new EYBCardTooltip(GR.Animator.Strings.SeriesSelection.ExpansionHeader, GR.Animator.Strings.SeriesSelection.ExpansionSeriesUnlocked);

    protected static final FieldInfo<String> _hp = JUtils.GetField("hp", CharacterOption.class);
    protected static final FieldInfo<Integer> _gold = JUtils.GetField("gold", CharacterOption.class);
    protected static final float POS_X = 170f * Settings.scale;
    protected static final float POS_Y = ((float) Settings.HEIGHT / 2f) - (20 * Settings.scale);
    protected static final float ROW_OFFSET = 60 * Settings.scale;

    protected static final Random RNG = new Random();

    protected final ArrayList<AnimatorLoadout> availableLoadouts;
    protected final ArrayList<AnimatorLoadout> loadouts;

    protected GUI_Button SeriesButton;
    protected GUI_Button LoadoutEditorButton;
    protected GUI_Label StartingCardsLabel;
    protected GUI_Label StartingCardsListLabel;
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

        loadouts = new ArrayList<>();
        availableLoadouts = new ArrayList<>();

        StartingCardsLabel = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
                new AdvancedHitbox(POS_X, POS_Y, leftTextWidth, 50f * Settings.scale))
                .SetColor(Settings.GOLD_COLOR)
                .SetAlignment(0.5f, 0.5f, false)
                .SetText(GR.Animator.Strings.CharSelect.LeftText);

        StartingCardsListLabel = new GUI_Label(EYBFontHelper.CardTitleFont_Small,
                new AdvancedHitbox(POS_X + ROW_OFFSET * 3.5f, POS_Y, leftTextWidth, 50f * Settings.scale))
                .SetFont(EYBFontHelper.CardTitleFont_Small, 0.8f)
                .SetAlignment(0.5f, 0.5f, false);

        SeriesButton = new GUI_Button(GR.Common.Images.Edit.Texture(), new AdvancedHitbox(0, 0, Scale(64), Scale(64)))
                .SetPosition(StartingCardsListLabel.hb.x + StartingCardsListLabel.hb.width + Scale(40), StartingCardsListLabel.hb.y + Scale(25)).SetText("")
                .SetTooltip(GR.Animator.Strings.CharSelect.SeriesEditor, GR.Animator.Strings.CharSelect.SeriesEditorInfo)
                .SetOnClick(this::OpenSeriesSelect);

        LoadoutEditorButton = new GUI_Button(GR.Common.Images.SwapCards.Texture(), new AdvancedHitbox(0, 0, Scale(64), Scale(64)))
                .SetPosition(SeriesButton.hb.x + SeriesButton.hb.width + Scale(40), StartingCardsLabel.hb.y + Scale(25)).SetText("")
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

    private void OpenSeriesSelect()
    {
        if (loadout != null && characterOption != null)
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

        SeriesButton.SetActive(availableLoadouts.size() > 1);
        AnimatorCharacterSelectScreen.TrophiesRenderer.Refresh(loadout);
        AnimatorCharacterSelectScreen.SpecialTrophiesRenderer.Refresh();
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
        SeriesButton.TryUpdate();
        LoadoutEditorButton.TryUpdate();
        StartingCardsLabel.Update();
        StartingCardsListLabel.Update();
    }

    public void Render(SpriteBatch sb)
    {
        SeriesButton.TryRender(sb);
        LoadoutEditorButton.TryRender(sb);
        StartingCardsLabel.Render(sb);
        StartingCardsListLabel.Render(sb);
    }
}