package patches.topPanel;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

@SpirePatch(clz= TopPanel.class, method="render", paramtypez = {SpriteBatch.class})
public class TopPanelPatches_Render
{
    @SpirePostfixPatch
    public static void Method(TopPanel __instance, SpriteBatch sb)
    {
        // TODO: Update special button
    }
}