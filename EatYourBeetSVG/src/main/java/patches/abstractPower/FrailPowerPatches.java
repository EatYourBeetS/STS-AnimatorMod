package patches.abstractPower;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.powers.FrailPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameUtilities;

@SpirePatch(clz = FrailPower.class, method = "modifyBlock", paramtypez = {float.class})
public class FrailPowerPatches
{
    @SpirePostfixPatch
    public static float Postfix(float result, FrailPower __instance, float block)
    {
        if (GameUtilities.IsPlayer(__instance.owner))
        {
            return CombatStats.PlayerFrailModifier > 0 ? block * ((result / block) - CombatStats.PlayerFrailModifier) : result;
        }
        else
        {
            return CombatStats.EnemyFrailModifier > 0 ? block * ((result / block) - CombatStats.EnemyFrailModifier) : result;
        }
    }
}