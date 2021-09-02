package patches.abstractMonster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import javassist.CtBehavior;

public class AbstractMonsterPatches
{
    @SpirePatch(clz = AbstractMonster.class, method = "<class>")
    public static class AbstractMonster_Fields
    {
        public static final SpireField<EnemyIntent> enemyIntent = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage", paramtypez = {DamageInfo.class})
    public static class AbstractMonster_Damage
    {
        @SpireInsertPatch(localvars = {"damageAmount"}, locator = Locator.class)
        public static void InsertPre(AbstractMonster __instance, DamageInfo info, @ByRef int[] damageAmount)
        {
            damageAmount[0] = Math.max(0, CombatStats.OnModifyDamage(__instance, info, damageAmount[0]));
        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher matcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "decrementBlock");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    @SpirePatch(clz= AbstractMonster.class, method = "die", paramtypez = {boolean.class})
    public static class AbstractMonster_Die
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

    @SpirePatch(clz= AbstractMonster.class, method = "renderDamageRange", paramtypez = {SpriteBatch.class})
    public static class AbstractMonster_Render
    {
        @SpirePrefixPatch
        public static void Method(AbstractMonster __instance, SpriteBatch sb)
        {
            GR.UI.CombatScreen.Intents.RenderMonsterInfo(__instance, sb);
        }
    }

    @SpirePatch(clz= AbstractMonster.class, method = "renderTip", paramtypez = {SpriteBatch.class})
    public static class AbstractMonster_RenderTip
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractMonster __instance, SpriteBatch sb)
        {
            EYBCardTooltip.QueueTooltips(__instance);
            return SpireReturn.Return();
        }
    }
}
