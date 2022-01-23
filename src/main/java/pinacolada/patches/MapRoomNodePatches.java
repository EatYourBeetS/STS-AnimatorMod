package pinacolada.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import pinacolada.resources.GR;

public class MapRoomNodePatches
{

    @SpirePatch(
            clz = MapRoomNode.class,
            method = "isConnectedTo",
            paramtypez = {MapRoomNode.class}
    )
    public static class MapRoomNodePatches_IsConnectedTo {

        public static boolean Postfix(boolean __result, MapRoomNode __instance, MapRoomNode node) {
            if (GR.PCL.Dungeon.CanJumpNextFloor()) {
                for (MapEdge edge : __instance.getEdges()) {
                    if (node.y == edge.dstY || GR.PCL.Dungeon.CanJumpAnywhere()) {
                        return true;
                    }
                }
            }
            return __result;
        }
    }

    @SpirePatch(
            clz = MapRoomNode.class,
            method = "playNodeSelectedSound"
    )
    public static class MapRoomNodePatches_NodeSelected
    {
        public static void Postfix(MapRoomNode __instance)
        {
            GR.PCL.Dungeon.SetJumpNextFloor(false);
        }
    }
}