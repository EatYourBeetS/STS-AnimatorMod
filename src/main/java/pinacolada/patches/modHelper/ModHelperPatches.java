package pinacolada.patches.modHelper;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.daily.mods.AbstractDailyMod;
import com.megacrit.cardcrawl.helpers.ModHelper;
import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.utilities.MethodInfo;
import pinacolada.dailymods.PCLDailyMod;
import pinacolada.utilities.PCLJUtils;

public class ModHelperPatches {

    @SpirePatch(clz = ModHelper.class, method = SpirePatch.STATICINITIALIZER)
    public static class ModHelperPatches_Initialize
    {
        private static final MethodInfo.T1<String, AbstractDailyMod> addStarterMod = PCLJUtils.GetMethod("addStarterMod", ModHelper.class, AbstractDailyMod.class);
        private static final MethodInfo.T1<String, AbstractDailyMod> addGenericMod = PCLJUtils.GetMethod("addGenericMod", ModHelper.class, AbstractDailyMod.class);
        private static final MethodInfo.T1<String, AbstractDailyMod> addDifficultyMod = PCLJUtils.GetMethod("addDifficultyMod", ModHelper.class, AbstractDailyMod.class);

        @SpirePostfixPatch
        public static void Postfix()
        {
            for (FuncT0<PCLDailyMod> mod : PCLDailyMod.mods)
            {
                addStarterMod.Invoke(null, mod.Invoke());
            }
        }
    }
}
