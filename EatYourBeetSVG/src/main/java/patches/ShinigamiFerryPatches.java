package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.relics.animator.ShinigamiFerry;
import eatyourbeets.utilities.GameUtilities;

public class ShinigamiFerryPatches
{
    @SpirePatch(clz = MapRoomNode.class, method = "isConnectedTo", paramtypez = {MapRoomNode.class})
    public static class IsConnectedTo
    {
        public static boolean Postfix(boolean __result, MapRoomNode __instance, MapRoomNode node)
        {
            final AbstractRelic relic = GameUtilities.GetRelic(ShinigamiFerry.ID);
            if (!__result && relic != null && relic.counter > 0)
            {
                for (MapEdge edge : __instance.getEdges())
                {
                    if (node.y == edge.dstY)
                    {
                        return true;
                    }
                }
            }

            return __result;
        }
    }

//    @SpirePatch(clz = MapRoomNode.class, method = "playNodeSelectedSound")
//    public static class NodeSelected
//    {
//        @SpirePostfixPatch
//        public static void Postfix(MapRoomNode __instance)
//        {
//            if (Settings.isDebug)
//            {
//                return;
//            }
//
//            final boolean normalConnection = IsNormalConnectedTo(AbstractDungeon.getCurrMapNode(), __instance);
//            final boolean specialConnection = AbstractDungeon.getCurrMapNode().isConnectedTo(__instance);
//
//            //Try to make Winged Boots and Flight have priority
//            if (!normalConnection && specialConnection)
//            {
//                final AbstractRelic boots = GameUtilities.GetRelic(ShinigamiFerry.ID);
//                if (boots != null && boots.counter > 0)
//                {
//                    if (boots.counter > 1)
//                    {
//                        boots.counter -= 1;
//                    }
//                    else
//                    {
//                        boots.setCounter(-2);
//                    }
//                    return;
//                }
//
//                if (ModHelper.isModEnabled(Flight.ID))
//                {
//                    return;
//                }
//
//                final AbstractRelic relic = GameUtilities.GetRelic(ShinigamiFerry.ID);
//                if (relic != null)
//                {
//                    relic.onTrigger();
//                }
//            }
//        }
//
//        private static boolean IsNormalConnectedTo(MapRoomNode node, MapRoomNode other)
//        {
//            for (MapEdge edge : node.getEdges())
//            {
//                if (other.x == edge.dstX && other.y == edge.dstY)
//                {
//                    return true;
//                }
//            }
//
//            return false;
//        }
//    }
}