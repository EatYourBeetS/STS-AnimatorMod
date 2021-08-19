package patches.actions;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import patches.cardLibrary.CardLibraryPatches;

public class MakeTempCardInDrawPileActionPatches
{
    @SpirePatch(clz = MakeTempCardInDrawPileAction.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {AbstractCard.class, int.class, boolean.class, boolean.class, boolean.class, float.class, float.class})
    public static class MakeTempCardInDrawPileActionPatches_Ctor
    {
        @SpirePrefixPatch
        public static void Prefix(MakeTempCardInDrawPileAction __instance, @ByRef AbstractCard[] card, int amount, boolean randomSpot, boolean autoPosition, boolean toBottom, float cardX, float cardY)
        {
            CardLibraryPatches.TryReplace(card);
        }
    }
}
