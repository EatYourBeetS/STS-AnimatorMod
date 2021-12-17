package pinacolada.patches.topPanel;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import pinacolada.resources.GR;

public class EnergyPanelPatches
{
    @SpirePatch(clz= EnergyPanel.class, method="render")
    public static class EnergyPanel_Render
    {
        @SpirePostfixPatch
        public static void Postfix(EnergyPanel __instance, SpriteBatch sb)
        {
            GR.UI.CombatScreen.TryRender(sb);
        }
    }
}