package pinacolada.patches.actions;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import pinacolada.patches.cardLibrary.CardLibraryPatches;

public class MakeTempCardInHandActionPatches
{
    @SpirePatch(clz = MakeTempCardInHandAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, boolean.class})
    public static class MakeTempCardInHandActionPatches_Ctor1
    {
        @SpirePrefixPatch
        public static void Prefix(MakeTempCardInHandAction __instance, @ByRef AbstractCard[] card, boolean isOtherCardInCenter)
        {
            CardLibraryPatches.TryReplace(card);
        }
    }

    @SpirePatch(clz = MakeTempCardInHandAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, int.class})
    public static class MakeTempCardInHandActionPatches_Ctor2
    {
        @SpirePrefixPatch
        public static void Prefix(MakeTempCardInHandAction __instance, @ByRef AbstractCard[] card, int amount)
        {
            CardLibraryPatches.TryReplace(card);
        }
    }
}
