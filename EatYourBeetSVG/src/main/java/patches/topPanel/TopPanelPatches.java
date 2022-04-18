package patches.topPanel;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

public class TopPanelPatches
{
    @SpirePatch(clz = TopPanel.class, method = "renderDungeonInfo", paramtypez = {SpriteBatch.class})
    public static class TopPanelPatches_RenderDungeonInfo
    {
        private static int ascensionCache;

        @SpirePrefixPatch
        public static void Prefix(TopPanel __instance, SpriteBatch sb)
        {
            ascensionCache = AbstractDungeon.ascensionLevel;

            final int actualAscension = GameUtilities.GetActualAscensionLevel();
            if (actualAscension > 20)
            {
                AbstractDungeon.ascensionLevel = actualAscension;
            }
        }

        @SpirePostfixPatch
        public static void Postfix(TopPanel __instance, SpriteBatch sb)
        {
            AbstractDungeon.ascensionLevel = ascensionCache;
        }
    }

    @SpirePatch(clz = TopPanel.class, method = "update")
    public static class TopPanelPatches_Update
    {
        @SpirePrefixPatch
        public static SpireReturn Method(TopPanel __instance)
        {
            // To simulate AbstractDungeon.screen == CurrentScreen.NO_INTERACT
            if (AbstractDungeon.screen == GR.Enums.Screens.EYB_SCREEN || Settings.hideTopBar)
            {
                return SpireReturn.Return(null);
            }
            else
            {
                return SpireReturn.Continue();
            }
        }
    }
}