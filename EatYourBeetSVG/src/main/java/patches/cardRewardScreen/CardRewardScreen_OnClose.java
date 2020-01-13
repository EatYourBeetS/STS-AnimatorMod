package patches.cardRewardScreen;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.ui.screens.CardRewardScreenPatch;

@SpirePatch(clz= CardRewardScreen.class, method="onClose")
public class CardRewardScreen_OnClose
{
    @SpirePostfixPatch
    public static void Postfix(CardRewardScreen __instance)
    {
        CardRewardScreenPatch.OnClose(__instance);
    }
}