package patches.screens;

import basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.compendium.CardLibSortHeader;
import com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.common.CardLibraryKeywordFilters;
import eatyourbeets.ui.common.CustomCardLibSortHeader;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.MethodInfo;

public class CardLibraryScreenPatches
{
    public static MethodInfo.T0 CalculateScrollBounds = JUtils.GetMethod("calculateScrollBounds", CardLibraryScreen.class);
    public static CardLibraryKeywordFilters KeywordFilter = new CardLibraryKeywordFilters();
    public static FieldInfo<CardGroup> VisibleCards = JUtils.GetField("visibleCards", CardLibraryScreen.class);

    @SpirePatch(clz = CardLibraryScreen.class, method = "update")
    public static class CardLibraryScreen_Update
    {
        private static FieldInfo<Boolean> _grabbedScreen = JUtils.GetField("grabbedScreen", CardLibraryScreen.class);

        @SpirePrefixPatch
        public static void Prefix(CardLibraryScreen screen)
        {
            if (KeywordFilter.TryUpdate() && KeywordFilter.HasFocus())
            {
                _grabbedScreen.Set(screen, false);
                GR.UI.CanHoverCards = false;
            }
        }
    }

    @SpirePatch(clz = CardLibraryScreen.class, method = "render", paramtypez = {SpriteBatch.class})
    public static class CardLibraryScreen_Render
    {
        @SpirePostfixPatch
        public static void Postfix(CardLibraryScreen screen, SpriteBatch sb)
        {
            KeywordFilter.TryRender(sb);
        }
    }

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

            KeywordFilter.shouldRefresh = true;
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

            if (newSelection == ColorTabBar.CurrentTab.COLORLESS || newSelection == ColorTabBarFix.Enums.MOD && ColorTabBarFix.Fields.getModTab().color.equals(GR.Animator.CardColor))
            {
                if (_sortHeader.Get(screen) != customHeader)
                {
                    _sortHeader.Set(screen, customHeader);
                }

                final boolean isColorless = newSelection == ColorTabBar.CurrentTab.COLORLESS;
                customHeader.SetupButtons(isColorless);
                KeywordFilter.SetFilters(false);
                KeywordFilter.SetActive(!isColorless);
            }
            else
            {
                if (_sortHeader.Get(screen) != defaultHeader)
                {
                    _sortHeader.Set(screen, defaultHeader);
                }

                KeywordFilter.SetActive(false);
            }
        }
    }
}