package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.cards.animator.Gilgamesh;
import eatyourbeets.relics.ExquisiteBloodVial;
import eatyourbeets.relics.AncientMedallion;
import eatyourbeets.relics.UnnamedReignRelic;

public class RelicObtainedPatches
{
    @SpirePatch(clz = AbstractRelic.class, method = "onEquip")
    public static class AbstractRelic_OnEquip
    {
        @SpirePostfixPatch
        public static void Postfix(AbstractRelic relic)
        {
            Gilgamesh.OnRelicReceived(relic);
            ExquisiteBloodVial.OnRelicReceived(relic);
            AncientMedallion.OnRelicReceived(relic);
            UnnamedReignRelic.OnRelicReceived(relic);
        }
    }

//    @SpirePatch(clz= BossRelicSelectScreen.class, method="relicObtainLogic", paramtypez = {AbstractRelic.class})
//    public static class BossRelicSelectScreenPatch
//    {
//        @SpirePrefixPatch
//        public static void Prefix(BossRelicSelectScreen __instance, AbstractRelic r)
//        {
//            Gilgamesh.OnRelicReceived(r);
//        }
//    }
//
//    @SpirePatch(clz = AbstractRelic.class, method = "obtain")
//    public static class AbstractRelic_Obtain
//    {
//        @SpirePostfixPatch
//        public static void Postfix(AbstractRelic relic)
//        {
//            Gilgamesh.OnRelicReceived(relic);
//            ExquisiteBloodVial.OnRelicReceived(relic);
//            AncientMedallion.OnRelicReceived(relic);
//            UnnamedReignRelic.OnRelicReceived(relic);
//        }
//    }
//
//    @SpirePatch(clz= MetricData.class, method="addRelicObtainData", paramtypez = {AbstractRelic.class})
//    public static class MetricData_AddRelicObtainData
//    {
//        @SpirePrefixPatch
//        public static void Prefix(MetricData __instance, AbstractRelic relic)
//        {
//            Gilgamesh.OnRelicReceived(relic);
//            ExquisiteBloodVial.OnRelicReceived(relic);
//            AncientMedallion.OnRelicReceived(relic);
//            UnnamedReignRelic.OnRelicReceived(relic);
//        }
//    }
}