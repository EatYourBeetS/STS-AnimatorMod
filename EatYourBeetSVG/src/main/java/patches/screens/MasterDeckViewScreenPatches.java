package patches.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.MasterDeckViewScreen;
import eatyourbeets.ui.animator.cardReward.AnimatorCardRewardAffinities;
import eatyourbeets.ui.animator.cardReward.AnimatorCardRewardScreen;

public class MasterDeckViewScreenPatches
{
    private static final AnimatorCardRewardAffinities screen = AnimatorCardRewardScreen.Instance.cardAffinities;

    @SpirePatch(clz= MasterDeckViewScreen.class, method="update")
    public static class MasterDeckViewScreen_Update
    {
        @SpirePrefixPatch
        public static void Prefix(MasterDeckViewScreen __instance)
        {
            screen.Update();
        }
    }

    @SpirePatch(clz= MasterDeckViewScreen.class, method="render")
    public static class MasterDeckViewScreen_Render
    {
//        @SpirePrefixPatch
//        public static void Prefix(MasterDeckViewScreen __instance, SpriteBatch sb)
//        {
//            screen.PreRender(sb);
//        }

        @SpirePrefixPatch
        public static void Prefix(MasterDeckViewScreen __instance, SpriteBatch sb)
        {
            screen.Render(sb);
        }
    }

    @SpirePatch(clz= MasterDeckViewScreen.class, method="open")
    public static class MasterDeckViewScreen_Open
    {
        @SpirePrefixPatch
        public static void Prefix(MasterDeckViewScreen __instance)
        {
            screen.Open(AbstractDungeon.player.masterDeck.group);
        }
    }

//    @SpirePatch(clz= MasterDeckViewScreen.class, method="onClose")
//    public static class MasterDeckViewScreen_OnClose
//    {
//        @SpirePostfixPatch
//        public static void Postfix(MasterDeckViewScreen __instance)
//        {
//            screen.Close();
//        }
//    }
}
