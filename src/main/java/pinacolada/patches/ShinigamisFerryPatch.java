package pinacolada.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.WingBoots;
import pinacolada.relics.pcl.ShinigamisFerry;

public class ShinigamisFerryPatch
{

    @SpirePatch(
            clz = MapRoomNode.class,
            method = "isConnectedTo"
    )
    public static class IsConnectedTo
    {
        public static boolean Postfix(boolean __result, MapRoomNode __instance, MapRoomNode node)
        {
            if (AbstractDungeon.player.hasRelic(ShinigamisFerry.ID))
            {
                AbstractRelic ferry = AbstractDungeon.player.getRelic(ShinigamisFerry.ID);
                if (!__result && ferry.counter > 0)
                {
                    for (MapEdge edge : __instance.getEdges())
                    {
                        if (node.y == edge.dstY)
                        {
                            return true;
                        }
                    }
                }
            }
            return __result;
        }
    }

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.map.MapRoomNode",
            method = "playNodeSelectedSound"
    )
    public static class NodeSelected
    {
        public static void Postfix(MapRoomNode __instance)
        {
            if (Settings.isDebug)
            {
                return;
            }

            boolean normalConnection = isNormalConnectedTo(AbstractDungeon.getCurrMapNode(), __instance);
            boolean specialConnection = AbstractDungeon.getCurrMapNode().isConnectedTo(__instance);
            //Try to make Winged Boots and Flight have priority
            if (!normalConnection && specialConnection)
            {
                if (AbstractDungeon.player.hasRelic(WingBoots.ID))
                {
                    if (AbstractDungeon.player.getRelic(WingBoots.ID).counter > 0)
                    {
                        --AbstractDungeon.player.getRelic(WingBoots.ID).counter;
                        if (AbstractDungeon.player.getRelic(WingBoots.ID).counter <= 0)
                        {
                            AbstractDungeon.player.getRelic(WingBoots.ID).setCounter(-2);
                        }
                        return;
                    }
                }
                if (ModHelper.isModEnabled("Flight"))
                {
                    return;
                }
                AbstractRelic ferry = AbstractDungeon.player.getRelic(ShinigamisFerry.ID);
                if (ferry != null)
                {
                    ferry.onTrigger();
                }
            }
        }

        private static boolean isNormalConnectedTo(MapRoomNode currNode, MapRoomNode node)
        {
            for (MapEdge edge : currNode.getEdges())
            {
                if (node.x == edge.dstX && node.y == edge.dstY)
                {
                    return true;
                }
            }
            return false;
        }
    }
}