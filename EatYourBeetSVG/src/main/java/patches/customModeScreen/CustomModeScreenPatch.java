package patches.customModeScreen;

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
import eatyourbeets.dailymods.AnimatorDailyMod;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.loadouts._FakeLoadout;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.controls.GUI_Dropdown;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.MethodInfo;
import javassist.CtBehavior;
import org.apache.logging.log4j.util.Strings;

import javax.xml.transform.Result;
import java.util.ArrayList;


public class CustomModeScreenPatch {

    private static final FieldInfo<Float> _scrollY = JUtils.GetField("scrollY", CustomModeScreen.class);
    private static final FieldInfo<Hitbox> _seedHb = JUtils.GetField("seedHb", CustomModeScreen.class);
    private static final FieldInfo<ArrayList<CustomMod>> _modList = JUtils.GetField("modList", CustomModeScreen.class);
    private static final MethodInfo.T0<Result> _playHoverSound = JUtils.GetMethod("playHoverSound", CustomModeScreen.class);
    private static final MethodInfo.T0<Result> _playClickStartSound = JUtils.GetMethod("playClickStartSound", CustomModeScreen.class);

    private static final BitmapFont seriesFont = FontHelper.charDescFont;
    private static final Random RNG = new Random();

    private static boolean displaySeries = false;
    private static boolean forceStopScroll = false;

    private static ArrayList<AnimatorDailyMod> allAnimatorMods = new ArrayList<>();

    private static AnimatorLoadout startingLoadout = new _FakeLoadout();

    private static ArrayList<AnimatorLoadout> availableLoadouts = new ArrayList<>();;
    private static ArrayList<AnimatorLoadout> loadouts = new ArrayList<>();;
    private static GUI_Dropdown<AnimatorLoadout> SeriesDropdown;
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
        private static MethodInfo.T2<String, String, String> addDailyMod = JUtils.GetMethod("addDailyMod", CustomModeScreen.class, String.class, String.class);

        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(CustomModeScreen __instance)
        {
            for (AnimatorDailyMod mod : AnimatorDailyMod.mods)
            {
                addDailyMod.Invoke(__instance,mod.name, mod.getColor());
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
            FontHelper.renderSmartText(sb, FontHelper.panelNameFont, GR.Animator.Strings.SeriesUI.SeriesUI, SeriesLeftButton.hb.cX - 24.0F, _scrollY.Get(__instance) - 460.0F * Settings.scale + 850.0F * Settings.scale, 9999.0F, 32.0F * Settings.scale, Settings.GOLD_COLOR);
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
        SeriesDropdown = new GUI_Dropdown<AnimatorLoadout>(new AdvancedHitbox(-9999, -9999, 240 * Settings.scale, 48 * Settings.scale), cs -> cs.Series.LocalizedName)
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

        final int unlockLevel = GR.Animator.GetUnlockLevel();
        for (AnimatorLoadout loadout : GR.Animator.Data.BaseLoadouts)
        {
            loadouts.add(loadout);
            if (unlockLevel >= loadout.UnlockLevel)
            {
                availableLoadouts.add(loadout);
            }
        }

        if (GR.Animator.Config.DisplayBetaSeries.Get())
        {
            for (AnimatorLoadout loadout : GR.Animator.Data.BetaLoadouts)
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
            final int level = GR.Animator.GetUnlockLevel();
            final int levelA = a.UnlockLevel - level;
            final int levelB = b.UnlockLevel - level;
            if (levelA > 0 || levelB > 0)
            {
                return diff + Integer.compare(levelA, levelB) * 1313;
            }

            return diff;
        });

        startingLoadout = GR.Animator.Data.SelectedLoadout;
        if (startingLoadout.GetStartingDeck().isEmpty() || !loadouts.contains(startingLoadout))
        {
            startingLoadout = GR.Animator.Data.SelectedLoadout = loadouts.get(0);
        }
        SeriesDropdown.SetItems(loadouts);
        SeriesDropdown.SetSelection(GR.Animator.Data.SelectedLoadout, false);
    }

    private static void InitializeAllAnimatorMods()
    {
        for (AnimatorDailyMod mod : AnimatorDailyMod.mods)
        {
            allAnimatorMods.add(mod.clone());
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

        startingLoadout = GR.Animator.Data.SelectedLoadout;
    }

    private static void UpdateDisplaySeries(CustomModeScreen __instance)
    {
        if (CardCrawlGame.chosenCharacter != null && CardCrawlGame.chosenCharacter.equals(GR.Enums.Characters.THE_ANIMATOR))
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
        for (AnimatorDailyMod curMod : allAnimatorMods)
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

    private static void ChangeLoadout(AnimatorLoadout loadout)
    {
        GR.Animator.Data.SelectedLoadout = loadout;
        SeriesDropdown.SetSelection(GR.Animator.Data.SelectedLoadout, false);
    }

    private static boolean ShouldHideSeries() {
        return (SeriesLeftButton == null || SeriesRightButton == null || SeriesDropdown == null || !displaySeries);
    }
}
