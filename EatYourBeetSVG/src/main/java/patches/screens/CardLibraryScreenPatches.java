package patches.screens;

import basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.compendium.CardLibSortHeader;
import com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.common.CustomCardLibSortHeader;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;

public class CardLibraryScreenPatches
{
    @SpirePatch(clz = CardLibraryScreen.class, method = "open")
    public static class CardLibraryScreen_Open
    {
        private static final FieldInfo<ColorTabBar> _colorBar = JUtils.GetField("colorBar", CardLibraryScreen.class);

        @SpirePrefixPatch
        public static void Prefix(CardLibraryScreen screen)
        {
            ColorTabBar tabBar = _colorBar.Get(screen);
            if (tabBar.curTab != ColorTabBarFix.Enums.MOD)
            {
                screen.didChangeTab(tabBar, tabBar.curTab = ColorTabBarFix.Enums.MOD);
            }
        }
    }

    @SpirePatch(clz = CardLibraryScreen.class, method = "didChangeTab", paramtypez = {ColorTabBar.class, ColorTabBar.CurrentTab.class})
    public static class CardLibraryScreen_DidChangeTab
    {
        private static final FieldInfo<CardLibSortHeader> _sortHeader = JUtils.GetField("sortHeader", CardLibraryScreen.class);
        private static final CustomCardLibSortHeader customHeader = new CustomCardLibSortHeader(null);
        private static CardLibSortHeader defaultHeader;

        @SpirePrefixPatch
        public static void Prefix(CardLibraryScreen screen, ColorTabBar tabBar, ColorTabBar.CurrentTab newSelection)
        {
            CustomCardLibSortHeader.Screen = screen;

            if (defaultHeader == null)
            {
                defaultHeader = _sortHeader.Get(screen);

                Hitbox upgradeHitbox = tabBar.viewUpgradeHb;

                float offsetX = 130 * Settings.scale;

                upgradeHitbox.width -= offsetX;
                //upgradeHitbox.move(upgradeHitbox.cX + (offsetX * 2), upgradeHitbox.cY);
            }

            if (newSelection == ColorTabBar.CurrentTab.COLORLESS || newSelection == ColorTabBarFix.Enums.MOD && ColorTabBarFix.Fields.getModTab().color.equals(GR.Enums.Cards.THE_ANIMATOR))
            {
                if (_sortHeader.Get(screen) != customHeader)
                {
                    _sortHeader.Set(screen, customHeader);
                }

                customHeader.SetupButtons(newSelection == ColorTabBar.CurrentTab.COLORLESS);
            }
            else
            {
                if (_sortHeader.Get(screen) != defaultHeader)
                {
                    _sortHeader.Set(screen, defaultHeader);
                }
            }
        }

        @SpirePostfixPatch
        public static void Postfix(CardLibraryScreen screen, ColorTabBar tabBar, ColorTabBar.CurrentTab newSelection) {
            GR.UI.CardFilters.Open(customHeader.group.group, __ -> customHeader.UpdateForFilters());
        }
    }

    @SpirePatch(clz= CardLibraryScreen.class, method="update")
    public static class CardLibraryScreen_Update
    {
        private static FieldInfo<Boolean> _grabbedScreen = JUtils.GetField("grabbedScreen", CardLibraryScreen.class);

        @SpirePrefixPatch
        public static void Prefix(CardLibraryScreen __instance)
        {
            if (GR.UI.CardFilters.TryUpdate() && GR.UI.CardFilters.isActive)
            {
                _grabbedScreen.Set(__instance, false);
            }
        }
    }

    @SpirePatch(clz= CardLibraryScreen.class, method="render")
    public static class CardLibraryScreen_Render
    {
        @SpirePrefixPatch
        public static void Postfix(CardLibraryScreen __instance, SpriteBatch sb)
        {
            GR.UI.CardFilters.TryRender(sb);
        }
    }
}