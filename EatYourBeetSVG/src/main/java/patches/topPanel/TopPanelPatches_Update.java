package patches.topPanel;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import eatyourbeets.resources.GR;

@SpirePatch(clz= TopPanel.class, method="update")
public class TopPanelPatches_Update
{
    @SpirePrefixPatch
    public static SpireReturn Method(TopPanel __instance)
    {
        // TODO: Render special button
        if (AbstractDungeon.screen == GR.Enums.Screens.EYB_SCREEN)
        {
            return SpireReturn.Return(null);
        }
        else
        {
            return SpireReturn.Continue();
        }
    }
}