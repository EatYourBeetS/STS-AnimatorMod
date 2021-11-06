package patches.screens;

import basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.compendium.CardLibSortHeader;
import com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.common.CustomCardLibSortHeader;
import eatyourbeets.ui.controls.GUI_Button;
import eatyourbeets.ui.hitboxes.DraggableHitbox;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;

public class CardLibraryScreenPatches
{
    private static GUI_Button openButton;
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

            Hitbox upgradeHitbox = tabBar.viewUpgradeHb;
            float offsetX = 130 * Settings.scale;
            upgradeHitbox.width -= offsetX;
            if (_sortHeader.Get(screen) != customHeader)
            {
                _sortHeader.Set(screen, customHeader);
            }

            customHeader.SetupButtons(!(newSelection == ColorTabBarFix.Enums.MOD && ColorTabBarFix.Fields.getModTab().color.equals(GR.Enums.Cards.THE_ANIMATOR)));
        }

        @SpirePostfixPatch
        public static void Postfix(CardLibraryScreen screen, ColorTabBar tabBar, ColorTabBar.CurrentTab newSelection) {
            GR.UI.CardFilters.Initialize(__ -> customHeader.UpdateForFilters(), customHeader.originalGroup);
            customHeader.UpdateForFilters();
            if (openButton == null) {
                openButton = new GUI_Button(GR.Common.Images.HexagonalButton.Texture(), new DraggableHitbox(0, 0, Settings.WIDTH * 0.07f, Settings.HEIGHT * 0.07f, false).SetIsPopupCompatible(true))
                        .SetBorder(GR.Common.Images.HexagonalButtonBorder.Texture(), Color.WHITE)
                        .SetPosition(Settings.WIDTH * 0.96f, Settings.HEIGHT * 0.95f).SetText(GR.Animator.Strings.Misc.Filters)
                        .SetOnClick(() -> {
                            if (GR.UI.CardFilters.isActive) {
                                GR.UI.CardFilters.Close();
                            }
                            else {
                                GR.UI.CardFilters.Open();
                            }
                        })
                        .SetColor(Color.GRAY);
            }
        }
    }

    @SpirePatch(clz= CardLibraryScreen.class, method="update")
    public static class CardLibraryScreen_Update
    {
        private static FieldInfo<Boolean> _grabbedScreen = JUtils.GetField("grabbedScreen", CardLibraryScreen.class);

        @SpirePrefixPatch
        public static void Prefix(CardLibraryScreen __instance)
        {
            if (openButton != null) {
                openButton.SetColor(GR.UI.CardFilters.isActive ? Color.WHITE : Color.GRAY).TryUpdate();
            }
            if (GR.UI.CardFilters.TryUpdate())
            {
                _grabbedScreen.Set(__instance, false);
            }
        }
    }

    @SpirePatch(clz = CardLibraryScreen.class, method = "updateScrolling")
    public static class CardLibraryScreen_UpdateScrolling
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> Prefix(CardLibraryScreen __instance)
        {
            if (GR.UI.CardFilters.TryUpdate()) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz= CardLibraryScreen.class, method="render", paramtypez = {SpriteBatch.class})
    public static class CardLibraryScreen_Render
    {
        @SpirePrefixPatch
        public static void Postfix(CardLibraryScreen __instance, SpriteBatch sb)
        {
            if (openButton != null) {
                openButton.TryRender(sb);
            }
        }
    }
}