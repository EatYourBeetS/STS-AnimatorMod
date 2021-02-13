package patches.abstractPlayer;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.powers.CombatStats;
import javassist.CtBehavior;

@SpirePatch(clz = AbstractPlayer.class, method = "damage", paramtypez = {DamageInfo.class})
public class AbstractPlayer_Damage
{
    @SpireInsertPatch(localvars = {"damageAmount"}, locator = Locator.class)
    public static void InsertPre(AbstractPlayer __instance, DamageInfo info, @ByRef int[] damageAmount)
    {
        damageAmount[0] = Math.max(0, CombatStats.OnModifyDamage(__instance, info, damageAmount[0]));
    }

    private static class Locator extends SpireInsertLocator
    {
        public int[] Locate(CtBehavior ctBehavior) throws Exception
        {
            Matcher matcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "decrementBlock");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
