package patches.modHelper;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.daily.mods.AbstractDailyMod;
import com.megacrit.cardcrawl.helpers.ModHelper;
import eatyourbeets.dailymods.SeriesDeck;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.MethodInfo;

public class ModHelperPatches {
    @SpirePatch(clz = ModHelper.class, method = "initialize")
    public static class ModHelperPatches_Initialize
    {
        private static MethodInfo.T1<String, AbstractDailyMod> addStarterMod;
        private static MethodInfo.T1<String, AbstractDailyMod> addGenericMod;
        private static MethodInfo.T1<String, AbstractDailyMod> addDifficultyMod;

        @SpirePostfixPatch
        public static void Insert(ModHelper __instance)
        {
            addStarterMod = JUtils.GetMethod("addStarterMod", ModHelper.class, AbstractDailyMod.class);
            addGenericMod = JUtils.GetMethod("addGenericMod", ModHelper.class, AbstractDailyMod.class);
            addDifficultyMod = JUtils.GetMethod("addDifficultyMod", ModHelper.class, AbstractDailyMod.class);

            //Add mods here to get them to show up in the custom games screen
            addStarterMod.Invoke(__instance, new SeriesDeck());
        }
    }
}
