package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.select.BossRelicSelectScreen;
import eatyourbeets.cards.animator.Gilgamesh;

@SpirePatch(clz= BossRelicSelectScreen.class, method="relicObtainLogic", paramtypez = {AbstractRelic.class})
public class BossRelicSelectScreenPatch
{
    @SpirePrefixPatch
    public static void Prefix(BossRelicSelectScreen __instance, AbstractRelic r)
    {
        Gilgamesh.OnRelicReceived(r);
    }
}