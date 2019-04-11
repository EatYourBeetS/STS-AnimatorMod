package patches;

import basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.compendium.CardLibSortHeader;
import com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
import eatyourbeets.ui.CustomCardLibSortHeader;

import java.lang.reflect.Field;

@SpirePatch(clz=CardLibraryScreen.class, method="didChangeTab", paramtypez = {ColorTabBar.class, ColorTabBar.CurrentTab.class})
public class CardLibraryScreenPatches
{
    private static CustomCardLibSortHeader customHeader;
    private static CardLibSortHeader defaultHeader;
    private static Field headerField;

    @SpirePrefixPatch
    public static void Prefix(CardLibraryScreen screen, ColorTabBar tabBar, ColorTabBar.CurrentTab newSelection) throws IllegalAccessException
    {
        if (defaultHeader == null)
        {
            defaultHeader = GetCurrentHeader(screen);

            Hitbox upgradeHitbox = tabBar.viewUpgradeHb;

            float offsetX = 130 * Settings.scale;

            upgradeHitbox.width -= offsetX;
            //upgradeHitbox.move(upgradeHitbox.cX + (offsetX * 2), upgradeHitbox.cY);
        }

        if (newSelection == ColorTabBarFix.Enums.MOD && ColorTabBarFix.Fields.getModTab().color.equals(AbstractEnums.Cards.THE_ANIMATOR))
        {
            if (GetCurrentHeader(screen) != customHeader)
            {
                SetCurrentHeader(screen, customHeader);
                customHeader.SetupButtons();
            }
        }
        else
        {
            if (GetCurrentHeader(screen) != defaultHeader)
            {
                SetCurrentHeader(screen, defaultHeader);
            }
        }
    }

    private static CardLibSortHeader GetCurrentHeader(CardLibraryScreen screen) throws IllegalAccessException
    {
        return (CardLibSortHeader) headerField.get(screen);
    }

    private static void SetCurrentHeader(CardLibraryScreen screen, CardLibSortHeader header) throws IllegalAccessException
    {
        headerField.set(screen, header);
    }

    static
    {
        try
        {
            headerField = CardLibraryScreen.class.getDeclaredField("sortHeader");
            headerField.setAccessible(true);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }

        customHeader = new CustomCardLibSortHeader(null);
    }
}