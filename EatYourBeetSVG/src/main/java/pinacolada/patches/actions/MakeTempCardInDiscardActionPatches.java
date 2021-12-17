package pinacolada.patches.actions;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import patches.cardLibrary.CardLibraryPatches;

public class MakeTempCardInDiscardActionPatches
{
    @SpirePatch(clz = MakeTempCardInDiscardAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, int.class})
    public static class MakeTempCardInDiscardActionPatches_Ctor
    {
        @SpirePrefixPatch
        public static void Prefix(MakeTempCardInDiscardAction __instance, @ByRef AbstractCard[] card, int amount)
        {
            CardLibraryPatches.TryReplace(card);
        }
    }
}
