package pinacolada.patches.screens;

import basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.compendium.CardLibSortHeader;
import com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
import eatyourbeets.utilities.FieldInfo;
import pinacolada.resources.GR;
import pinacolada.ui.common.CustomCardLibSortHeader;
import pinacolada.ui.controls.GUI_Button;
import pinacolada.ui.hitboxes.DraggableHitbox;
import pinacolada.utilities.PCLJUtils;

public class CardLibraryScreenPatches
{
    private static final FieldInfo<AbstractCard> hoveredCards = PCLJUtils.GetField("hoveredCard", CardLibraryScreen.class);
    private static final FieldInfo<ColorTabBar> _colorBar = PCLJUtils.GetField("colorBar", CardLibraryScreen.class);
    private static GUI_Button openButton;
    @SpirePatch(clz = CardLibraryScreen.class, method = "open")
    public static class CardLibraryScreen_Open
    {

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
        private static final FieldInfo<CardLibSortHeader> _sortHeader = PCLJUtils.GetField("sortHeader", CardLibraryScreen.class);
        private static final CustomCardLibSortHeader customHeader = new CustomCardLibSortHeader(null);
        private static CardLibSortHeader defaultHeader;

        @SpireInsertPatch(rloc = 0)
        public static void Insert(CardLibraryScreen screen, ColorTabBar tabBar, ColorTabBar.CurrentTab newSelection)
        {
            if (!IsAnimator(screen)) {
                CustomCardLibSortHeader.Screen = screen;

                Hitbox upgradeHitbox = tabBar.viewUpgradeHb;
                upgradeHitbox.width = 260 * Settings.scale;
                if (_sortHeader.Get(screen) != customHeader)
                {
                    _sortHeader.Set(screen, customHeader);
                }

                customHeader.SetupButtons(!(newSelection == ColorTabBarFix.Enums.MOD && ColorTabBarFix.Fields.getModTab().color.equals(GR.Enums.Cards.THE_FOOL)));
            }
        }

        @SpirePostfixPatch
        public static void Postfix(CardLibraryScreen screen, ColorTabBar tabBar, ColorTabBar.CurrentTab newSelection) {
            GR.UI.CardFilters.Initialize(__ -> customHeader.UpdateForFilters(), customHeader.originalGroup, customHeader.IsColorless());
            customHeader.UpdateForFilters();
            if (openButton == null) {
                openButton = new GUI_Button(GR.PCL.Images.HexagonalButton.Texture(), new DraggableHitbox(0, 0, Settings.WIDTH * 0.07f, Settings.HEIGHT * 0.07f, false).SetIsPopupCompatible(true))
                        .SetBorder(GR.PCL.Images.HexagonalButtonBorder.Texture(), Color.WHITE)
                        .SetPosition(Settings.WIDTH * 0.96f, Settings.HEIGHT * 0.95f).SetText(GR.PCL.Strings.Misc.Filters)
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
        private static FieldInfo<Boolean> _grabbedScreen = PCLJUtils.GetField("grabbedScreen", CardLibraryScreen.class);

        @SpirePrefixPatch
        public static void Prefix(CardLibraryScreen __instance)
        {
            if (openButton != null && !IsAnimator(__instance)) {
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
            if (openButton != null && !IsAnimator(__instance)) {
                openButton.TryRender(sb);
            }
        }
    }

    protected static boolean IsAnimator(CardLibraryScreen screen) {
        ColorTabBar tabBar = _colorBar.Get(screen);
        return tabBar != null && tabBar.curTab == ColorTabBarFix.Enums.MOD && ColorTabBarFix.Fields.getModTab().color.equals(eatyourbeets.resources.GR.Enums.Cards.THE_ANIMATOR);
    }
}