package patches.gridCardSelectScreen;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import eatyourbeets.ui.screens.GridCardSelectScreenPatch;

@SpirePatch(clz= GridCardSelectScreen.class, method="callOnOpen")
public class GridCardSelectScreen_CallOnOpen
{
    @SpirePostfixPatch
    public static void Postfix(GridCardSelectScreen __instance)
    {
        GridCardSelectScreenPatch.Open(__instance);
    }
}