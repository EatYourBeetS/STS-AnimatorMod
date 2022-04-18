package patches.metrics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.MethodInfo;

import java.util.HashMap;

public class MetricsPatches
{
    private static final MethodInfo.T2<Object, Object, Object> _addData = JUtils.GetMethod("addData", Metrics.class, Object.class, Object.class);
    private static final FieldInfo<HashMap<Object, Object>> _params = JUtils.GetField("params", Metrics.class);

    @SpirePatch(clz = Metrics.class, method = "gatherAllData", paramtypez = {boolean.class, boolean.class, MonsterGroup.class})
    public static class MetricPatches_gatherAllData
    {
        @SpirePostfixPatch
        public static void Postfix(Metrics __instance, boolean death, boolean trueVictor, MonsterGroup monsters)
        {
            final int actualAscension = GameUtilities.GetActualAscensionLevel();
            if (actualAscension > 20)
            {
                _params.Get(__instance).put("ascension_level", actualAscension);
            }

            if (GameUtilities.IsPlayerClass(GR.Animator.PlayerClass))
            {
                final String key = "loadout"; // Reusing an unused field in RunData.class
                _addData.Invoke(__instance, key, AnimatorCharacter.OVERRIDE_NAME);
            }
        }
    }
}
