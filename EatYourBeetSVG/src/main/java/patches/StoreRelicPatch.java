package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StoreRelic;

@SpirePatch(clz= StoreRelic.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractRelic.class, int.class, ShopScreen.class})
public class StoreRelicPatch
{
    // Not ideal solution, but should work since the shop only has 3 relics at a time
    public static final AbstractRelic[] last20Relics = new AbstractRelic[20];

    private static int index = 0;

    @SpirePostfixPatch
    public static void Method(StoreRelic __instance, AbstractRelic relic, int slot, ShopScreen screen)
    {
//        TODO: Implement this
//        if (AbstractDungeon.player.chosenClass == AbstractEnums.Characters.THE_ANIMATOR &&
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
//            if (seen >= 5 && AnimatorCustomLoadout.specialTrophies.trophy1 > 0)
//            {
//                relic = new Destiny();
//                __instance.relic = relic;
//                __instance.price = relic.getPrice();
//            }
//        }

        last20Relics[index] = relic;
        index += 1;
        if (index >= last20Relics.length)
        {
            index = 0;
        }
    }
}