package patches.customModeScreen;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.MethodInfo;

public class CustomModeScreen {

    @SpirePatch(clz = CustomModeScreen.class, method = "initializeMods")
    public static class CustomModeScreen_InitializeMods
    {
        private static MethodInfo.T2<String, String, String> addDailyMod = JUtils.GetMethod("addDailyMod", CustomModeScreen.class, String.class, String.class);
        private static MethodInfo.T3<String, String, String, Boolean> addMod = JUtils.GetMethod("addMod", CustomModeScreen.class, String.class, String.class, Boolean.class);

        @SpirePostfixPatch
        public static void Postfix(CustomModeScreen __instance)
        {
            //Add all custom run mods here
            addDailyMod.Invoke(__instance,"Series Deck", "");
        }
    }
}
