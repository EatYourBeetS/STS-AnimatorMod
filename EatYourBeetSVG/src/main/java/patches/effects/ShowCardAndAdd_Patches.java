package patches.effects;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;

public class ShowCardAndAdd_Patches
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
}
