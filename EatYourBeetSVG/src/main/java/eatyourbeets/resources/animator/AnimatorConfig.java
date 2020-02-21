package eatyourbeets.resources.animator;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.powers.UnnamedReign.DarkCubePower;
import eatyourbeets.resources.GR;

import java.io.IOException;

public class AnimatorConfig
{
    private static final String TROPHY_DATA_KEY = "TDAL";
    private static final String CROP_CARD_PORTRAIT_KEY =  "TheAnimator-UseCroppedPortrait";

    private SpireConfig config;
    private Boolean CropCardImages = null;

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
                CropCardImages = true;
            }
        }

        return CropCardImages;
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

            ModPanel settingsPanel = new ModPanel(); // TODO: Localize
            settingsPanel.addUIElement(new ModLabeledToggleButton("Use card hovering animation.", 400, 700,
            Settings.CREAM_COLOR, FontHelper.charDescFont, GetCropCardImages(), settingsPanel, __ -> { }, c -> SetCropCardImages(c.enabled, true)));
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
}
