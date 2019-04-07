package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.metrics.MetricData;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.cards.animator.Gilgamesh;
import eatyourbeets.relics.ExquisiteBloodVial;

public class RelicObtainedPatches
{
    @SpirePatch(clz = AbstractRelic.class, method = "obtain")
    public static class AbstractRelic_Obtain
    {
        @SpirePostfixPatch
        public static void Postfix(AbstractRelic relic)
        {
            Gilgamesh.OnRelicReceived();
            ExquisiteBloodVial.OnRelicReceived(relic);
        }
    }

    @SpirePatch(clz= MetricData.class, method="addRelicObtainData", paramtypez = {AbstractRelic.class})
    public static class MetricData_AddRelicObtainData
    {
        @SpirePrefixPatch
        public static void Prefix(MetricData __instance, AbstractRelic relic)
        {
            Gilgamesh.OnRelicReceived();
            ExquisiteBloodVial.OnRelicReceived(relic);
        }
    }
}