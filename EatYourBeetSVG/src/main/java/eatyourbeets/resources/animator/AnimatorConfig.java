package eatyourbeets.resources.animator;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.ModToggleButton;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.powers.monsters.DarkCubePower;
import eatyourbeets.resources.GR;

import java.io.IOException;
import java.util.function.Consumer;

public class AnimatorConfig
{
    private static final String TROPHY_DATA_KEY = "TDAL";
    private static final String CROP_CARD_PORTRAIT_KEY =  "TheAnimator-UseCroppedPortrait";
    private static final String DISPLAY_BETA_SERIES =  "TheAnimator-DisplayBetaSeries";

    private SpireConfig config;
    private Boolean CropCardImages = null;
    private Boolean DisplayBetaSeries = null;

    public boolean GetDisplayBetaSeries()
    {
        if (DisplayBetaSeries == null)
        {
            if (config.has(DISPLAY_BETA_SERIES))
            {
                DisplayBetaSeries = config.getBool(DISPLAY_BETA_SERIES);
            }
            else
            {
                DisplayBetaSeries = false; // Default value
            }
        }

        return DisplayBetaSeries;
    }

    public boolean GetCropCardImages()
    {
        if (CropCardImages == null)
        {
            if (config.has(CROP_CARD_PORTRAIT_KEY))
            {
                CropCardImages = config.getBool(CROP_CARD_PORTRAIT_KEY);
            }
            else
            {
                CropCardImages = true; // Default value
            }
        }

        return CropCardImages;
    }

    public void SetDisplayBetaSeries(boolean value, boolean flush)
    {
        config.setBool(DISPLAY_BETA_SERIES, DisplayBetaSeries = value);

        if (flush)
        {
            Save();
        }
    }

    public void SetCropCardImages(boolean value, boolean flush)
    {
        config.setBool(CROP_CARD_PORTRAIT_KEY, CropCardImages = value);

        if (flush)
        {
            Save();
        }
    }

    public void SetTrophyString(String value, boolean flush)
    {
        config.setString(TROPHY_DATA_KEY, value);

        if (flush)
        {
            Save();
        }

        Prefs prefs = null;
        if (AbstractDungeon.player != null)
        {
            prefs = AbstractDungeon.player.getPrefs();
        }
        if (prefs == null)
        {
            prefs = SaveHelper.getPrefs(GR.Animator.PlayerClass.name());
        }
        if (prefs != null)
        {
            prefs.putString(TROPHY_DATA_KEY, value);

            if (flush)
            {
                prefs.flush();
            }
        }
    }

    public String GetTrophyString()
    {
        String data = config.getString(TROPHY_DATA_KEY);
        if (data == null)
        {
            Prefs prefs = SaveHelper.getPrefs(GR.Animator.PlayerClass.name());
            data = prefs.getString(TROPHY_DATA_KEY);
            config.setString(TROPHY_DATA_KEY, data);
            Save();
        }

        return data;
    }

    public void Initialize()
    {
        try
        {
            config = new SpireConfig("TheAnimator", "TheAnimatorConfig");

            if (config.has(CROP_CARD_PORTRAIT_KEY))
            {
                CropCardImages = config.getBool(CROP_CARD_PORTRAIT_KEY);
            }

            ModPanel settingsPanel = new ModPanel();
            AnimatorStrings.Misc misc = GR.Animator.Strings.Misc;
            AddToggle(settingsPanel, misc.UseCardHoveringAnimation, 400, 700, GetCropCardImages(), c -> SetCropCardImages(c.enabled, true));

            if (GR.Animator.Data.BetaLoadouts.size() > 0)
            {
                AddToggle(settingsPanel, misc.DisplayBetaSeries, 400, 650, GetDisplayBetaSeries(), c -> SetDisplayBetaSeries(c.enabled, true));
            }

            BaseMod.registerModBadge(GR.GetTexture(GR.GetPowerImage(DarkCubePower.POWER_ID)), AnimatorCharacter.NAME, "EatYourBeetS", "", settingsPanel);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public boolean Save()
    {
        try
        {
            config.save();
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    protected void AddToggle(ModPanel settingsPanel, String label, float x, float y, boolean initialValue, Consumer<ModToggleButton> onToggle)
    {
        settingsPanel.addUIElement(new ModLabeledToggleButton(label, x, y, Settings.CREAM_COLOR, FontHelper.charDescFont, initialValue, settingsPanel, __ -> { }, onToggle));
    }
}
