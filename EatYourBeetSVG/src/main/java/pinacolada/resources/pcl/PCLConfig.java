package pinacolada.resources.pcl;

import basemod.BaseMod;
import basemod.ModPanel;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import eatyourbeets.powers.monsters.LightningCubePower;
import pinacolada.characters.FoolCharacter;
import pinacolada.resources.GR;
import pinacolada.ui.config.*;
import pinacolada.utilities.PCLJUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;

import static pinacolada.ui.seriesSelection.PCLLoadoutsContainer.MINIMUM_SERIES;

public class PCLConfig
{
    public static String CreateFullID(String name)
    {
        return GR.BASE_PREFIX.toUpperCase(Locale.ROOT) + "-" + name;
    }

    private static final String TROPHY_DATA_KEY = "TDAL";
    private static final String LAST_SEED_KEY = "TSDL";
    private static final String CUSTOM_LOADOUTS_KEY = PCLConfig.CreateFullID("Loadouts");
    private static final String CROP_CARD_PORTRAIT = PCLConfig.CreateFullID("UseCroppedPortrait");
    private static final String DISPLAY_BETA_SERIES = PCLConfig.CreateFullID("DisplayBetaSeries");
    private static final String ENABLE_EVENTS_FOR_OTHER_CHARACTERS = PCLConfig.CreateFullID("EnableEventsForOtherCharacters");
    private static final String ENABLE_RELICS_FOR_OTHER_CHARACTERS = PCLConfig.CreateFullID("EnableRelicsForOtherCharacters");
    private static final String FADE_CARDS_WITHOUT_SYNERGY = PCLConfig.CreateFullID("FadeNonSynergicCards");
    private static final String HIDE_TIP_DESCRIPTION = PCLConfig.CreateFullID("HideTipDescription");
    private static final String HIDE_BLOCK_DAMAGE_BACKGROUND = PCLConfig.CreateFullID("HideBlockDamageBackground");
    private static final String AFFINITY_METER_POSITION = PCLConfig.CreateFullID("AffinityMeterPosition");
    private static final String AFFINITY_SYSTEM_POSITION = PCLConfig.CreateFullID("AffinitySystemPosition");
    private static final String VERSION = PCLConfig.CreateFullID("Version");
    private static final String SELECTED_SERIES = PCLConfig.CreateFullID("SelectedSeries");
    private static final String EXPANDED_SERIES = PCLConfig.CreateFullID("ExpandedSeries");
    private static final String SERIES_SIZE = PCLConfig.CreateFullID("SeriesSize");
    private static final String REPLACE_CARDS_FOOL = PCLConfig.CreateFullID("ReplaceCards");
    private static final String REPLACE_CARDS_ANIMATOR = PCLConfig.CreateFullID("ReplaceCardsAnimator");
    private static final String FLASH_FOR_REROLL = PCLConfig.CreateFullID("FlashForReroll");

    private SpireConfig config;
    private HashSet<String> tips = null;

    public ConfigOption_String Trophies = new ConfigOption_String(TROPHY_DATA_KEY, "");
    public ConfigOption_String LastSeed = new ConfigOption_String(LAST_SEED_KEY, "");
    public ConfigOption_String CustomLoadouts = new ConfigOption_String(CUSTOM_LOADOUTS_KEY, "");
    public ConfigOption_Boolean SimplifyCardUI = new ConfigOption_Boolean(HIDE_BLOCK_DAMAGE_BACKGROUND, false);
    public ConfigOption_Boolean CropCardImages = new ConfigOption_Boolean(CROP_CARD_PORTRAIT, false);
    public ConfigOption_Boolean DisplayBetaSeries = new ConfigOption_Boolean(DISPLAY_BETA_SERIES, true);
    public ConfigOption_Boolean ReplaceCardsFool = new ConfigOption_Boolean(REPLACE_CARDS_FOOL, true);
    public ConfigOption_Boolean ReplaceCardsAnimator = new ConfigOption_Boolean(REPLACE_CARDS_ANIMATOR, true);
    public ConfigOption_Boolean FlashForReroll = new ConfigOption_Boolean(FLASH_FOR_REROLL, true);
    public ConfigOption_Boolean EnableEventsForOtherCharacters = new ConfigOption_Boolean(ENABLE_EVENTS_FOR_OTHER_CHARACTERS, false);
    public ConfigOption_Boolean EnableRelicsForOtherCharacters = new ConfigOption_Boolean(ENABLE_RELICS_FOR_OTHER_CHARACTERS, false);
    public ConfigOption_Vector2 AffinitySystemPosition = new ConfigOption_Vector2(AFFINITY_SYSTEM_POSITION, null);
    public ConfigOption_Vector2 AffinityMeterPosition = new ConfigOption_Vector2(AFFINITY_METER_POSITION, null);
    public ConfigOption_Integer MajorVersion = new ConfigOption_Integer(VERSION, null);
    public ConfigOption_SeriesList SelectedSeries = new ConfigOption_SeriesList(SELECTED_SERIES, null);
    public ConfigOption_SeriesList ExpandedSeries = new ConfigOption_SeriesList(EXPANDED_SERIES, new ArrayList<>());
    public ConfigOption_Integer SeriesSize = new ConfigOption_Integer(SERIES_SIZE, MINIMUM_SERIES);

    public void Load(int slot)
    {
        try
        {
            final String fileName = "PCLConfig_" + slot;
            if (slot == 0)
            {
                final File file = new File(SpireConfig.makeFilePath("PCL", fileName));
                if (!file.exists())
                {
                    final File previousFile = new File(SpireConfig.makeFilePath("PCL", "PCLConfig"));
                    if (previousFile.exists())
                    {
                        Files.copy(previousFile.toPath(), file.toPath());
                    }
                }
            }

            config = new SpireConfig("PCL", fileName);
            PCLJUtils.LogInfo(this, "Loaded: " + fileName);

            MajorVersion.SetConfig(config);
            Trophies.SetConfig(config);
            LastSeed.SetConfig(config);
            CustomLoadouts.SetConfig(config);
            SimplifyCardUI.SetConfig(config);
            CropCardImages.SetConfig(config);
            DisplayBetaSeries.SetConfig(config);
            ReplaceCardsFool.SetConfig(config);
            ReplaceCardsAnimator.SetConfig(config);
            EnableEventsForOtherCharacters.SetConfig(config);
            EnableRelicsForOtherCharacters.SetConfig(config);
            FlashForReroll.SetConfig(config);
            AffinityMeterPosition.SetConfig(config);
            AffinitySystemPosition.SetConfig(config);
            SelectedSeries.SetConfig(config);
            ExpandedSeries.SetConfig(config);
            SeriesSize.SetConfig(config);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected void InitializeOptions()
    {
        final ModPanel panel = new ModPanel();
        final PCLStrings.Misc misc = GR.PCL.Strings.Misc;

        //FadeCardsWithoutSynergy.AddToPanel(panel, misc.FadeCardsWithoutSynergy, 400, 700);
        CropCardImages.AddToPanel(panel, misc.UseCardHoveringAnimation, 400, 650);
        SimplifyCardUI.AddToPanel(panel, misc.SimplifyCardUI, 400, 600);
        EnableEventsForOtherCharacters.AddToPanel(panel, misc.EnableEventsForOtherCharacters, 400, 550);
        EnableRelicsForOtherCharacters.AddToPanel(panel, misc.EnableRelicsForOtherCharacters, 400, 500);
        FlashForReroll.AddToPanel(panel, misc.EnableFlashForReroll, 400, 450);
        ReplaceCardsFool.AddToPanel(panel, misc.ReplaceCardsFool, 400, 400);
        ReplaceCardsAnimator.AddToPanel(panel, misc.ReplaceCardsAnimator, 400, 350);

        if (GR.PCL.Data.BetaLoadouts.size() > 0)
        {
            DisplayBetaSeries.AddToPanel(panel, misc.DisplayBetaSeries, 400, 300);
        }
        else
        {
            DisplayBetaSeries.Set(false, false);
        }

        BaseMod.registerModBadge(GR.GetTexture(GR.GetPowerImage(LightningCubePower.POWER_ID)), FoolCharacter.ORIGINAL_NAME, "PinaColada", "", panel);
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

        config.setString(HIDE_TIP_DESCRIPTION, PCLJUtils.JoinStrings("|", tips));

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