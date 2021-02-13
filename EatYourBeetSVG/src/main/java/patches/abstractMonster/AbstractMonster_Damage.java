package patches.abstractMonster;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractMonster.class, method = "damage", paramtypez = {DamageInfo.class})
public class AbstractMonster_Damage
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
