package patches.abstractCreature;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.CombatStats;

@SpirePatch(clz= AbstractCreature.class, method = "loseBlock", paramtypez = {int.class, boolean.class})
public class AbstractCreature_LoseBlock
{
    @SpirePrefixPatch
    public static void Method(AbstractCreature __instance, int amount, boolean noAnimation)
    {
        CombatStats.OnBeforeLoseBlock(__instance, amount, noAnimation);
    }
}