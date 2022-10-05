package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.shop.StoreRelic;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.utilities.JUtils;

@SpirePatch(clz= StoreRelic.class, method = "update", paramtypez = {float.class})
public class StoreRelicPatches
{
    @SpirePostfixPatch
    public static void Method(StoreRelic __instance, float rugY)
    {
        if (__instance.isPurchased)
        {
            return;
        }

        final EYBRelic relic = JUtils.SafeCast(__instance.relic, EYBRelic.class);
        if (relic != null && relic.hb != null && relic.hb.hovered)
        {
            relic.OnRelicHovering(__instance);
        }
    }
}

//TODO: Implement this
//@SpirePatch(clz= StoreRelic.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractRelic.class, int.class, ShopScreen.class})
//public class StoreRelicPatches
//{
//    @SpirePostfixPatch
//    public static void Method(StoreRelic __instance, AbstractRelic relic, int slot, ShopScreen screen)
//    {
//
//        if (AbstractDungeon.player.chosenClass == Enums.Characters.THE_ANIMATOR &&
//            AbstractDungeon.floorNum >= 16 && AbstractDungeon.floorNum <= 36 &&
//            AbstractDungeon.merchantRng.randomBoolean() && !DevConsole.visible)
//        {
//            int seen = 0;
//            for (AnimatorCard_UltraRare card : AnimatorCard_UltraRare.GetCards().values())
//            {
//                if (card.isSeen)
//                {
//                    seen += 1;
//                }
//            }
//
//            if (seen >= 5 && AnimatorCustomLoadout.specialTrophies.Trophy1 > 0)
//            {
//                relic = new Destiny();
//                __instance.relic = relic;
//                __instance.price = relic.getPrice();
//            }
//        }
//    }
//}