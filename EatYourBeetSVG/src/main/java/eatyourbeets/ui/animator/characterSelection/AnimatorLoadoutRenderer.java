package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.Color;
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
import eatyourbeets.ui.AdvancedHitbox;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

public class AnimatorLoadoutRenderer extends GUIElement
{
    protected static FieldInfo<String> _hp = JavaUtilities.GetField("hp", CharacterOption.class);
    protected static FieldInfo<Integer> _gold = JavaUtilities.GetField("gold", CharacterOption.class);

    protected static final AnimatorStrings.CharacterSelect charSelectStrings = GR.Animator.Strings.CharSelect;
    protected static final Random RNG = new Random();

    protected final ArrayList<AnimatorLoadout> availableLoadouts;
    protected final ArrayList<AnimatorLoadout> loadouts;

    //TODO: Use GUI_Button and GUI_Label
    protected final Hitbox startingCardsLabelHb;
    protected final Hitbox startingCardsSelectedHb;
    protected final Hitbox startingCardsLeftHb;
    protected final Hitbox startingCardsRightHb;

    protected GUI_Button RandomizeButton;
    protected CharacterSelectScreen selectScreen;
    protected CharacterOption characterOption;
    protected String lockedDescription;
    protected AnimatorLoadout loadout;

    public AnimatorLoadoutRenderer()
    {
        float leftTextWidth = FontHelper.getSmartWidth(FontHelper.cardTitleFont, charSelectStrings.LeftText, 9999f, 0f); // Ascension
        float rightTextWidth = FontHelper.getSmartWidth(FontHelper.cardTitleFont, charSelectStrings.RightText, 9999f, 0f); // Level 22

        float POS_X = 180f * Settings.scale;
        float POS_Y = ((float) Settings.HEIGHT / 2f) + (20 * Settings.scale);

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

        RandomizeButton = new GUI_Button(GR.Common.Images.Randomize.Texture(), new AdvancedHitbox(0, 0, (50 * Settings.scale), (50 * Settings.scale), false))
        .SetPosition(startingCardsRightHb.x + startingCardsRightHb.width + (15 * Settings.scale), POS_Y - (10 * Settings.scale)).SetText("")
        .SetOnClick(this::RandomizeLoadout);
    }

    private void RandomizeLoadout()
    {
        if (availableLoadouts.size() > 1)
        {
            while (loadout == GR.Animator.Data.SelectedLoadout)
            {
                GR.Animator.Data.SelectedLoadout = JavaUtilities.GetRandomElement(loadouts, RNG);
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
        if (GR.Animator.Config.GetDisplayBetaSeries())
        {
            for (AnimatorLoadout loadout : GR.Animator.Data.BetaLoadouts)
            {
                if (loadout.GetStartingDeck().size() > 0)
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
            int diff = a.Name.compareTo(b.Name);
            int level = GR.Animator.GetUnlockLevel();
            int levelA = a.UnlockLevel - level;
            int levelB = b.UnlockLevel - level;
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

        _gold.Set(characterOption, loadout.StartingGold);
        _hp.Set(characterOption, String.valueOf(loadout.MaxHP));
        selectScreen.bgCharImg = GR.Animator.Images.GetCharacterPortrait(loadout.ID);

        int currentLevel = GR.Animator.GetUnlockLevel();
        if (currentLevel >= loadout.UnlockLevel)
        {
            lockedDescription = null;
        }
        else
        {
            lockedDescription = GR.Animator.Strings.CharSelect.UnlocksAtLevel(loadout.UnlockLevel, currentLevel);
        }

        RandomizeButton.SetActive(availableLoadouts.size() > 1);
        AnimatorCharacterSelectScreen.TrophiesRenderer.Refresh(loadout);
        AnimatorCharacterSelectScreen.SpecialTrophiesRenderer.Refresh();
    }

    public void Update()
    {
        startingCardsLabelHb.update();
        startingCardsRightHb.update();
        startingCardsLeftHb.update();
        RandomizeButton.TryUpdate();

        if (InputHelper.justClickedLeft)
        {
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
        Color textColor;
        String description;
        if (lockedDescription != null)
        {
            description = lockedDescription;
            textColor = Settings.RED_TEXT_COLOR;
            selectScreen.confirmButton.isDisabled = true;
        }
        else
        {
            description = loadout.GetDeckPreviewString();
            textColor = Settings.GREEN_TEXT_COLOR;
            selectScreen.confirmButton.isDisabled = false;
        }

        float originalScale = FontHelper.cardTitleFont_small.getData().scaleX;
        FontHelper.cardTitleFont_small.getData().setScale(Settings.scale * 0.8f);

        FontHelper.renderFont(sb, FontHelper.cardTitleFont_small, description, startingCardsSelectedHb.x, startingCardsSelectedHb.cY + (20 * Settings.scale), textColor);
        FontHelper.cardTitleFont_small.getData().setScale(Settings.scale * originalScale);

        FontHelper.renderFont(sb, FontHelper.cardTitleFont, charSelectStrings.LeftText, startingCardsLabelHb.x, startingCardsLabelHb.cY, Settings.GOLD_COLOR);
        FontHelper.renderFont(sb, FontHelper.cardTitleFont, loadout.Name, startingCardsSelectedHb.x, startingCardsSelectedHb.cY, Settings.CREAM_COLOR);//.BLUE_TEXT_COLOR);

        sb.setColor(startingCardsLeftHb.hovered ? Color.WHITE : Color.LIGHT_GRAY);
        sb.draw(ImageMaster.CF_LEFT_ARROW, startingCardsLeftHb.cX - 24f, startingCardsLeftHb.cY - 24f, 24f, 24f,
                48f, 48f, Settings.scale, Settings.scale, 0f, 0, 0, 48, 48, false, false);

        sb.setColor(startingCardsRightHb.hovered ? Color.WHITE : Color.LIGHT_GRAY);
        sb.draw(ImageMaster.CF_RIGHT_ARROW, startingCardsRightHb.cX - 24f, startingCardsRightHb.cY - 24f, 24f, 24f,
                48f, 48f, Settings.scale, Settings.scale, 0f, 0, 0, 48, 48, false, false);

        RandomizeButton.TryRender(sb);
    }
}