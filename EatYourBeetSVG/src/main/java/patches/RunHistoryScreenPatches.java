package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryScreen;
import com.megacrit.cardcrawl.screens.stats.RunData;

public class RunHistoryScreenPatches
{
//    @SpirePatch(clz= RunHistoryScreen.class, method="cardForName", paramtypez = {RunData.class, String.class})
//    public static class RunHistoryScreen_CardForName
//    {
//        @SpirePrefixPatch
//        public static void Prefix(RunHistoryScreen __instance, RunData runData, String cardID)
//        {
//            CardLibraryPatches.CardLibraryPatches_getCard.allowed = true;
//        }
//
//        @SpirePostfixPatch
//        public static void Postfix(RunHistoryScreen __instance, RunData runData, String cardID)
//        {
//            CardLibraryPatches.CardLibraryPatches_getCard.allowed = false;
//        }
//    }
}