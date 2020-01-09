package eatyourbeets.dungeons;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EmptyRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.monsters.UnnamedReign.UnnamedEnemyGroup;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.resources.GR;
import eatyourbeets.scenes.TheUnnamedReignScene;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class TheUnnamedReign extends AbstractDungeon
{
    public static final String NAME = GR.Common.Text.TheUnnamedReign.Name;
    public static final String ID = GR.Common.CreateID("TheUnnamedReign");

    protected ArrayList<String> MONSTER_LIST_WHICH_ACTUALLY_WORKS;

    public static void EnterDungeon()
    {
        AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;

        CardCrawlGame.nextDungeon = TheUnnamedReign.ID;
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;

        GenericEventDialog.hide();
        CardCrawlGame.music.fadeAll();
        AbstractDungeon.fadeOut();
        AbstractDungeon.isDungeonBeaten = true;
    }

    public TheUnnamedReign(AbstractPlayer p, ArrayList<String> specialOneTimeEventList)
    {
        super(NAME, ID, p, new ArrayList<>());

        if (scene != null)
        {
            scene.dispose();
        }

        AbstractDungeon.actNum = 4;

        scene = new TheUnnamedReignScene();
        fadeColor = Color.valueOf("140a1eff");
        this.initializeLevelSpecificChances();
        mapRng = new Random(Settings.seed + (long) (AbstractDungeon.actNum * 200));

        GenerateMap();

        AbstractDungeon.currMapNode = new MapRoomNode(0, -1);
        AbstractDungeon.currMapNode.room = new EmptyRoom();

        CardCrawlGame.music.changeBGM(id);
    }

    public TheUnnamedReign(AbstractPlayer p, SaveFile saveFile)
    {
        super(NAME, p, saveFile);
        CardCrawlGame.dungeon = this;
        if (scene != null)
        {
            scene.dispose();
        }

        AbstractDungeon.actNum = 4;

        scene = new TheUnnamedReignScene();
        fadeColor = Color.valueOf("140a1eff");
        this.initializeLevelSpecificChances();
        miscRng = new Random(Settings.seed + (long) saveFile.floor_num);
        CardCrawlGame.music.changeBGM(id);
        mapRng = new Random(Settings.seed + (long) (saveFile.act_num * 200));

        GenerateMap();

//        MapRoomNode victoryNode = new MapRoomNode(3, map.size());
//        victoryNode.room = new TrueVictoryRoom();

        firstRoomChosen = true;
        this.populatePathTaken(saveFile);
    }

    protected void GenerateMap()
    {
        AbstractDungeon.map = new ArrayList<>();
        TheUnnamedReign_Map.GenerateMap(AbstractDungeon.map);

        GenerateMonstersInADecentWay();

        generateMonsters();

        firstRoomChosen = false;
        fadeIn();
    }

    @SpireOverride
    protected void setBoss(String key)
    {
        bossKey = key;

        if (key.equals(TheUnnamed.ID))
        {
            DungeonMap.boss = ImageMaster.loadImage("images/ui/map/boss/Animator_TheUnnamed.png");
            DungeonMap.bossOutline = ImageMaster.loadImage("images/ui/map/bossOutline/Animator_TheUnnamed.png");
        }
        else
        {
            SpireSuper.call(key);
        }
    }

    protected void initializeLevelSpecificChances()
    {
        PlayerStatistics.SaveData.EnteredUnnamedReign = true;

        shopRoomChance = 0.12F;
        restRoomChance = 0.10F;
        treasureRoomChance = 0.0F;
        eventRoomChance = 0.15F;
        eliteRoomChance = 0.12F;
        smallChestChance = 0;
        mediumChestChance = 0;
        largeChestChance = 100;
        commonRelicChance = 100;
        uncommonRelicChance = 0;
        rareRelicChance = 0;
        colorlessRareChance = 0.3F;
        if (AbstractDungeon.ascensionLevel >= 12)
        {
            cardUpgradedChance = 0.15F;
        }
        else
        {
            cardUpgradedChance = 0.3F;
        }
    }

    @Override
    public MonsterGroup getEliteMonsterForRoomCreation()
    {
        lastCombatMetricKey = MONSTER_LIST_WHICH_ACTUALLY_WORKS.get(getCurrMapNode().y);
        return MonsterHelper.getEncounter(lastCombatMetricKey);
    }

    @Override
    public MonsterGroup getMonsterForRoomCreation()
    {
        lastCombatMetricKey = MONSTER_LIST_WHICH_ACTUALLY_WORKS.get(getCurrMapNode().y);
        return MonsterHelper.getEncounter(lastCombatMetricKey);
    }

    protected ArrayList<String> generateExclusions()
    {
        return new ArrayList<>();
    }

    protected void initializeBoss()
    {
        bossList.clear();
        bossList.add(UnnamedEnemyGroup.THE_UNNAMED);
        bossList.add(UnnamedEnemyGroup.THE_UNNAMED);
        bossList.add(UnnamedEnemyGroup.THE_UNNAMED);
    }

    protected void initializeEventList()
    {
//        eventList.add("MindBloom");
//        eventList.add("The Moai Head");
//        eventList.add("Mysterious Sphere");
//        eventList.add("Tomb of Lord Red Mask");
        eventList.add("SensoryStone");
//        eventList.add("Winding Halls");
    }

    protected void initializeEventImg()
    {
        if (eventBackgroundImg != null)
        {
            eventBackgroundImg.dispose();
            eventBackgroundImg = null;
        }

        eventBackgroundImg = ImageMaster.loadImage("images/ui/event/panel.png");
    }

    protected void initializeShrineList()
    {
//        shrineList.add("Match and Keep!");
//        //shrineList.add("Wheel of Change");
//        shrineList.add("Golden Shrine");
//        shrineList.add("Transmorgrifier");
//        shrineList.add("Purifier");
//        shrineList.add("Upgrade Shrine");
    }

    protected void GenerateMonstersInADecentWay()
    {
        MONSTER_LIST_WHICH_ACTUALLY_WORKS = new ArrayList<>();

        RandomizedList<String> weakEnemies = new RandomizedList<>();
        weakEnemies.Add(UnnamedEnemyGroup.CULTIST);
        weakEnemies.Add(UnnamedEnemyGroup.UNNAMED_HAT);
        weakEnemies.Add(UnnamedEnemyGroup.THREE_NORMAL_SHAPES);

        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(0, weakEnemies.Retrieve(mapRng)); // mo1
        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(1, weakEnemies.Retrieve(mapRng)); // mo2
        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(2, weakEnemies.Retrieve(mapRng)); // th
        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(3, null);                 // ev1
        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(4, null);                 // r1/sh1

        RandomizedList<String> normalEnemies = new RandomizedList<>();
        normalEnemies.Add(UnnamedEnemyGroup.CULTIST_AND_TWO_SHAPES);
        normalEnemies.Add(UnnamedEnemyGroup.THREE_LAGAVULIN);
        int n = (mapRng.random(2));
        if (n == 0)
        {
            normalEnemies.Add(UnnamedEnemyGroup.LARGE_CUBE);
        }
        else if (n == 1)
        {
            normalEnemies.Add(UnnamedEnemyGroup.LARGE_CRYSTAL);
        }
        else
        {
            normalEnemies.Add(UnnamedEnemyGroup.LARGE_WISP);
        }

        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(5, normalEnemies.Retrieve(mapRng)); // mo1
        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(6, normalEnemies.Retrieve(mapRng)); // mo2
        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(7, normalEnemies.Retrieve(mapRng)); // ev1
        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(8, null);                   // r1/sh1

        RandomizedList<String> eliteEnemies = new RandomizedList<>();
        eliteEnemies.Add(UnnamedEnemyGroup.ULTIMATE_CRYSTAL);
        eliteEnemies.Add(UnnamedEnemyGroup.ULTIMATE_CUBE);
        eliteEnemies.Add(UnnamedEnemyGroup.ULTIMATE_WISP);

        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(9, eliteEnemies.Retrieve(mapRng));  // mo1
        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(10, null);  // tr1
        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(11, null); // ev1
        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(12, null); // r1/sh1

        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(13, eliteEnemies.Retrieve(mapRng)); // mo1
        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(14, eliteEnemies.Retrieve(mapRng)); // mo2
    }

    protected void generateMonsters()
    {
        generateWeakEnemies(0);
        generateStrongEnemies(0);
        generateElites(0);
    }

    @Override
    protected void generateWeakEnemies(int count)
    {
        // These aren't even used but whatever
        monsterList.add(UnnamedEnemyGroup.CULTIST);
        monsterList.add(UnnamedEnemyGroup.THREE_NORMAL_SHAPES);
        monsterList.add(UnnamedEnemyGroup.TWO_SHAPES);
    }

    @Override
    protected void generateStrongEnemies(int count)
    {
        monsterList.add(UnnamedEnemyGroup.CULTIST_AND_TWO_SHAPES);
        monsterList.add(UnnamedEnemyGroup.THREE_LAGAVULIN);
        monsterList.add(UnnamedEnemyGroup.LARGE_CUBE);
        monsterList.add(UnnamedEnemyGroup.LARGE_CRYSTAL);
        monsterList.add(UnnamedEnemyGroup.LARGE_WISP);
    }

    @Override
    protected void generateElites(int count)
    {
        // These aren't even used but whatever
        eliteMonsterList.add(UnnamedEnemyGroup.ULTIMATE_WISP);
        eliteMonsterList.add(UnnamedEnemyGroup.ULTIMATE_CUBE);
        eliteMonsterList.add(UnnamedEnemyGroup.ULTIMATE_CRYSTAL);
    }
}