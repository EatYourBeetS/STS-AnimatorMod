package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StoreRelic;

@SpirePatch(clz= StoreRelic.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractRelic.class, int.class, ShopScreen.class})
public class StoreRelicPatch
{
    // Not ideal solution, but should work since the shop only has 3 relics at a time
    public static AbstractRelic[] last20Relics = new AbstractRelic[20];

    private static int index = 0;

    @SpirePrefixPatch
    public static void Method(StoreRelic __instance, AbstractRelic relic, int slot, ShopScreen screen)
    {
        last20Relics[index] = relic;
        index += 1;
        if (index >= last20Relics.length)
        {
            index = 0;
        }
    }
}