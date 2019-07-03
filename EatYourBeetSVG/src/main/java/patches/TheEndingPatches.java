package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import eatyourbeets.characters.AnimatorCustomLoadout;
import eatyourbeets.events.TheMaskedTraveler2;
import eatyourbeets.resources.Resources_Common;
import eatyourbeets.room.AnimatorCustomEventRoom;

public class TheEndingPatches
{
    @SpirePatch(clz = TheEnding.class, method = "generateSpecialMap")
    public static class TheEndingPatch
    {
        @SpirePostfixPatch
        public static void Postfix(TheEnding __instance)
        {
//            if (Settings.isStandardRun() && PlayerStatistics.GetActualAscensionLevel() >= 0)
//            {
                if (AnimatorCustomLoadout.specialTrophies.trophy1 > 0 ||
                        AbstractDungeon.player.chosenClass == AbstractEnums.Characters.THE_ANIMATOR)
                {
//                int currentLevel = UnlockTracker.getUnlockLevel(AbstractEnums.Characters.THE_ANIMATOR);
//                if (currentLevel <= 1)
//                {
//                }
                    MapRoomNode rest = __instance.getMap().get(0).get(3);
                    MapRoomNode shop = __instance.getMap().get(1).get(3);
                    MapRoomNode fight = __instance.getMap().get(1).get(4);
                    MapRoomNode node = __instance.getMap().get(1).get(5);

                    node.room = new AnimatorCustomEventRoom(TheMaskedTraveler2::new);
                    node.room.setMapImg(Resources_Common.Map_Act5Entrance, Resources_Common.Map_Act5EntranceOutline);

                    connectNode(rest, node);
                    connectNode(node, shop);
                }
 //           }
        }

        private static void connectNode(MapRoomNode src, MapRoomNode dst)
        {
            src.addEdge(new MapEdge(src.x, src.y, src.offsetX, src.offsetY, dst.x, dst.y, dst.offsetX, dst.offsetY, false));
        }
    }
}
