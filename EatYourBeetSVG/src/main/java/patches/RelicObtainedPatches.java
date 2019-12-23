package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.metrics.MetricData;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.select.BossRelicSelectScreen;
import eatyourbeets.interfaces.OnRelicObtainedSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.relics.UnnamedReign.AncientMedallion;
import eatyourbeets.relics.UnnamedReign.UnnamedReignRelic;

public class RelicObtainedPatches
{
    private static void OnRelicObtain(AbstractRelic relic, OnRelicObtainedSubscriber.Trigger trigger)
    {
        PlayerStatistics.OnRelicObtained(relic, trigger);
        //Gilgamesh.OnRelicReceived(relic, trigger);
        //ExquisiteBloodVial.OnRelicReceived(relic, trigger);
        //AncientMedallion.OnRelicObtained(relic, trigger);
        UnnamedReignRelic.OnRelicObtained(relic, trigger);
    }

    @SpirePatch(clz = AbstractRelic.class, method = "onEquip")
    public static class AbstractRelic_OnEquip
    {
        @SpirePostfixPatch
        public static void Postfix(AbstractRelic relic)
        {
            OnRelicObtain(relic, OnRelicObtainedSubscriber.Trigger.Equip);
        }
    }

    @SpirePatch(clz= BossRelicSelectScreen.class, method="relicObtainLogic", paramtypez = {AbstractRelic.class})
    public static class BossRelicSelectScreenPatch
    {
        @SpirePrefixPatch
        public static void Prefix(BossRelicSelectScreen __instance, AbstractRelic relic)
        {
            OnRelicObtain(relic, OnRelicObtainedSubscriber.Trigger.Obtain);
        }
    }

    @SpirePatch(clz = AbstractRelic.class, method = "obtain")
    public static class AbstractRelic_Obtain
    {
        @SpirePostfixPatch
        public static void Postfix(AbstractRelic relic)
        {
            OnRelicObtain(relic, OnRelicObtainedSubscriber.Trigger.Obtain);
        }
    }

    @SpirePatch(clz= MetricData.class, method="addRelicObtainData", paramtypez = {AbstractRelic.class})
    public static class MetricData_AddRelicObtainData
    {
        @SpirePrefixPatch
        public static void Prefix(MetricData __instance, AbstractRelic relic)
        {
            OnRelicObtain(relic, OnRelicObtainedSubscriber.Trigger.MetricData);
        }
    }
}