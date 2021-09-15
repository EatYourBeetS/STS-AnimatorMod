package patches.abstractDailyMod;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.daily.mods.AbstractDailyMod;
import eatyourbeets.dailymods.AnimatorDailyMod;

public class AbstractDailyModPatches {

    @SpirePatch(clz = AbstractDailyMod.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {String.class, String.class, String.class, String.class, boolean.class, AbstractPlayer.PlayerClass.class})
    public static class AbstractDailyMod_Constructor
    {
        @SpirePrefixPatch
        public static void Prefix(AbstractDailyMod __instance)
        {
            if (__instance instanceof AnimatorDailyMod)
            {
                //Add image
            }
        }
    }
}
