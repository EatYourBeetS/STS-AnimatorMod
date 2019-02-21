package patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.ui.CardRewardScreenPatch;

@SpirePatch(clz= CardRewardScreen.class, method="render")
public class CardRewardScreenPatch_Render
{
    @SpirePrefixPatch
    public static void Prefix(CardRewardScreen __instance, SpriteBatch sb)
    {
        CardRewardScreenPatch.PreRender(__instance, sb);
    }

    @SpirePostfixPatch
    public static void Postfix(CardRewardScreen __instance, SpriteBatch sb)
    {
        CardRewardScreenPatch.PostRender(__instance, sb);
    }
}