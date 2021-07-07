package patches.abstractMonster;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;

@SpirePatch(clz= AbstractMonster.class, method = "die", paramtypez = {boolean.class})
public class AbstractMonster_Die
{
    @SpirePrefixPatch
    public static void Method(AbstractMonster __instance, boolean triggerRelics)
    {
        if (!__instance.isDying) // to avoid triggering this more than once
        {
            CombatStats.OnEnemyDying(__instance, triggerRelics);
        }
    }
}