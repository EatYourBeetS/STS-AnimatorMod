package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.powers.CombatStats;
import javassist.CtBehavior;

public class EnergyManagerPatches
{
    @SpirePatch(clz = EnergyManager.class, method = "recharge")
    public static class EnergyManagerPatches_recharge
    {
        private static int previousEnergy;

        @SpirePrefixPatch
        public static void Prefix(EnergyManager __instance)
        {
            previousEnergy = EnergyPanel.getCurrentEnergy();
        }

        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(EnergyManager __instance)
        {
            final int currentEnergy = EnergyPanel.getCurrentEnergy();
            final int newEnergyCount = CombatStats.OnEnergyRecharge(previousEnergy, currentEnergy);
            if (newEnergyCount != currentEnergy)
            {
                EnergyPanel.setEnergy(newEnergyCount);
            }
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            final Matcher finalMatcher = new Matcher.MethodCallMatcher(EnergyPanel.class, "setEnergy");
            return new int[]{ LineFinder.findInOrder(ctMethodToPatch, finalMatcher)[0] + 1 };
        }
    }
}