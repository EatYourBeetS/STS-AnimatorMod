package patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.dungeons.TheUnnamedReign;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

public class AbstractRoomPatches
{
    @SpirePatch(clz = AbstractRoom.class, method = "update")
    public static class AbstractRoomUpdateIncrementElitesPatch
    {
        @SpireInsertPatch(locator = AbstractRoomUpdateIncrementElitesPatch.Locator.class)
        public static void Insert(AbstractRoom __instance)
        {
            if (CardCrawlGame.dungeon instanceof TheUnnamedReign)
            {
                CardCrawlGame.elites3Slain += 1;
            }
        }

        public static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(CardCrawlGame.class, "dungeon");
                return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher)[1]};
            }
        }
    }
}
