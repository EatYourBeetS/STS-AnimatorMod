package patches.actions;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class LoseHPActionPatches
{
    @SpirePatch(clz = LoseHPAction.class, method = "update")
    public static class LoseHPActionPatches_Update
    {
        private static LoseHPAction current;

        @SpirePrefixPatch
        public static void Prefix(LoseHPAction __instance)
        {
            if (__instance != current)
            {
                current = __instance;

                if (__instance.target == AbstractDungeon.player && GameUtilities.IsEYBPlayerClass() && GameUtilities.IsPlayerTurn(false))
                {
                    if (__instance.amount >= 999)
                    {
                        JUtils.LogInfo(LoseHPActionPatches.class, "Dealing 999 or more damage, instakill will not be prevented.");
                        return;
                    }

                    final int hp = GameUtilities.GetHP(__instance.target, true, false);
                    if (__instance.amount >= hp)
                    {
                        __instance.amount = hp - 1;
                    }
                    if (__instance.amount <= 0)
                    {
                        __instance.isDone = true;
                    }
                }
            }
        }
    }
}
