package pinacolada.patches.animator;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import pinacolada.patches.cardLibrary.PCLCardLibraryPatches;

public class AnimatorCardLibraryPatches
{
    @SpirePatch(clz = patches.cardLibrary.CardLibraryPatches.class, method = "TryReplace")
    public static class AnimatorCardLibraryPatches_TryReplace
    {
        @SpirePostfixPatch
        public static void Postfix(AbstractCard[] srcCard)
        {
            PCLCardLibraryPatches.TryReplace(srcCard);
        }
    }
}
