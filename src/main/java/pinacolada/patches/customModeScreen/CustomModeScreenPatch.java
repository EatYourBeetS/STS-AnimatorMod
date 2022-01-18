package pinacolada.patches.customModeScreen;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.custom.CustomMod;
import com.megacrit.cardcrawl.screens.custom.CustomModeScreen;
import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.MethodInfo;
import javassist.CtBehavior;
import org.apache.logging.log4j.util.Strings;
import pinacolada.dailymods.PCLDailyMod;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.loadouts._FakeLoadout;
import pinacolada.resources.pcl.misc.PCLLoadout;
import pinacolada.ui.controls.GUI_Button;
import pinacolada.ui.controls.GUI_Dropdown;
import pinacolada.ui.hitboxes.AdvancedHitbox;
import pinacolada.utilities.PCLJUtils;

import javax.xml.transform.Result;
import java.util.ArrayList;


public class CustomModeScreenPatch {

    private static final FieldInfo<Float> _scrollY = PCLJUtils.GetField("scrollY", CustomModeScreen.class);
    private static final FieldInfo<Hitbox> _seedHb = PCLJUtils.GetField("seedHb", CustomModeScreen.class);
    private static final FieldInfo<ArrayList<CustomMod>> _modList = PCLJUtils.GetField("modList", CustomModeScreen.class);
    private static final MethodInfo.T0<Result> _playHoverSound = PCLJUtils.GetMethod("playHoverSound", CustomModeScreen.class);
    private static final MethodInfo.T0<Result> _playClickStartSound = PCLJUtils.GetMethod("playClickStartSound", CustomModeScreen.class);

    private static final BitmapFont seriesFont = FontHelper.charDescFont;
    private static final Random RNG = new Random();

    private static boolean displaySeries = false;
    private static boolean forceStopScroll = false;

    private static ArrayList<PCLDailyMod> allAnimatorMods = new ArrayList<>();

    private static PCLLoadout startingLoadout = new _FakeLoadout();

    private static ArrayList<PCLLoadout> availableLoadouts = new ArrayList<>();
    private static ArrayList<PCLLoadout> loadouts = new ArrayList<>();
    private static GUI_Dropdown<PCLLoadout> SeriesDropdown;
    private static GUI_Button SeriesLeftButton;
    private static GUI_Button SeriesRightButton;


    private static String seriesLabel = Strings.EMPTY;


    @SpirePatch(clz = CustomModeScreen.class, method = "open")
    public static class CustomModeScreen_Open
    {
        @SpirePostfixPatch
        public static void Postfix(CustomModeScreen __instance)
        {
            InitializeSeries(__instance);
        }
    }

    @SpirePatch(clz = CustomModeScreen.class, method = "initializeMods")
    public static class CustomModeScreen_InitializeMods
    {
        private static MethodInfo.T2<String, String, String> addDailyMod = PCLJUtils.GetMethod("addDailyMod", CustomModeScreen.class, String.class, String.class);

        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(CustomModeScreen __instance)
        {
            for (FuncT0<PCLDailyMod> mod : PCLDailyMod.mods)
            {
                PCLDailyMod m = mod.Invoke();
                addDailyMod.Invoke(__instance,m.name, m.getColor());
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(CustomModeScreen.class, "addMod");
                return new int[]{LineFinder.findInOrder(ctMethodToPatch, finalMatcher)[0] - 1};
            }
        }
    }

    @SpirePatch(clz = CustomModeScreen.class, method = "renderScreen")
    public static class CustomModeScreen_RenderScreen
    {
        @SpirePostfixPatch
        public static void Postfix(CustomModeScreen __instance, SpriteBatch sb)
        {
            FontHelper.renderSmartText(sb, FontHelper.panelNameFont, GR.PCL.Strings.SeriesUI.SeriesUI, SeriesLeftButton.hb.cX - 24.0F, _scrollY.Get(__instance) - 460.0F * Settings.scale + 850.0F * Settings.scale, 9999.0F, 32.0F * Settings.scale, Settings.GOLD_COLOR);
        }
    }

    @SpirePatch(clz = CustomModeScreen.class, method = "updateScrolling")
    public static class CustomModeScreen_UpdateScrolling
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> Prefix(CustomModeScreen __instance)
        {
            if (forceStopScroll) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = CustomModeScreen.class, method = "updateSeed")
    public static class CustomModeScreen_UpdateSeed
    {
        @SpirePostfixPatch
        public static void Postfix(CustomModeScreen __instance)
        {
            UpdateDisplaySeries(__instance);
            UpdateSeriesArrows(__instance);
        }
    }

    @SpirePatch(clz = CustomModeScreen.class, method = "render")
    public static class CustomModeScreen_Render
    {
        @SpirePostfixPatch
        public static void Postfix(CustomModeScreen __instance, SpriteBatch sb)
        {
            RenderSeries(__instance, sb);
        }

        private static void RenderSeries(CustomModeScreen __instance, SpriteBatch sb)
        {

            if (ShouldHideSeries())
            {
                return;
            }
            Hitbox seedHb = _seedHb.Get(__instance);
            float seedRightWidth = seedHb.cX + seedHb.width + 2.5F;
            FontHelper.renderFontLeft(sb, EYBFontHelper.CardTitleFont_Small, startingLoadout.GetDeckPreviewString(true), seedRightWidth, seedHb.cY - SeriesLeftButton.hb.height - 20.0F, Settings.GREEN_TEXT_COLOR);
            SeriesLeftButton.TryRender(sb);
            SeriesDropdown.TryRender(sb);
            SeriesRightButton.TryRender(sb);
        }
    }

    private static void InitializeSeries(CustomModeScreen __instance)
    {
        forceStopScroll = false;
        SeriesLeftButton = new GUI_Button(ImageMaster.CF_LEFT_ARROW, new AdvancedHitbox(-9999, -9999, 48 * Settings.scale, 48 * Settings.scale))
                .SetText("")
                .SetOnClick(() -> {
                    ChangeLoadout(loadouts.indexOf(startingLoadout) - 1);
                });
        SeriesRightButton = new GUI_Button(ImageMaster.CF_RIGHT_ARROW, new AdvancedHitbox(-9999, -9999, 48 * Settings.scale, 48 * Settings.scale))
                .SetText("")
                .SetOnClick(() -> {
                    ChangeLoadout(loadouts.indexOf(startingLoadout) + 1);
                });
        SeriesDropdown = new GUI_Dropdown<PCLLoadout>(new AdvancedHitbox(-9999, -9999, 240 * Settings.scale, 48 * Settings.scale), cs -> cs.Series.LocalizedName)
                .SetOnChange(selectedSeries -> {
                    ChangeLoadout(selectedSeries.get(0));
                })
                .SetOnOpenOrClose(isOpen -> {
                    forceStopScroll = isOpen;
                });


        InitializeAllAnimatorMods();
        UpdateDisplaySeries(__instance);

        InitializeSeriesLoadout();
        UpdateSeriesArrows(__instance);
    }

    private static void InitializeSeriesLoadout()
    {
        loadouts.clear();
        availableLoadouts.clear();

        final int unlockLevel = GR.PCL.GetUnlockLevel();
        for (PCLLoadout loadout : GR.PCL.Data.BaseLoadouts)
        {
            loadouts.add(loadout);
            if (unlockLevel >= loadout.UnlockLevel)
            {
                availableLoadouts.add(loadout);
            }
        }

        if (GR.PCL.Config.DisplayBetaSeries.Get())
        {
            for (PCLLoadout loadout : GR.PCL.Data.BetaLoadouts)
            {
                if (loadout.GetPreset().CardsSize() > 0)
                {
                    loadouts.add(loadout);
                    if (unlockLevel >= loadout.UnlockLevel)
                    {
                        availableLoadouts.add(loadout);
                    }
                }
            }
        }

        loadouts.sort((a, b) ->
        {
            final int diff = a.Name.compareTo(b.Name);
            final int level = GR.PCL.GetUnlockLevel();
            final int levelA = a.UnlockLevel - level;
            final int levelB = b.UnlockLevel - level;
            if (levelA > 0 || levelB > 0)
            {
                return diff + Integer.compare(levelA, levelB) * 1313;
            }

            return diff;
        });

        startingLoadout = GR.PCL.Data.SelectedLoadout;
        if (startingLoadout.GetStartingDeck().isEmpty() || !loadouts.contains(startingLoadout))
        {
            startingLoadout = GR.PCL.Data.SelectedLoadout = loadouts.get(0);
        }
        SeriesDropdown.SetItems(loadouts);
        SeriesDropdown.SetSelection(GR.PCL.Data.SelectedLoadout, false);
    }

    private static void InitializeAllAnimatorMods()
    {
        for (FuncT0<PCLDailyMod> mod : PCLDailyMod.mods)
        {
            allAnimatorMods.add(mod.Invoke());
        }
    }

    private static void UpdateSeriesArrows(CustomModeScreen __instance)
    {
        if (ShouldHideSeries())
        {
            return;
        }

        boolean loadoutHbRightClick = false;

        Hitbox seedHb = _seedHb.Get(__instance);
        float seedRightWidth = seedHb.cX + seedHb.width + 2.5F;

        SeriesLeftButton.SetPosition(seedRightWidth - 70.0F * 0.5F, seedHb.cY);
        SeriesDropdown.SetPosition(SeriesLeftButton.hb.x + SeriesLeftButton.hb.width + (20 * Settings.scale), SeriesLeftButton.hb.y);
        SeriesRightButton.SetPosition(SeriesDropdown.hb.x + SeriesDropdown.hb.width + (40 * Settings.scale), seedHb.cY);

        SeriesLeftButton.TryUpdate();
        SeriesDropdown.TryUpdate();
        SeriesRightButton.TryUpdate();

        startingLoadout = GR.PCL.Data.SelectedLoadout;
    }

    private static void UpdateDisplaySeries(CustomModeScreen __instance)
    {
        if (CardCrawlGame.chosenCharacter != null && CardCrawlGame.chosenCharacter.equals(GR.Enums.Characters.THE_FOOL))
        {
            for (CustomMod mod : _modList.Get(__instance))
            {
                if (mod.selected && IsModNoDisplaySeries(mod))
                {
                    displaySeries = false;
                    return;
                }
            }

            displaySeries = true;
            return;
        }

        displaySeries = false;
        forceStopScroll = false;
    }

    private static boolean IsModNoDisplaySeries(CustomMod mod)
    {
        for (PCLDailyMod curMod : allAnimatorMods)
        {
            if (curMod.modID.equals(mod.ID))
            {
                return curMod.noDisplaySeries;
            }
        }

        return false;
    }

    private static void ChangeLoadout(int index)
    {
        int actualIndex = index % loadouts.size();
        if (actualIndex < 0) {
            actualIndex = loadouts.size() - 1;
        }
        ChangeLoadout(loadouts.get(actualIndex));
    }

    private static void ChangeLoadout(PCLLoadout loadout)
    {
        GR.PCL.Data.SelectedLoadout = loadout;
        SeriesDropdown.SetSelection(GR.PCL.Data.SelectedLoadout, false);
    }

    private static boolean ShouldHideSeries() {
        return (SeriesLeftButton == null || SeriesRightButton == null || SeriesDropdown == null || !displaySeries);
    }
}
