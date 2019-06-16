package eatyourbeets.dungeons;

import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.*;

import java.util.ArrayList;

public class TheUnnamedReign_Map
{
    private static ArrayList<ArrayList<MapRoomNode>> Map;

    public static void GenerateMap(ArrayList<ArrayList<MapRoomNode>> map)
    {
        Map = map;

        MapRoomNode Mo1;
        MapRoomNode Mo2;
        MapRoomNode Ev1;
        MapRoomNode Ev2;
        MapRoomNode Sh1;
        MapRoomNode Re1;
        ArrayList<MapRoomNode> row;

        int y = 0;
        int left = 2;
        int center = 3;
        int right = 4;

        // M
        // |
        // M
        {
            Mo1 = CreateMonsterRoom(center, y);
            row = CreateRow(y);
            row.set(center, Mo1);
            y += 1;

            Mo2 = CreateMonsterRoom(center, y);
            row = CreateRow(y);
            row.set(center, Mo2);
            y += 1;

            ConnectRooms(Mo1, Mo2);
        }

        // E  E
        // |  |
        // S  R
        {
            Ev1 = CreateTreasureRoom(center, y);
            row = CreateRow(y);
            row.set(center, Ev1);
            y += 1;

            ConnectRooms(Mo2, Ev1);

            Ev2 = CreateEventRoom(center, y);
            row = CreateRow(y);
            row.set(center, Ev2);
            y += 1;

            ConnectRooms(Ev1, Ev2);

            Sh1 = CreateShopRoom(left, y);
            Re1 = CreateRestRoom(right, y);
            row = CreateRow(y);
            row.set(left, Sh1);
            row.set(right, Re1);
            y += 1;

            ConnectRooms(Ev2, Sh1);
            ConnectRooms(Ev2, Re1);
        }

        // M
        // |
        // M
        {
            Mo1 = CreateMonsterRoom(center, y);
            row = CreateRow(y);
            row.set(center, Mo1);
            y += 1;

            Mo2 = CreateMonsterRoom(center, y);
            row = CreateRow(y);
            row.set(center, Mo2);
            y += 1;

            ConnectRooms(Sh1, Mo1);
            ConnectRooms(Re1, Mo1);
            ConnectRooms(Mo1, Mo2);
        }

        // E1  E2
        // |  |
        // S1  R1
        {
            Ev1 = CreateEventRoom(center, y);
            row = CreateRow(y);
            row.set(center, Ev1);
            y += 1;

            ConnectRooms(Mo2, Ev1);

            Re1 = CreateRestRoom(left, y);
            Sh1 = CreateShopRoom(right, y);
            row = CreateRow(y);
            row.set(left, Sh1);
            row.set(right, Re1);
            y += 1;

            ConnectRooms(Ev1, Re1);
            ConnectRooms(Ev1, Sh1);
        }

        // M
        // |
        // M
        {
            Mo1 = CreateMonsterRoomElite(center, y);
            row = CreateRow(y);
            row.set(center, Mo1);
            y += 1;

            Mo2 = CreateTreasureRoom(center, y);
            row = CreateRow(y);
            row.set(center, Mo2);
            y += 1;

            ConnectRooms(Sh1, Mo1);
            ConnectRooms(Re1, Mo1);
            ConnectRooms(Mo1, Mo2);
        }

        // E  E
        // |  |
        // S  R
        {
            Ev1 = CreateEventRoom(center, y);
            row = CreateRow(y);
            row.set(center, Ev1);
            y += 1;

            ConnectRooms(Mo2, Ev1);

            Sh1 = CreateShopRoom(left, y);
            Re1 = CreateRestRoom(right, y);
            row = CreateRow(y);
            row.set(left, Sh1);
            row.set(right, Re1);
            y += 1;

            ConnectRooms(Ev1, Sh1);
            ConnectRooms(Ev1, Re1);
        }

        // M
        // |
        // M
        {
            Ev1 = CreateMonsterRoomElite(center, y);
            row = CreateRow(y);
            row.set(center, Ev1);
            y += 1;

            Ev2 = CreateRestRoom(center, y);
            row = CreateRow(y);
            row.set(center, Ev2);
            y += 1;

            Mo1 = CreateMonsterRoomBoss(center, y);
            row = CreateRow(y);
            row.set(center, Mo1);
            //y += 1;

            ConnectRooms(Sh1, Ev1);
            ConnectRooms(Re1, Ev1);
            ConnectRooms(Ev1, Ev2);
            ConnectRooms(Ev2, Mo1);
        }
    }


    private static ArrayList<MapRoomNode> CreateRow(int y)
    {
        ArrayList<MapRoomNode> row = new ArrayList<>();

        row.add(new MapRoomNode(0, y));
        row.add(new MapRoomNode(1, y));
        row.add(new MapRoomNode(2, y));
        row.add(new MapRoomNode(3, y));
        row.add(new MapRoomNode(4, y));
        row.add(new MapRoomNode(5, y));
        row.add(new MapRoomNode(6, y));

        Map.add(row);
        return row;
    }

    private static MapRoomNode CreateTreasureRoom(int x, int y)
    {
        MapRoomNode node = new MapRoomNode(x, y);
        node.room = new TreasureRoom();
        return node;
    }

    private static MapRoomNode CreateMonsterRoomBoss(int x, int y)
    {
        MapRoomNode node = new MapRoomNode(x, y);
        node.room = new MonsterRoomBoss();
        return node;
    }

    private static MapRoomNode CreateEventRoom(int x, int y)
    {
        MapRoomNode node = new MapRoomNode(x, y);
        node.room = new EventRoom();
        return node;
    }

    private static MapRoomNode CreateMonsterRoomElite(int x, int y)
    {
        MapRoomNode node = new MapRoomNode(x, y);
        node.room = new MonsterRoomElite();
        return node;
    }

    private static MapRoomNode CreateMonsterRoom(int x, int y)
    {
        MapRoomNode node = new MapRoomNode(x, y);
        node.room = new MonsterRoom();
        return node;
    }

    private static MapRoomNode CreateShopRoom(int x, int y)
    {
        MapRoomNode node = new MapRoomNode(x, y);
        node.room = new ShopRoom();
        return node;
    }

    private static MapRoomNode CreateRestRoom(int x, int y)
    {
        MapRoomNode node = new MapRoomNode(x, y);
        node.room = new RestRoom();
        return node;
    }

    private static void ConnectRooms(MapRoomNode src, MapRoomNode dst)
    {
        src.addEdge(new MapEdge(src.x, src.y, src.offsetX, src.offsetY, dst.x, dst.y, dst.offsetX, dst.offsetY, false));
    }

    private static void ConnectRooms(MapRoomNode src, MapRoomNode dst, boolean isBoss)
    {
        src.addEdge(new MapEdge(src.x, src.y, src.offsetX, src.offsetY, dst.x, dst.y, dst.offsetX, dst.offsetY, isBoss));
    }
}
