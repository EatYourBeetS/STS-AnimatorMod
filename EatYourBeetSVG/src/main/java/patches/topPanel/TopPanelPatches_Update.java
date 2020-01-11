package patches.topPanel;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

@SpirePatch(clz= TopPanel.class, method="update")
public class TopPanelPatches_Update
{
    @SpirePostfixPatch
    public static void Method(TopPanel __instance)
    {
        // TODO:
    }
}