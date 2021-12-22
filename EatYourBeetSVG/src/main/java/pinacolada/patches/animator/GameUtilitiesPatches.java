package pinacolada.patches.animator;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.utilities.GameUtilities;
import pinacolada.resources.GR;

public class GameUtilitiesPatches
{
    @SpirePatch(clz = GameUtilities.class, method = "CanObtainCopy")
    public static class AnimatorCardLibraryPatches_TryReplace
    {
        @SpirePostfixPatch
        public static boolean Postfix(boolean __result, AbstractCard card)
        {
            return __result && GR.PCL.Dungeon.CanObtainCopy(card);
        }
    }
}
