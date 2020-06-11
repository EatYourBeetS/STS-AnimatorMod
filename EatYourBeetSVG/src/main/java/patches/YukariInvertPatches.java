package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.cards.animator.beta.TouhouProject.YukariYakumo;

public class YukariInvertPatches
{

    @SpirePatch(
            clz = VulnerablePower.class,
            method = "atDamageReceive"
    )
    public static class InvertVulnerable
    {
        public static float Postfix(float __result, VulnerablePower __instance, float damage, DamageInfo.DamageType type)
        {
            if (__instance.owner.hasPower(YukariYakumo.InvertPower.ID) && type == DamageInfo.DamageType.NORMAL)
            {
                float originalMultiplier = __result / damage;
                float invertedMultiplier = (1 / originalMultiplier);
                return damage * invertedMultiplier;
            }
            else
            {
                return __result;
            }
        }
    }

    @SpirePatch(
            clz = WeakPower.class,
            method = "atDamageGive"
    )
    public static class InvertWeak
    {
        public static float Postfix(float __result, WeakPower __instance, float damage, DamageInfo.DamageType type)
        {
            if (__instance.owner.hasPower(YukariYakumo.InvertPower.ID) && type == DamageInfo.DamageType.NORMAL)
            {
                float originalMultiplier = __result / damage;
                float invertedMultiplier = (1 / originalMultiplier);
                return damage * invertedMultiplier;
            }
            else
            {
                return __result;
            }
        }
    }

    @SpirePatch(
            clz = FrailPower.class,
            method = "modifyBlock"
    )
    public static class InvertFrail
    {
        public static float Postfix(float __result, FrailPower __instance, float blockAmount)
        {
            if (__instance.owner.hasPower(YukariYakumo.InvertPower.ID))
            {
                float originalMultiplier = __result / blockAmount;
                float invertedMultiplier = (1 / originalMultiplier);
                return blockAmount * invertedMultiplier;
            }
            else
            {
                return __result;
            }
        }
    }
}