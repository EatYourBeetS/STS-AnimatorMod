package patches.customModeScreen;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.screens.custom.CustomModeScreen;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.MethodInfo;


public class CustomModeScreenPatch {

    @SpirePatch(clz = CustomModeScreen.class, method = "initializeMods")
    public static class CustomModeScreen_InitializeMods
    {
        private static MethodInfo.T2<String, String, String> addDailyMod = JUtils.GetMethod("addDailyMod", CustomModeScreen.class, String.class, String.class);

        @SpirePostfixPatch
        public static void Postfix(CustomModeScreen __instance)
        {
            //Add all custom run mods here
            addDailyMod.Invoke(__instance,"Series Deck", "b");
        }
    }
}
