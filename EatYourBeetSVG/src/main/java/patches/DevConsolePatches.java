package patches;

import basemod.DevConsole;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

public class DevConsolePatches
{
    @SpirePatch(clz = DevConsole.class, method = "execute")
    public static class AutocompletePatches_Execute
    {
        @SpirePrefixPatch
        public static void Prefix()
        {
            if (GameUtilities.InGame() && GameUtilities.IsEYBPlayerClass())
            {
                GR.Common.Dungeon.SetCheating();
            }
        }
    }
}
