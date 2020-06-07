package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.WingBoots;
import eatyourbeets.relics.animator.beta.ShinigamisFerry;

import java.util.Iterator;

public class ShinigamisFerryPatch
{
    private static MapRoomNode getNode(int x, int y)
    {
        try {
            return CardCrawlGame.dungeon.getMap().get(y).get(x);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @SpirePatch(
            clz= MapRoomNode.class,
            method="isConnectedTo"
    )
    public static class IsConnectedTo
    {

        public static boolean Postfix(boolean __result, MapRoomNode __instance, MapRoomNode node)
        {
            if (AbstractDungeon.player.hasRelic(ShinigamisFerry.ID)) {
                AbstractRelic ferry = AbstractDungeon.player.getRelic(ShinigamisFerry.ID);
                if (!__result && ferry.counter > 0) {
                    Iterator var2 = __instance.getEdges().iterator();
                    MapEdge edge;
                    do {
                        if (!var2.hasNext()) {
                            return false;
                        }

                        edge = (MapEdge)var2.next();
                        if (node.y == edge.dstY) {
                            return true;
                        }
                    } while(node.y < edge.dstY || !AbstractDungeon.player.hasRelic(ShinigamisFerry.ID));
                }
            }
            return __result;
        }

        static int getNodeDistance(MapRoomNode start, MapRoomNode end)
        {
            return getNodeDistance(start, end, 1);
        }

        private static int getNodeDistance(MapRoomNode start, MapRoomNode end, int depth)
        {
            if (start == null) {
                return -1;
            }

            for (MapEdge edge : start.getEdges()) {
                MapRoomNode nextNode = getNode(edge.dstX, edge.dstY);
                if (nextNode != null && nextNode.y == end.y) {
                    return depth;
                }
                int dist = getNodeDistance(nextNode, end, depth+1);
                if (dist != -1) {
                    return dist;
                }
            }
            return -1;
        }
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.map.MapRoomNode",
            method="playNodeSelectedSound"
    )
    public static class NodeSelected
    {
        public static void Postfix(MapRoomNode __instance)
        {
            if (Settings.isDebug) {
                return;
            }

            int distance = IsConnectedTo.getNodeDistance(AbstractDungeon.getCurrMapNode(), __instance);
            boolean normalConnection = isNormalConnectedTo(AbstractDungeon.getCurrMapNode(), __instance);
            boolean specialConnection = AbstractDungeon.getCurrMapNode().isConnectedTo(__instance);
            //Try to make Winged Boots and Flight have priority
            if (distance == 1 && (!normalConnection && specialConnection)) {
                if (AbstractDungeon.player.hasRelic(WingBoots.ID)) {
                    if (AbstractDungeon.player.getRelic(WingBoots.ID).counter > 0) {
                        --AbstractDungeon.player.getRelic(WingBoots.ID).counter;
                        if (AbstractDungeon.player.getRelic(WingBoots.ID).counter <= 0) {
                            AbstractDungeon.player.getRelic(WingBoots.ID).setCounter(-2);
                        }
                        return;
                    }
                }
                if (ModHelper.isModEnabled("Flight")) {
                    return;
                }
            }
            if (!normalConnection && specialConnection) {
                AbstractRelic ferry = AbstractDungeon.player.getRelic(ShinigamisFerry.ID);
                if (ferry != null) {
                    ferry.onTrigger();
                }
            }
        }

        private static boolean isNormalConnectedTo(MapRoomNode currNode, MapRoomNode node) {
            Iterator var2 = currNode.getEdges().iterator();

            MapEdge edge;
            do {
                if (!var2.hasNext()) {
                    return false;
                }

                edge = (MapEdge)var2.next();
            } while(node.x != edge.dstX || node.y != edge.dstY);

            return true;
        }
    }
}