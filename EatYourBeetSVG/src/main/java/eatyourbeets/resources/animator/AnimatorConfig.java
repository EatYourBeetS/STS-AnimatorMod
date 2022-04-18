package eatyourbeets.resources.animator;

import basemod.BaseMod;
import basemod.ModPanel;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.powers.monsters.DarkCubePower;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.config.ConfigOption_Boolean;
import eatyourbeets.ui.config.ConfigOption_Integer;
import eatyourbeets.ui.config.ConfigOption_String;
import eatyourbeets.ui.config.ConfigOption_Vector2;
import eatyourbeets.utilities.JUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashSet;

public class AnimatorConfig
{
    private static final String TROPHY_DATA_KEY = "TDAL";
    private static final String STATS_KEY = "STAL";
    private static final String LAST_SEED_KEY = "TSDL";
    private static final String UNNAMED_DATA = "UNDL";
    private static final String ASCENSION_LEVELS_KEY =  "TheAnimator-AscensionModifiers";
    private static final String CUSTOM_LOADOUTS_KEY =  "TheAnimator-Loadouts";
    private static final String CROP_CARD_PORTRAIT =  "TheAnimator-UseCroppedPortrait";
    private static final String DISPLAY_BETA_SERIES =  "TheAnimator-DisplayBetaSeries";
    private static final String FADE_CARDS_WITHOUT_SYNERGY =  "TheAnimator-FadeNonSynergicCards";
    private static final String HIDE_TIP_DESCRIPTION =  "TheAnimator-HideTipDescription";
    private static final String HIDE_BLOCK_DAMAGE_BACKGROUND =  "TheAnimator-HideBlockDamageBackground";
    private static final String AFFINITY_SYSTEM_POSITION =  "TheAnimator-AffinitySystemPosition";
    private static final String VERSION =  "TheAnimator-Version";

    private SpireConfig config;
    private HashSet<String> tips = null;

    public ConfigOption_Integer MajorVersion = new ConfigOption_Integer(VERSION, null);
    public ConfigOption_String Trophies = new ConfigOption_String(TROPHY_DATA_KEY, "");
    public ConfigOption_String Stats = new ConfigOption_String(STATS_KEY, "");
    public ConfigOption_String LastSeed = new ConfigOption_String(LAST_SEED_KEY, "");
    public ConfigOption_String UnnamedData = new ConfigOption_String(UNNAMED_DATA, "");
    public ConfigOption_String CustomLoadouts = new ConfigOption_String(CUSTOM_LOADOUTS_KEY, "");
    public ConfigOption_String AscensionLevels = new ConfigOption_String(ASCENSION_LEVELS_KEY, "");
    public ConfigOption_Boolean SimplifyCardUI = new ConfigOption_Boolean(HIDE_BLOCK_DAMAGE_BACKGROUND, false);
    public ConfigOption_Boolean CropCardImages = new ConfigOption_Boolean(CROP_CARD_PORTRAIT, false);
    public ConfigOption_Boolean DisplayBetaSeries = new ConfigOption_Boolean(DISPLAY_BETA_SERIES, true);
    public ConfigOption_Vector2 AffinitySystemPosition = new ConfigOption_Vector2(AFFINITY_SYSTEM_POSITION, null);

    public void Load(int slot)
    {
        try
        {
            final String fileName = "TheAnimatorConfig_" + slot;
            if (slot == 0)
            {
                final File file = new File(SpireConfig.makeFilePath("TheAnimator", fileName));
                if (!file.exists())
                {
                    final File previousFile = new File(SpireConfig.makeFilePath("TheAnimator", "TheAnimatorConfig"));
                    if (previousFile.exists())
                    {
                        Files.copy(previousFile.toPath(), file.toPath());
                    }
                }
            }

            config = new SpireConfig("TheAnimator", fileName);
            JUtils.LogInfo(this, "Loaded: " + fileName);

            MajorVersion.SetConfig(config);
            Trophies.SetConfig(config);
            Stats.SetConfig(config);
            LastSeed.SetConfig(config);
            UnnamedData.SetConfig(config);
            AscensionLevels.SetConfig(config);
            CustomLoadouts.SetConfig(config);
            SimplifyCardUI.SetConfig(config);
            CropCardImages.SetConfig(config);
            DisplayBetaSeries.SetConfig(config);
            AffinitySystemPosition.SetConfig(config);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected void InitializeOptions()
    {
        final ModPanel panel = new ModPanel();
        final AnimatorStrings.Misc misc = GR.Animator.Strings.Misc;

        //FadeCardsWithoutSynergy.AddToPanel(panel, misc.FadeCardsWithoutSynergy, 400, 700);
        CropCardImages.AddToPanel(panel, misc.UseCardHoveringAnimation, 400, 650);
        SimplifyCardUI.AddToPanel(panel, misc.SimplifyCardUI, 400, 600);

        if (GR.Animator.Data.BetaLoadouts.size() > 0)
        {
            DisplayBetaSeries.AddToPanel(panel, misc.DisplayBetaSeries, 400, 550);
        }
        else
        {
            DisplayBetaSeries.Set(false, false);
        }

        BaseMod.registerModBadge(GR.GetTexture(GR.GetPowerImage(DarkCubePower.POWER_ID)), AnimatorCharacter.ORIGINAL_NAME, "EatYourBeetS", "", panel);
    }

    public boolean HideTipDescription(String id)
    {
        if (tips == null)
        {
            tips = new HashSet<>();

            if (config.has(HIDE_TIP_DESCRIPTION))
            {
                Collections.addAll(tips, config.getString(HIDE_TIP_DESCRIPTION).split("\\|"));
            }
        }

        return tips.contains(id);
    }

    public void HideTipDescription(String id, boolean value, boolean flush)
    {
        if (tips == null)
        {
            tips = new HashSet<>();
        }

        if (value)
        {
            if (id != null)
            {
                tips.add(id);
            }
        }
        else
        {
            tips.remove(id);
        }

        config.setString(HIDE_TIP_DESCRIPTION, JUtils.JoinStrings("|", tips));

        if (flush)
        {
            Save();
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