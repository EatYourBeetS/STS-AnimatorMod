package patches.abstractPower;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameUtilities;

@SpirePatch(clz = VulnerablePower.class, method = "atDamageReceive", paramtypez = {float.class, DamageInfo.DamageType.class})
public class VulnerablePowerPatches
{
    @SpirePostfixPatch
    public static float Postfix(float result, VulnerablePower __instance, float damage, DamageInfo.DamageType type)
    {
        if (GameUtilities.IsPlayer(__instance.owner))
        {
            return CombatStats.PlayerVulnerableModifier > 0 ? damage * ((result / damage) + CombatStats.PlayerVulnerableModifier) : result;
        }
        else
        {
            return CombatStats.EnemyVulnerableModifier > 0 ? damage * ((result / damage) + CombatStats.EnemyVulnerableModifier) : result;
        }
    }
}