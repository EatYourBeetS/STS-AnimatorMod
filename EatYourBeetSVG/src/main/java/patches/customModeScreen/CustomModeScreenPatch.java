package patches.customModeScreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.custom.CustomMod;
import com.megacrit.cardcrawl.screens.custom.CustomModeScreen;
import com.megacrit.cardcrawl.trials.CustomTrial;
import eatyourbeets.dailymods.AnimatorDailyMod;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.loadouts._FakeLoadout;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.ui.animator.characterSelection.AnimatorCharacterSelectScreen;
import eatyourbeets.utilities.*;
import org.apache.logging.log4j.util.Strings;
import patches.CustomAnimatorCharacterOptions;

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

    private static ArrayList<AnimatorDailyMod> allAnimatorMods = new ArrayList<>();

    private static AnimatorLoadout startingLoadout = new _FakeLoadout();

    private static ArrayList<AnimatorLoadout> availableLoadouts = new ArrayList<>();
    private static ArrayList<AnimatorLoadout> loadouts = new ArrayList<>();

    private static CustomAnimatorCharacterOptions option = new CustomAnimatorCharacterOptions();

    private static Hitbox loadoutHb = null;
    private static Hitbox seriesleftHb = null;
    private static Hitbox seriesRightHb = null;
    private static Hitbox seriesHb = null;

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

        @SpirePostfixPatch
        public static void Postfix(CustomModeScreen __instance)
        {
            //Add all custom run mods here
            addDailyMod.Invoke(__instance,"Series Deck", "b");
        }
    }

    @SpirePatch(clz = CustomModeScreen.class, method = "renderScreen")
    public static class CustomModeScreen_RenderScreen
    {
        @SpirePostfixPatch
        public static void Postfix(CustomModeScreen __instance, SpriteBatch sb)
        {
            //Add all custom run mods here
            FontHelper.renderSmartText(sb, FontHelper.panelNameFont, GR.Animator.Strings.SeriesUI.SeriesUI, seriesleftHb.cX - 24.0F, _scrollY.Get(__instance) - 460.0F * Settings.scale + 850.0F * Settings.scale, 9999.0F, 32.0F * Settings.scale, Settings.GOLD_COLOR);
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
            //Add all custom run mods here
            RenderSeries(__instance, sb);
        }

        private static void RenderSeries(CustomModeScreen __instance, SpriteBatch sb)
        {

            if (seriesleftHb == null || seriesRightHb == null || seriesHb == null || loadoutHb == null ||  !displaySeries)
            {
                return;
            }

            if (seriesleftHb.hovered || Settings.isControllerMode) {
                sb.setColor(Color.WHITE);
            } else {
                sb.setColor(Color.LIGHT_GRAY);
            }
            sb.draw(ImageMaster.CF_LEFT_ARROW, seriesleftHb.cX - 24.0F * Settings.scale, seriesleftHb.cY - 24.0F * Settings.scale, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);

            if (startingLoadout != null)
            {
                seriesLabel = startingLoadout.Name;
            }

            FontHelper.renderFontLeft(sb, seriesFont, seriesLabel, seriesHb.cX, seriesHb.cY, Settings.BLUE_TEXT_COLOR);

            if (seriesRightHb.hovered || Settings.isControllerMode) {
                sb.setColor(Color.WHITE);
            } else {
                sb.setColor(Color.LIGHT_GRAY);
            }
            sb.draw(ImageMaster.CF_RIGHT_ARROW, seriesRightHb.cX - 24.0F * Settings.scale, seriesRightHb.cY - 24.0F * Settings.scale, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);

            Hitbox seedHb = _seedHb.Get(__instance);
            float seedRightWidth = seedHb.cX + seedHb.width + 2.5F;
            FontHelper.renderFontLeft(sb, EYBFontHelper.CardTitleFont_Small, startingLoadout.GetDeckPreviewString(true), seedRightWidth, seedHb.cY - seriesleftHb.height - 20.0F, Settings.GREEN_TEXT_COLOR);

            if (loadoutHb.hovered || Settings.isControllerMode) {
                sb.setColor(Color.WHITE);
            } else {
                sb.setColor(Color.LIGHT_GRAY);
            }

            sb.draw(GR.Common.Images.SwapCards.Texture(), loadoutHb.x, loadoutHb.y, 0.0F, 0.0F, loadoutHb.width, loadoutHb.height, 1, 1, 0.0F, 0, 0, Math.round(loadoutHb.width), Math.round(loadoutHb.height), false, false);

            seriesleftHb.render(sb);
            seriesRightHb.render(sb);
            seriesHb.render(sb);
            loadoutHb.render(sb);
        }
    }

    @SpirePatch(clz = CustomModeScreen.class, method = "addNonDailyMods")
    public static class CustomModeScreen_AddNonDailyMods {

        @SpirePrefixPatch
        public static void Prefix(CustomModeScreen __instance, CustomTrial trial, ArrayList<String> modIds) {
            trial.setMaxHpOverride(option.getHp());
            AbstractPlayer p = AbstractDungeon.player;

            p.gold = option.getGold();
        }
    }

        private static void InitializeSeries(CustomModeScreen __instance)
    {
        seriesleftHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
        seriesRightHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
        seriesHb = new Hitbox(80.0F * Settings.scale, 80.0F * Settings.scale);
        loadoutHb = new Hitbox(32.0F * Settings.scale, 32.0F * Settings.scale);

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
                if (loadout.GetPreset().Size() > 0)
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
        if (seriesleftHb == null || seriesRightHb == null || seriesHb == null || loadoutHb == null || !displaySeries)
        {
            return;
        }

        if (seriesHb.justHovered || seriesleftHb.justHovered || seriesRightHb.justHovered || loadoutHb.justHovered) {
            _playHoverSound.Invoke(__instance);
        }

        boolean loadoutHbRightClick = false;

        if (seriesleftHb.hovered && InputHelper.justClickedLeft) {
            _playClickStartSound.Invoke(__instance);
            seriesleftHb.clickStarted = true;
         } else if (seriesRightHb.hovered && InputHelper.justClickedLeft) {
            _playClickStartSound.Invoke(__instance);
            seriesRightHb.clickStarted = true;
         } else if (seriesHb.hovered && InputHelper.justClickedLeft) {
            _playClickStartSound.Invoke(__instance);
            seriesHb.clickStarted = true;
        } else if (loadoutHb.hovered && InputHelper.justClickedLeft) {
            _playClickStartSound.Invoke(__instance);
            loadoutHb.clickStarted = true;
        } else if (loadoutHb.hovered && InputHelper.justClickedRight) {
            _playClickStartSound.Invoke(__instance);
            loadoutHb.clickStarted = true;
            loadoutHbRightClick = true;
        }

        if (seriesleftHb.clicked || CInputActionSet.topPanel.isJustPressed()) {
            //Go to previous series

            seriesleftHb.clicked = false;

            int current = loadouts.indexOf(startingLoadout);
            if (current == 0)
            {
                GR.Animator.Data.SelectedLoadout = loadouts.get(loadouts.size() - 1);
            }
            else
            {
                GR.Animator.Data.SelectedLoadout = loadouts.get(current - 1);
            }
        }
        else if (seriesRightHb.clicked || CInputActionSet.topPanel.isJustPressed()) {
            //Go to next series

            seriesRightHb.clicked = false;

            int current = loadouts.indexOf(startingLoadout);
            if (current >= (loadouts.size() - 1))
            {
                GR.Animator.Data.SelectedLoadout = loadouts.get(0);
            }
            else
            {
                GR.Animator.Data.SelectedLoadout = loadouts.get(current + 1);
            }

        }
        else if (seriesHb.clicked || CInputActionSet.topPanel.isJustPressed()) {
            //Go to random series

            seriesHb.clicked = false;

            if (availableLoadouts.size() > 1) {
                while (startingLoadout == GR.Animator.Data.SelectedLoadout) {
                    GR.Animator.Data.SelectedLoadout = GameUtilities.GetRandomElement(availableLoadouts, RNG);
                }
            }
        }
        else if (loadoutHb.clicked || CInputActionSet.topPanel.isJustPressed()) {

            loadoutHb.clicked = false;

            if (loadoutHbRightClick)
            {
                //Random loadout
                AnimatorCharacterSelectScreen.RandomizeLoadout();
            }
            else
                {
                //Run loadout editor
                
                AnimatorCharacterSelectScreen.OpenLoadoutEditor(startingLoadout, option);
            }
        }

        Hitbox seedHb = _seedHb.Get(__instance);
        float seedRightWidth = seedHb.cX + seedHb.width + 2.5F;

        seriesleftHb.move(seedRightWidth - 70.0F * 0.5F, seedHb.cY);
        seriesHb.move(seedRightWidth + 10.0F * 1.5F, seedHb.cY);

        seriesHb.width = FontHelper.getSmartWidth(seriesFont, seriesLabel, 9999f, 0f);

        seriesRightHb.move(seedRightWidth + seriesHb.width + 20.0F * 1.5F + 70.0F * 1.5F, seedHb.cY);

        loadoutHb.move(seedRightWidth, seedHb.cY - seriesleftHb.height - 20.0F - FontHelper.getHeight(EYBFontHelper.CardTitleFont_Small) - 32.0F * Settings.scale);

        seriesleftHb.update();
        seriesRightHb.update();
        seriesHb.update();
        loadoutHb.update();

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
}
