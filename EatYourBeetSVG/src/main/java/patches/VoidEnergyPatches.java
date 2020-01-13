package patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.ui.screens.unnamed.VoidEnergyOrb;

public class VoidEnergyPatches
{
    protected static VoidEnergyOrb orb = null;

    public static void SetOrb(VoidEnergyOrb orb)
    {
        VoidEnergyPatches.orb = orb;
    }

    @SpirePatch(clz = EnergyPanel.class, method = "update")
    public static class EnergyPanelPatches_Update
    {
        @SpirePostfixPatch
        public static void Method(EnergyPanel __instance)
        {
            if (orb != null)
            {
                orb.update(__instance);
            }
        }
    }

    @SpirePatch(clz = EnergyPanel.class, method = "render")
    public static class EnergyPanelPatches_Render
    {
        @SpirePostfixPatch
        public static void Method(EnergyPanel __instance, SpriteBatch sb)
        {
            if (orb != null)
            {
                orb.render(__instance, sb);
            }
        }
    }
}
