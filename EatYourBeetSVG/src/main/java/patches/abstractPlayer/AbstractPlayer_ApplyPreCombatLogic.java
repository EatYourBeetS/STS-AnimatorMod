package patches.abstractPlayer;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.powers.CombatStats;

@SpirePatch(clz = AbstractPlayer.class, method = "applyPreCombatLogic")
public class AbstractPlayer_ApplyPreCombatLogic
{
    @SpirePrefixPatch
    public static void Method(AbstractPlayer __instance)
    {
        CombatStats.OnStartup();
    }
}