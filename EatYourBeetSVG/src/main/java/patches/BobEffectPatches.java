package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.vfx.BobEffect;

public class BobEffectPatches
{
    public static boolean IsEnabled(BobEffect effect)
    {
        return BobEffect_Fields.enabled.get(effect);
    }

    public static boolean SetEnabled(BobEffect effect, boolean value)
    {
        BobEffect_Fields.enabled.set(effect, value);
        return value;
    }

    @SpirePatch(clz = BobEffect.class, method = "<class>")
    public static class BobEffect_Fields
    {
        public static final SpireField<Boolean> enabled = new SpireField<>(() -> true);
    }

    @SpirePatch(clz = BobEffect.class, method = "update")
    public static class BobEffect_Update
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(BobEffect __instance)
        {
            return IsEnabled(__instance) ? SpireReturn.Continue() : SpireReturn.Return();
        }
    }
}
