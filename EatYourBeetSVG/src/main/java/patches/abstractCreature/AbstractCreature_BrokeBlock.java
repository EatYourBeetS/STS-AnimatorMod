package patches.abstractCreature;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.CombatStats;

@SpirePatch(clz = AbstractCreature.class, method = "brokeBlock")
public class AbstractCreature_BrokeBlock
{
    @SpirePostfixPatch
    public static void Method(AbstractCreature __instance)
    {
        CombatStats.OnBlockBroken(__instance);
    }
}