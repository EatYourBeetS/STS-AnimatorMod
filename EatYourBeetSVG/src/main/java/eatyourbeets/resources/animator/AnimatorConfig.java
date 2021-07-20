package eatyourbeets.resources.animator;

import basemod.BaseMod;
import basemod.ModPanel;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.powers.monsters.DarkCubePower;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.ConfigOption_Boolean;
import eatyourbeets.resources.animator.misc.ConfigOption_String;
import eatyourbeets.utilities.JUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;

public class AnimatorConfig
{
    private static final String TROPHY_DATA_KEY = "TDAL";
    private static final String CUSTOM_LOADOUTS_KEY =  "TheAnimator-Loadouts";
    private static final String CROP_CARD_PORTRAIT =  "TheAnimator-UseCroppedPortrait";
    private static final String DISPLAY_BETA_SERIES =  "TheAnimator-DisplayBetaSeries";
    private static final String FADE_CARDS_WITHOUT_SYNERGY =  "TheAnimator-FadeNonSynergicCards";
    private static final String HIDE_TIP_DESCRIPTION =  "TheAnimator-HideTipDescription";
    private static final String HIDE_BLOCK_DAMAGE_BACKGROUND =  "TheAnimator-HideBlockDamageBackground";

    private SpireConfig config;
    private HashSet<String> tips = null;

    public ConfigOption_String CustomLoadouts;
    public ConfigOption_Boolean SimplifyCardUI;
    public ConfigOption_Boolean CropCardImages;
    public ConfigOption_Boolean DisplayBetaSeries;
    public ConfigOption_Boolean FadeCardsWithoutSynergy;

    protected void Initialize()
    {
        try
        {
            config = new SpireConfig("TheAnimator", "TheAnimatorConfig");

            CustomLoadouts = new ConfigOption_String(config, CUSTOM_LOADOUTS_KEY, "");
            SimplifyCardUI = new ConfigOption_Boolean(config, HIDE_BLOCK_DAMAGE_BACKGROUND, false);
            FadeCardsWithoutSynergy = new ConfigOption_Boolean(config, FADE_CARDS_WITHOUT_SYNERGY, true);
            DisplayBetaSeries = new ConfigOption_Boolean(config, DISPLAY_BETA_SERIES, false);
            CropCardImages = new ConfigOption_Boolean(config, CROP_CARD_PORTRAIT, true);
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

        FadeCardsWithoutSynergy.AddToPanel(panel, misc.FadeCardsWithoutSynergy, 400, 700);
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

        BaseMod.registerModBadge(GR.GetTexture(GR.GetPowerImage(DarkCubePower.POWER_ID)), AnimatorCharacter.NAME, "EatYourBeetS", "", panel);
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

    public String TrophyString()
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

    public void TrophyString(String value, boolean flush)
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