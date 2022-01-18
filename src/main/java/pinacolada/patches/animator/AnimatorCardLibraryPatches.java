package pinacolada.patches.animator;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import eatyourbeets.blights.animator.UltimateWispBlight;
import pinacolada.patches.cardLibrary.PCLCardLibraryPatches;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

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

    @SpirePatch(clz = UltimateWispBlight.class, method = "OnShuffle", paramtypez = {boolean.class})
    public static class RelicLibraryPatches_GetRelic
    {
        @SpirePrefixPatch
        public static SpireReturn Prefix(UltimateWispBlight __instance, boolean triggerRelics)
        {
            // Card replacements may occur for non-Animator classes which will cause crashes when trying to generate a copy of the EYBCardData's card
            if (!PCLGameUtilities.IsPlayerClass(GR.Animator.PlayerClass)) {
                AbstractCard card = CardLibrary.getCopy(GR.PCL.Dungeon.IsUnnamedReign() ? VoidCard.ID : Burn.ID, 0, 0);
                PCLActions.Last.MakeCardInDrawPile(card);
                __instance.flash();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}
