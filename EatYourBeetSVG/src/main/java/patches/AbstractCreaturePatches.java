package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.PlayerStatistics;

public class AbstractCreaturePatches
{
    @SpirePatch(clz= AbstractCreature.class, method = "brokeBlock")
    public static class AbstractCreaturePatch_BrokeBlock
    {
        @SpirePostfixPatch
        public static void Method(AbstractCreature __instance)
        {
            PlayerStatistics.Instance.OnBlockBroken(__instance);
        }
    }

    @SpirePatch(clz= AbstractCreature.class, method = "loseBlock", paramtypez = {int.class, boolean.class})
    public static class AbstractCreaturePatch_LoseBlock
    {
        @SpirePrefixPatch
        public static void Method(AbstractCreature __instance, int amount, boolean noAnimation)
        {
            PlayerStatistics.Instance.OnBeforeLoseBlock(__instance, amount, noAnimation);
        }
    }
}