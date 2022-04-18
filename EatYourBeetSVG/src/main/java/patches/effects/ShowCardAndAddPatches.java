package patches.effects;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;
import patches.cardLibrary.CardLibraryPatches;

public class ShowCardAndAddPatches
{
    @SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = "update")
    public static class ShowCardAndAddToDrawPileEffect_Update
    {
        private static final FieldInfo<AbstractCard> _card = JUtils.GetField("card", ShowCardAndAddToDrawPileEffect.class);

        @SpirePostfixPatch
        public static void Postfix(ShowCardAndAddToDrawPileEffect __instance)
        {
            if (__instance.isDone)
            {
                CombatStats.OnCardCreated(_card.Get(__instance), false);
            }
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = "update")
    public static class ShowCardAndAddToDiscardPileEffect_Update
    {
        private static final FieldInfo<AbstractCard> _card = JUtils.GetField("card", ShowCardAndAddToDiscardEffect.class);

        @SpirePostfixPatch
        public static void Postfix(ShowCardAndAddToDiscardEffect __instance)
        {
            if (__instance.isDone)
            {
                CombatStats.OnCardCreated(_card.Get(__instance), false);
            }
        }
    }

    @SpirePatch(clz = ShowCardAndAddToHandEffect.class, method = "update")
    public static class ShowCardAndAddToHandEffect_Update
    {
        private static final FieldInfo<AbstractCard> _card = JUtils.GetField("card", ShowCardAndAddToHandEffect.class);

        @SpirePostfixPatch
        public static void Postfix(ShowCardAndAddToHandEffect __instance)
        {
            if (__instance.isDone)
            {
                CombatStats.OnCardCreated(_card.Get(__instance), false);
            }
        }
    }

    // Constructors:

    @SpirePatch(clz = ShowCardAndObtainEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, float.class, float.class, boolean.class})
    public static class ShowCardAndObtainEffect_Ctor
    {
        @SpirePrefixPatch
        public static void Prefix(ShowCardAndObtainEffect __instance, @ByRef AbstractCard[] srcCard, float x, float y, boolean converge)
        {
            CardLibraryPatches.TryReplace(srcCard);
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, boolean.class, boolean.class})
    public static class ShowCardAndAddToDrawPileEffect_Ctor1
    {
        @SpirePrefixPatch
        public static void Prefix(ShowCardAndAddToDrawPileEffect __instance, @ByRef AbstractCard[] srcCard, boolean randomSpot, boolean toBottom)
        {
            CardLibraryPatches.TryReplace(srcCard);
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, float.class, float.class, boolean.class, boolean.class, boolean.class})
    public static class ShowCardAndAddToDrawPileEffect_Ctor2
    {
        @SpirePrefixPatch
        public static void Prefix(ShowCardAndAddToDrawPileEffect __instance, @ByRef AbstractCard[] srcCard, float x, float y, boolean randomSpot, boolean cardOffset, boolean toBottom)
        {
            CardLibraryPatches.TryReplace(srcCard);
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class})
    public static class ShowCardAndAddToDiscardEffect_Ctor1
    {
        @SpirePrefixPatch
        public static void Prefix(ShowCardAndAddToDiscardEffect __instance, @ByRef AbstractCard[] card)
        {
            CardLibraryPatches.TryReplace(card);
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, float.class, float.class})
    public static class ShowCardAndAddToDiscardEffect_Ctor2
    {
        @SpirePrefixPatch
        public static void Prefix(ShowCardAndAddToDiscardEffect __instance, @ByRef AbstractCard[] srcCard, float x, float y)
        {
            CardLibraryPatches.TryReplace(srcCard);
        }
    }

    @SpirePatch(clz = ShowCardAndAddToHandEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class})
    public static class ShowCardAndAddToHandEffect_Ctor1
    {
        @SpirePrefixPatch
        public static void Prefix(ShowCardAndAddToHandEffect __instance, @ByRef AbstractCard[] card)
        {
            CardLibraryPatches.TryReplace(card);
        }
    }

    @SpirePatch(clz = ShowCardAndAddToHandEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, float.class, float.class})
    public static class ShowCardAndAddToHandEffect_Ctor2
    {
        @SpirePrefixPatch
        public static void Prefix(ShowCardAndAddToHandEffect __instance, @ByRef AbstractCard[] card, float offsetX, float offsetY)
        {
            CardLibraryPatches.TryReplace(card);
        }
    }
}
