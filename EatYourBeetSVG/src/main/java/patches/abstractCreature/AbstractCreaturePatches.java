package patches.abstractCreature;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.CombatStats;

public class AbstractCreaturePatches
{
    @SpirePatch(clz = AbstractCreature.class, method = "<class>")
    public static class AbstractCreature_Fields
    {
        public static final SpireField<Boolean> increasingMaxHP = new SpireField<>(() -> false);
    }

    @SpirePatch(clz = AbstractCreature.class, method = "increaseMaxHp", paramtypez = {int.class, boolean.class})
    public static class AbstractCreaturePatches_IncreaseMaxHP
    {
        @SpirePostfixPatch
        public static void Prefix(AbstractCreature __instance, int amount, boolean showEffect)
        {
            AbstractCreature_Fields.increasingMaxHP.set(__instance, true);
        }

        @SpirePostfixPatch
        public static void Postfix(AbstractCreature __instance, int amount, boolean showEffect)
        {
            AbstractCreature_Fields.increasingMaxHP.set(__instance, false);
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "healthBarUpdatedEvent")
    public static class AbstractCreaturePatches_HealthBarUpdatedEvent
    {
        @SpirePostfixPatch
        public static void Method(AbstractCreature __instance)
        {
            CombatStats.OnHealthBarUpdated(__instance);
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "addBlock", paramtypez = {int.class})
    public static class AbstractCreaturePatches_AddBlock
    {
        @SpirePostfixPatch
        public static void Method(AbstractCreature __instance, int block)
        {
            CombatStats.OnBlockGained(__instance, block);
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "brokeBlock")
    public static class AbstractCreaturePatches_BrokeBlock
    {
        @SpirePostfixPatch
        public static void Method(AbstractCreature __instance)
        {
            CombatStats.OnBlockBroken(__instance);
        }
    }

    @SpirePatch(clz= AbstractCreature.class, method = "loseBlock", paramtypez = {int.class, boolean.class})
    public static class AbstractCreaturePatches_LoseBlock
    {
        @SpirePrefixPatch
        public static void Method(AbstractCreature __instance, int amount, boolean noAnimation)
        {
            CombatStats.OnBeforeLoseBlock(__instance, amount, noAnimation);
        }
    }
}