package patches.abstractDungeon;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.resources.GR;

@SpirePatch(clz = AbstractDungeon.class, method = "closeCurrentScreen")
public class AbstractDungeon_CloseCurrentScreen
{
    @SpirePrefixPatch
    public static void Method()
    {
        if (AbstractDungeon.screen == GR.Enums.Screens.EYB_SCREEN)
        {
            GR.Screens.Dispose();
        }
    }
}