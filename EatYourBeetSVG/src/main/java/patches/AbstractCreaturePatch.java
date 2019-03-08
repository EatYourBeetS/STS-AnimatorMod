package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.PlayerStatistics;

@SpirePatch(clz= AbstractCreature.class, method = "brokeBlock")
public class AbstractCreaturePatch
{
    @SpirePostfixPatch
    public static void Method(AbstractCreature __instance)
    {
        PlayerStatistics.Instance.OnBlockBroken(__instance);
    }
}