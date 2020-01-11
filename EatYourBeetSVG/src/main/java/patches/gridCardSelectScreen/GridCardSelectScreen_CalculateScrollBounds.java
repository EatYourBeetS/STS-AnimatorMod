package patches.gridCardSelectScreen;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import eatyourbeets.ui.GridCardSelectScreenPatch;

@SpirePatch(clz= GridCardSelectScreen.class, method="calculateScrollBounds")
public class GridCardSelectScreen_CalculateScrollBounds
{
    @SpirePrefixPatch
    public static SpireReturn Prefix(GridCardSelectScreen __instance)
    {
        if (GridCardSelectScreenPatch.CalculateScrollBounds(__instance))
        {
            return SpireReturn.Return(null);
        }
        else
        {
            return SpireReturn.Continue();
        }
    }
}