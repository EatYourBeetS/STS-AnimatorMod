package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.utilities.*;

import java.util.ArrayList;

public class AnimatorLoadoutRenderer extends GUIElement
{
    protected static final FieldInfo<String> _hp = JUtils.GetField("hp", CharacterOption.class);
    protected static final FieldInfo<Integer> _gold = JUtils.GetField("gold", CharacterOption.class);

    protected static final AnimatorStrings.CharacterSelect charSelectStrings = GR.Animator.Strings.CharSelect;
    protected static final Random RNG = new Random();

    protected static final int ResetLoadoutMax = 2;

    protected final ArrayList<AnimatorLoadout> availableLoadouts;
    protected final ArrayList<AnimatorLoadout> loadouts;

    //TODO: Use GUI_Button and GUI_Label
    protected final Hitbox startingCardsLabelHb;
    protected final Hitbox startingCardsSelectedHb;
    protected final Hitbox startingCardsLeftHb;
    protected final Hitbox startingCardsRightHb;

    protected GUI_Button RandomizeButton;
    protected GUI_Button LoadoutEditorButton;
    protected GUI_Button ResetButton;
    protected CharacterSelectScreen selectScreen;
    protected CharacterOption characterOption;
    protected AnimatorLoadout loadout;

    //2 - default
    //1 - first confirmation
    //0 - second confirmation
    private int ResetLoadoutCount = ResetLoadoutMax;
    private int ResetAllLoadoutCount = ResetLoadoutMax;

    protected float textScale;
    protected ColoredString subtitle;

    public AnimatorLoadoutRenderer()
    {
        final float leftTextWidth = FontHelper.getSmartWidth(FontHelper.cardTitleFont, charSelectStrings.LeftText, 9999f, 0f); // Ascension
        final float rightTextWidth = FontHelper.getSmartWidth(FontHelper.cardTitleFont, charSelectStrings.RightText, 9999f, 0f); // Level 22

        textScale = Settings.scale;

        //Need to prevent text from disappearing from scaling too big on 4K resolutions
        if (textScale > 1)
        {
            textScale = 1;
        }

        float POS_X = 180f * Settings.scale;
        float POS_Y = ((float) Settings.HEIGHT / 2f) + (20 * Settings.scale);

        subtitle = new ColoredString();
        loadouts = new ArrayList<>();
        availableLoadouts = new ArrayList<>();
        startingCardsLabelHb = new Hitbox(leftTextWidth, 50f * Settings.scale);
        startingCardsSelectedHb = new Hitbox(rightTextWidth, 50f * Settings.scale);
        startingCardsLeftHb = new Hitbox(70f * Settings.scale, 50f * Settings.scale);
        startingCardsRightHb = new Hitbox(70f * Settings.scale, 50f * Settings.scale);

        startingCardsLabelHb.move(POS_X + (leftTextWidth / 2f), POS_Y);
        startingCardsLeftHb.move(startingCardsLabelHb.x + startingCardsLabelHb.width + (20 * Settings.scale), POS_Y - (10 * Settings.scale));
        startingCardsSelectedHb.move(startingCardsLeftHb.x + startingCardsLeftHb.width + (rightTextWidth / 2f), POS_Y);
        startingCardsRightHb.move(startingCardsSelectedHb.x + startingCardsSelectedHb.width + (10 * Settings.scale), POS_Y - (10 * Settings.scale));

        RandomizeButton = new GUI_Button(GR.Common.Images.Randomize.Texture(), new AdvancedHitbox(0, 0, Scale(64), Scale(64)))
        .SetPosition(startingCardsRightHb.x + startingCardsRightHb.width - Scale(160), POS_Y - Scale(70)).SetText("")
        .SetOnClick(this::RandomizeLoadout);

        LoadoutEditorButton = new GUI_Button(GR.Common.Images.SwapCards.Texture(), new AdvancedHitbox(0, 0, Scale(64), Scale(64)))
        .SetPosition(startingCardsRightHb.x + startingCardsRightHb.width - Scale(110), POS_Y - Scale(70)).SetText("")
        .SetTooltip(charSelectStrings.DeckEditor, charSelectStrings.DeckEditorInfo)
        .SetOnRightClick(this::ChangePreset)
        .SetOnClick(this::OpenLoadoutEditor);

        ResetButton = new GUI_Button(GR.Common.Images.Reset.Texture(), new AdvancedHitbox(0, 0, Scale(64), Scale(64)))
            .SetPosition(startingCardsRightHb.x + startingCardsRightHb.width - Scale(50), POS_Y - Scale(70)).SetText("")
            .SetTooltip(charSelectStrings.Reset, charSelectStrings.ResetInfo)
            .SetOnRightClick(this::ResetAllLoadoutCountdown)
            .SetOnClick(this::ResetLoadoutCountdown);
    }

    private void OpenLoadoutEditor()
    {
        if (loadout != null && characterOption != null)
        {
            GR.UI.LoadoutEditor.Open(loadout, characterOption, () -> RefreshInternal(false));
        }
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

    private void ResetLoadoutCountdown()
    {
        if (RefreshResetAllLoadoutCount())
        {
            return;
        }

        switch (ResetLoadoutCount)
        {
            case 2:
                ResetLoadoutCount--;
                subtitle.SetText(GR.Animator.Strings.CharSelect.ResetCurrentLoadoutConfirm).SetColor(Settings.RED_TEXT_COLOR);
                LoadoutEditorButton.SetInteractable(false);
                selectScreen.confirmButton.isDisabled = true;
                break;
            case 1:
                ResetLoadoutCount--;
                subtitle.SetText(GR.Animator.Strings.CharSelect.FinalConfirm).SetColor(Settings.RED_TEXT_COLOR);
                LoadoutEditorButton.SetInteractable(false);
                selectScreen.confirmButton.isDisabled = true;
                break;
            default:
                ResetLoadoutCount = ResetLoadoutMax;
                //Case 0 - reset!
                ResetLoadout();
                break;
        }
    }

    private void ResetAllLoadoutCountdown()
    {
        if (RefreshResetSingleLoadoutCount())
        {
            return;
        }

        switch (ResetAllLoadoutCount)
        {
            case 2:
                ResetAllLoadoutCount--;
                subtitle.SetText(GR.Animator.Strings.CharSelect.ResetAllLoadoutsConfirm).SetColor(Settings.RED_TEXT_COLOR);
                LoadoutEditorButton.SetInteractable(false);
                selectScreen.confirmButton.isDisabled = true;
                break;
            case 1:
                ResetAllLoadoutCount--;
                subtitle.SetText(GR.Animator.Strings.CharSelect.FinalConfirm).SetColor(Settings.RED_TEXT_COLOR);
                LoadoutEditorButton.SetInteractable(false);
                selectScreen.confirmButton.isDisabled = true;
                break;
            default:
                //Case 0 - reset!

                ResetAllLoadoutCount = ResetLoadoutMax;
                ResetAllLoadout();
                break;
        }
    }

    private boolean RefreshResetLoadoutCountIfResetNotClicked()
    {
        if (ResetButton.hb.justHovered || ResetButton.hb.hovered)
        {
            return false;
        }

        if (RefreshResetSingleLoadoutCount())
        {
            return true;
        }

        if (RefreshResetAllLoadoutCount())
        {
            return true;
        }

        return false;
    }

    private boolean RefreshResetSingleLoadoutCount()
    {
        if (ResetLoadoutCount < ResetLoadoutMax)
        {
            ResetLoadoutCount = ResetLoadoutMax;
            Refresh(selectScreen, characterOption);
            return true;
        }

        return false;
    }

    private boolean RefreshResetAllLoadoutCount()
    {
        if (ResetAllLoadoutCount < ResetLoadoutMax)
        {
            ResetAllLoadoutCount = ResetLoadoutMax;
            Refresh(selectScreen, characterOption);
            return true;
        }

        return false;
    }

    private void ResetLoadout()
    {
        this.loadout.SetPresetToDefault();
        GR.Animator.Data.SaveLoadouts(true);
        Refresh(selectScreen, characterOption);
    }

    private void ResetAllLoadout()
    {
        if (availableLoadouts.size() > 1)
        {
            for (AnimatorLoadout curLoadout : availableLoadouts)
            {
                curLoadout.SetPresetToDefault();
            }
        }
        GR.Animator.Data.SaveLoadouts(true);
        Refresh(selectScreen, characterOption);
    }

    public void RandomizeLoadout()
    {
        if (RefreshResetLoadoutCountIfResetNotClicked())
        {
            return;
        }

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
            subtitle.SetText(GR.Animator.Strings.CharSelect.UnlocksAtLevel(loadout.UnlockLevel, currentLevel)).SetColor(Settings.RED_TEXT_COLOR);
            LoadoutEditorButton.SetInteractable(false);
            selectScreen.confirmButton.isDisabled = true;
        }
        else if (!loadout.Validate().IsValid)
        {
            subtitle.SetText(GR.Animator.Strings.CharSelect.InvalidLoadout).SetColor(Settings.RED_TEXT_COLOR);
            LoadoutEditorButton.SetInteractable(true);
            selectScreen.confirmButton.isDisabled = true;
        }
        else
        {
            subtitle.SetText(loadout.GetDeckPreviewString(true)).SetColor(Settings.GREEN_TEXT_COLOR);
            LoadoutEditorButton.SetInteractable(true);
            selectScreen.confirmButton.isDisabled = false;
        }
    }

    public void Update()
    {
        startingCardsLabelHb.update();
        startingCardsRightHb.update();
        startingCardsLeftHb.update();
        RandomizeButton.TryUpdate();
        LoadoutEditorButton.TryUpdate();
        ResetButton.TryUpdate();

        if (InputHelper.justClickedRight && RefreshResetLoadoutCountIfResetNotClicked())
        {
            return;
        }

        if (InputHelper.justClickedLeft)
        {
            if (RefreshResetLoadoutCountIfResetNotClicked())
            {
                return;
            }

            if (startingCardsLabelHb.hovered)
            {
                startingCardsLabelHb.clickStarted = true;
            }
            else if (startingCardsRightHb.hovered)
            {
                startingCardsRightHb.clickStarted = true;
            }
            else if (startingCardsLeftHb.hovered)
            {
                startingCardsLeftHb.clickStarted = true;
            }
        }

        if (startingCardsLeftHb.clicked)
        {
            startingCardsLeftHb.clicked = false;

            int current = loadouts.indexOf(loadout);
            if (current == 0)
            {
                GR.Animator.Data.SelectedLoadout = loadouts.get(loadouts.size() - 1);
            }
            else
            {
                GR.Animator.Data.SelectedLoadout = loadouts.get(current - 1);
            }

            Refresh(selectScreen, characterOption);
        }

        if (startingCardsRightHb.clicked)
        {
            startingCardsRightHb.clicked = false;

            int current = loadouts.indexOf(loadout);
            if (current >= (loadouts.size() - 1))
            {
                GR.Animator.Data.SelectedLoadout = loadouts.get(0);
            }
            else
            {
                GR.Animator.Data.SelectedLoadout = loadouts.get(current + 1);
            }

            Refresh(selectScreen, characterOption);
        }
    }

    public void Render(SpriteBatch sb)
    {
        // NOTE: this was FontHelper.cardTitleFont_small;
        BitmapFont font = EYBFontHelper.CardTitleFont_Small;
        float originalScale = font.getData().scaleX;
        font.getData().setScale(0.8f);

        FontHelper.renderFont(sb, font, subtitle.text, startingCardsSelectedHb.x, startingCardsSelectedHb.cY + (20 * Settings.scale), subtitle.color);
        font.getData().setScale(originalScale);

        FontHelper.renderFont(sb, FontHelper.cardTitleFont, charSelectStrings.LeftText, startingCardsLabelHb.x, startingCardsLabelHb.cY, Settings.GOLD_COLOR);
        FontHelper.renderFont(sb, FontHelper.cardTitleFont, loadout.Name, startingCardsSelectedHb.x, startingCardsSelectedHb.cY, Settings.CREAM_COLOR);//.BLUE_TEXT_COLOR);

        final float scale = Settings.scale;

        sb.setColor(startingCardsLeftHb.hovered ? Color.WHITE : Color.LIGHT_GRAY);
        sb.draw(ImageMaster.CF_LEFT_ARROW, startingCardsLeftHb.cX - 24f, startingCardsLeftHb.cY - 24f, 24f, 24f,
                48f, 48f, scale, scale, 0f, 0, 0, 48, 48, false, false);

        sb.setColor(startingCardsRightHb.hovered ? Color.WHITE : Color.LIGHT_GRAY);
        sb.draw(ImageMaster.CF_RIGHT_ARROW, startingCardsRightHb.cX - 24f, startingCardsRightHb.cY - 24f, 24f, 24f,
                48f, 48f, scale, scale, 0f, 0, 0, 48, 48, false, false);

        RandomizeButton.TryRender(sb);
        LoadoutEditorButton.TryRender(sb);
        ResetButton.TryRender(sb);
    }
}