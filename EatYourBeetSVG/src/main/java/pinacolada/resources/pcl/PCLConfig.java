package pinacolada.resources.pcl;

import basemod.BaseMod;
import basemod.ModPanel;
import com.badlogic.gdx.Input;
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
import java.util.HashMap;
import java.util.HashSet;

import static pinacolada.ui.seriesSelection.PCLLoadoutsContainer.MINIMUM_SERIES;

public class PCLConfig
{
    public static final HashMap<Integer, Integer> EQUIVALENT_KEYS = new HashMap<>();

    private static final String TROPHY_DATA_KEY = "TDAL";
    private static final String LAST_SEED_KEY = "TSDL";
    private static final String CUSTOM_LOADOUTS_KEY =  "PCL-Loadouts";
    private static final String CROP_CARD_PORTRAIT =  "PCL-UseCroppedPortrait";
    private static final String DISPLAY_BETA_SERIES =  "PCL-DisplayBetaSeries";
    private static final String ENABLE_EVENTS_FOR_OTHER_CHARACTERS =  "PCL-EnableEventsForOtherCharacters";
    private static final String ENABLE_RELICS_FOR_OTHER_CHARACTERS =  "PCL-EnableRelicsForOtherCharacters";
    private static final String FADE_CARDS_WITHOUT_SYNERGY =  "PCL-FadeNonSynergicCards";
    private static final String HIDE_TIP_DESCRIPTION =  "PCL-HideTipDescription";
    private static final String HIDE_BLOCK_DAMAGE_BACKGROUND =  "PCL-HideBlockDamageBackground";
    private static final String AFFINITY_METER_POSITION =  "PCL-AffinityMeterPosition";
    private static final String AFFINITY_SYSTEM_POSITION =  "PCL-AffinitySystemPosition";
    private static final String VERSION =  "PCL-Version";
    private static final String SELECTEDSERIES =  "PCL-SelectedSeries";
    private static final String EXPANDEDSERIES =  "PCL-ExpandedSeries";
    private static final String SERIESSIZE =  "PCL-SeriesSize";
    private static final String REPLACECARDS = "PCL-ReplaceCards";
    private static final String KEYMAP_CONTROLPILE = "PCL-KeyMapControlPile";
    private static final String KEYMAP_CYCLE1 = "PCL-KeyMapCycle1";
    private static final String KEYMAP_CYCLE2 = "PCL-KeyMapCycle2";
    private static final String KEYMAP_REROLL = "PCL-KeyMapReroll";

    static {
        EQUIVALENT_KEYS.put(Input.Keys.ALT_LEFT, Input.Keys.ALT_RIGHT);
        EQUIVALENT_KEYS.put(Input.Keys.ALT_RIGHT, Input.Keys.ALT_LEFT);
        EQUIVALENT_KEYS.put(Input.Keys.CONTROL_LEFT, Input.Keys.CONTROL_RIGHT);
        EQUIVALENT_KEYS.put(Input.Keys.CONTROL_RIGHT, Input.Keys.CONTROL_LEFT);
        EQUIVALENT_KEYS.put(Input.Keys.SHIFT_LEFT, Input.Keys.SHIFT_RIGHT);
        EQUIVALENT_KEYS.put(Input.Keys.SHIFT_RIGHT, Input.Keys.SHIFT_LEFT);
    }

    private SpireConfig config;
    private HashSet<String> tips = null;

    public ConfigOption_String Trophies = new ConfigOption_String(TROPHY_DATA_KEY, "");
    public ConfigOption_String LastSeed = new ConfigOption_String(LAST_SEED_KEY, "");
    public ConfigOption_String CustomLoadouts = new ConfigOption_String(CUSTOM_LOADOUTS_KEY, "");
    public ConfigOption_Boolean SimplifyCardUI = new ConfigOption_Boolean(HIDE_BLOCK_DAMAGE_BACKGROUND, false);
    public ConfigOption_Boolean CropCardImages = new ConfigOption_Boolean(CROP_CARD_PORTRAIT, false);
    public ConfigOption_Boolean DisplayBetaSeries = new ConfigOption_Boolean(DISPLAY_BETA_SERIES, true);
    public ConfigOption_Boolean ReplaceCards = new ConfigOption_Boolean(REPLACECARDS, true);
    public ConfigOption_Boolean EnableEventsForOtherCharacters = new ConfigOption_Boolean(ENABLE_EVENTS_FOR_OTHER_CHARACTERS, false);
    public ConfigOption_Boolean EnableRelicsForOtherCharacters = new ConfigOption_Boolean(ENABLE_RELICS_FOR_OTHER_CHARACTERS, false);
    public ConfigOption_Vector2 AffinitySystemPosition = new ConfigOption_Vector2(AFFINITY_SYSTEM_POSITION, null);
    public ConfigOption_Vector2 AffinityMeterPosition = new ConfigOption_Vector2(AFFINITY_METER_POSITION, null);
    public ConfigOption_Integer MajorVersion = new ConfigOption_Integer(VERSION, null);
    public ConfigOption_SeriesList SelectedSeries = new ConfigOption_SeriesList(SELECTEDSERIES, null);
    public ConfigOption_SeriesList ExpandedSeries = new ConfigOption_SeriesList(EXPANDEDSERIES, new ArrayList<>());
    public ConfigOption_Integer SeriesSize = new ConfigOption_Integer(SERIESSIZE, MINIMUM_SERIES);

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
            ReplaceCards.SetConfig(config);
            EnableEventsForOtherCharacters.SetConfig(config);
            EnableRelicsForOtherCharacters.SetConfig(config);
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
        ReplaceCards.AddToPanel(panel, misc.EnableEventsForOtherCharacters, 400, 450);

        if (GR.PCL.Data.BetaLoadouts.size() > 0)
        {
            DisplayBetaSeries.AddToPanel(panel, misc.DisplayBetaSeries, 400, 400);
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