package patches.customModeScreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.custom.CustomModeScreen;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.MethodInfo;

import javax.xml.transform.Result;


public class CustomModeScreenPatch {

    private static final FieldInfo<Float> _scrollY = JUtils.GetField("scrollY", CustomModeScreen.class);
    private static final FieldInfo<Hitbox> _seedHb = JUtils.GetField("seedHb", CustomModeScreen.class);
    private static final MethodInfo.T0<Result> _playHoverSound = JUtils.GetMethod("playHoverSound", CustomModeScreen.class);
    private static final MethodInfo.T0<Result> _playClickStartSound = JUtils.GetMethod("playClickStartSound", CustomModeScreen.class);

    public static Hitbox seriesleftHb = null;
    public static Hitbox seriesRightHb = null;

    @SpirePatch(clz = CustomModeScreen.class, method = "open")
    public static class CustomModeScreen_Open
    {
        @SpirePostfixPatch
        public static void Postfix(CustomModeScreen __instance)
        {
            //Add all custom run mods here
            InitializeSeriesArrows(__instance);
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
            if (seriesleftHb == null || seriesRightHb == null)
            {
                return;
            }

            if (seriesleftHb.hovered || Settings.isControllerMode) {
                sb.setColor(Color.WHITE);
            } else {
                sb.setColor(Color.LIGHT_GRAY);
            }
            sb.draw(ImageMaster.CF_LEFT_ARROW, seriesleftHb.cX - 24.0F * Settings.scale, seriesleftHb.cY - 24.0F * Settings.scale, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);

            if (seriesRightHb.hovered || Settings.isControllerMode) {
                sb.setColor(Color.WHITE);
            } else {
                sb.setColor(Color.LIGHT_GRAY);
            }
            sb.draw(ImageMaster.CF_RIGHT_ARROW, seriesRightHb.cX - 24.0F * Settings.scale, seriesRightHb.cY - 24.0F * Settings.scale, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);

            seriesleftHb.render(sb);
            seriesRightHb.render(sb);
        }
    }

    public static void InitializeSeriesArrows(CustomModeScreen __instance)
    {
        seriesleftHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
        seriesRightHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);

        UpdateSeriesArrows(__instance);
    }

    public static void UpdateSeriesArrows(CustomModeScreen __instance)
    {
        if (seriesleftHb == null || seriesRightHb == null)
        {
            return;
        }

        Hitbox seedHb = _seedHb.Get(__instance);

        float seedRightWidth = seedHb.cX + seedHb.width + 2.5F;

        seriesleftHb.move(seedRightWidth - 70.0F * 0.5F, seedHb.cY);
        seriesRightHb.move(seedRightWidth + 70.0F * 1.5F, seedHb.cY);

        seriesleftHb.update();
        seriesRightHb.update();

        if (seriesleftHb.justHovered || seriesRightHb.justHovered) {
            _playHoverSound.Invoke(__instance);
        }

        if (seriesleftHb.hovered && InputHelper.justClickedLeft) {
            _playClickStartSound.Invoke(__instance);
            seriesleftHb.clickStarted = true;
         } else if (seriesRightHb.hovered && InputHelper.justClickedLeft) {
            _playClickStartSound.Invoke(__instance);
            seriesRightHb.clickStarted = true;
         }
    }
}
