package patches.abstractPower;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameUtilities;

@SpirePatch(clz = WeakPower.class, method = "atDamageGive", paramtypez = {float.class, DamageInfo.DamageType.class})
public class WeakPowerPatches
{
    @SpirePostfixPatch
    public static float Postfix(float result, WeakPower __instance, float damage, DamageInfo.DamageType type)
    {
        if (GameUtilities.IsPlayer(__instance.owner))
        {
            return CombatStats.PlayerWeakModifier > 0 ? damage * ((result / damage) - CombatStats.PlayerWeakModifier) : result;
        }
        else
        {
            return CombatStats.EnemyWeakModifier > 0 ? damage * ((result / damage) - CombatStats.EnemyWeakModifier) : result;
        }
    }
}