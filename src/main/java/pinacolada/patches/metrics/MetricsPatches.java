package pinacolada.patches.metrics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import eatyourbeets.utilities.MethodInfo;
import pinacolada.characters.FoolCharacter;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class MetricsPatches
{
    private static final MethodInfo.T2<Object, Object, Object> _addData = PCLJUtils.GetMethod("addData", Metrics.class, Object.class, Object.class);

    @SpirePatch(clz = Metrics.class, method = "gatherAllData", paramtypez = {boolean.class, boolean.class, MonsterGroup.class})
    public static class MetricPatches_gatherAllData
    {
        @SpirePostfixPatch
        public static void Postfix(Metrics __instance, boolean death, boolean trueVictor, MonsterGroup monsters)
        {
            if (PCLGameUtilities.IsPlayerClass(GR.PCL.PlayerClass))
            {
                final String key = "loadout"; // Reusing an unused field in RunData.class
                _addData.Invoke(__instance, key, FoolCharacter.OVERRIDE_NAME);
            }
        }
    }
}
