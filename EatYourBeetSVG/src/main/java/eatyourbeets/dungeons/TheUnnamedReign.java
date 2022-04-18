package eatyourbeets.dungeons;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.*;
import com.megacrit.cardcrawl.cards.colorless.*;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.cards.curses.Clumsy;
import com.megacrit.cardcrawl.cards.curses.CurseOfTheBell;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import com.megacrit.cardcrawl.cards.green.*;
import com.megacrit.cardcrawl.cards.red.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.EmptyRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.blights.animator.UltimateCrystalBlight;
import eatyourbeets.blights.animator.UltimateCubeBlight;
import eatyourbeets.blights.animator.UltimateWispBlight;
import eatyourbeets.cards.animator.curse.special.Curse_AscendersBane;
import eatyourbeets.cards.animator.series.Katanagatari.HigakiRinne;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.monsters.UnnamedReign.UnnamedEnemyGroup;
import eatyourbeets.resources.GR;
import eatyourbeets.scenes.TheUnnamedReignScene;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class TheUnnamedReign extends AbstractDungeon
{
    public static final String NAME = GR.Common.Strings.TheUnnamedReign.Name;
    public static final String ID = GR.Common.CreateID("TheUnnamedReign");

    protected ArrayList<String> MONSTER_LIST_WHICH_ACTUALLY_WORKS;

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

        GenerateMap(null);

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

        GenerateMap(saveFile);

//        MapRoomNode victoryNode = new MapRoomNode(3, map.size());
//        victoryNode.room = new TrueVictoryRoom();

        firstRoomChosen = true;
        this.populatePathTaken(saveFile);
    }

    protected void GenerateMap(SaveFile saveFile)
    {
        AbstractDungeon.map = new ArrayList<>();
        TheUnnamedReign_Map.GenerateMap(AbstractDungeon.map);

        GenerateMonsterPool(saveFile);
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
            DungeonMap.boss = GR.Common.Images.UnnamedReignBoss.Texture(true);
            DungeonMap.bossOutline = GR.Common.Images.UnnamedReignBossOutline.Texture(true);
        }
        else
        {
            SpireSuper.call(key);
        }
    }

    @Override
    protected void initializeLevelSpecificChances()
    {
        shopRoomChance = 0.12f;
        restRoomChance = 0.10f;
        treasureRoomChance = 0f;
        eventRoomChance = 0.15f;
        eliteRoomChance = 0.12f;
        smallChestChance = 0;
        mediumChestChance = 0;
        largeChestChance = 100;
        commonRelicChance = 100;
        uncommonRelicChance = 0;
        rareRelicChance = 0;
        colorlessRareChance = 0.3f;
        if (AbstractDungeon.ascensionLevel >= 12)
        {
            cardUpgradedChance = 0.15f;
        }
        else
        {
            cardUpgradedChance = 0.3f;
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

    @Override
    protected ArrayList<String> generateExclusions()
    {
        return new ArrayList<>();
    }

    @Override
    protected void initializeBoss()
    {
        bossList.clear();
        bossList.add(UnnamedEnemyGroup.THE_UNNAMED);
        bossList.add(UnnamedEnemyGroup.THE_UNNAMED);
        bossList.add(UnnamedEnemyGroup.THE_UNNAMED);
    }

    @Override
    public void initializeSpecialOneTimeEventList()
    {

    }

    @Override
    protected void initializeEventList()
    {
//        eventList.add("MindBloom");
//        eventList.add("The Moai Head");
//        eventList.add("Mysterious Sphere");
//        eventList.add("Tomb of Lord Red Mask");
        eventList.add("SensoryStone");
//        eventList.add("Winding Halls");
    }

    @Override
    protected void initializeEventImg()
    {
        if (eventBackgroundImg != null)
        {
            eventBackgroundImg.dispose();
            eventBackgroundImg = null;
        }

        eventBackgroundImg = ImageMaster.loadImage("images/ui/event/panel.png");
    }

    @Override
    protected void initializeShrineList()
    {
//        shrineList.add("Match and Keep!");
//        //shrineList.add("Wheel of Change");
//        shrineList.add("Golden Shrine");
//        shrineList.add("Transmorgrifier");
//        shrineList.add("Purifier");
//        shrineList.add("Upgrade Shrine");
    }

    protected void GenerateMonsterPool(SaveFile saveFile)
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

        final RandomizedList<String> eliteEnemies = new RandomizedList<>();
        eliteEnemies.Add(UnnamedEnemyGroup.ULTIMATE_CRYSTAL);
        eliteEnemies.Add(UnnamedEnemyGroup.ULTIMATE_CUBE);
        eliteEnemies.Add(UnnamedEnemyGroup.ULTIMATE_WISP);

        String addLast = null;
        final String blight = GameUtilities.GetAscensionBlightChoice();
        if (blight != null)
        {
            if (UltimateCrystalBlight.ID.equals(blight))
            {
                addLast = UnnamedEnemyGroup.ULTIMATE_CRYSTAL;
            }
            else if (UltimateCubeBlight.ID.equals(blight))
            {
                addLast = UnnamedEnemyGroup.ULTIMATE_CUBE;
            }
            else if (UltimateWispBlight.ID.equals(blight))
            {
                addLast = UnnamedEnemyGroup.ULTIMATE_WISP;
            }

            if (addLast != null)
            {
                eliteEnemies.Remove(addLast);
            }
        }

        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(9, eliteEnemies.Retrieve(mapRng));  // mo1
        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(10, null);  // tr1
        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(11, null); // ev1
        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(12, null); // r1/sh1

        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(13, eliteEnemies.Retrieve(mapRng)); // mo1
        MONSTER_LIST_WHICH_ACTUALLY_WORKS.add(14, addLast != null ? addLast : eliteEnemies.Retrieve(mapRng)); // mo2
    }

    @Override
    protected void generateMonsters()
    {
        generateWeakEnemies(0);
        generateStrongEnemies(0);
        generateElites(0);
    }

    @Override
    protected void generateWeakEnemies(int count)
    {
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
        eliteMonsterList.add(UnnamedEnemyGroup.ULTIMATE_WISP);
        eliteMonsterList.add(UnnamedEnemyGroup.ULTIMATE_CUBE);
        eliteMonsterList.add(UnnamedEnemyGroup.ULTIMATE_CRYSTAL);
    }

    public static ArrayList<AbstractCard> GetCardReplacements(ArrayList<AbstractCard> cards, boolean forceReplace)
    {
        final ArrayList<AbstractCard> result = new ArrayList<>();
        for (AbstractCard c : cards)
        {
            for (String cardID : GetCardReplacements(c, forceReplace))
            {
                UnlockTracker.markCardAsSeen(cardID);
                result.add(CardLibrary.getCard(cardID).makeCopy());
            }
        }

        return result;
    }

    public static ArrayList<String> GetCardReplacements(AbstractCard card, boolean forceReplace)
    {
        ArrayList<String> replacements = new ArrayList<>();
        switch (card.cardID)
        {
            case "infinitespire:Virus":
            {
                replacements.add(Anger.ID);
                replacements.add(HigakiRinne.DATA.ID);
                break;
            }

            case "hubris:Fate":
            {
                replacements.add(Discovery.ID);
                break;
            }

            case "hubris:Rewind":
            {
                replacements.add(EchoForm.ID);
                break;
            }

            case "hubris:InfiniteBlow":
            {
                replacements.add(SearingBlow.ID);
                break;
            }

            case "infinitespire:Gouge":
            {
                replacements.add(BeamCell.ID);
                replacements.add(Neutralize.ID);
                break;
            }

            case "infinitespire:SevenWalls":
            {
                replacements.add(Dash.ID);
                break;
            }

            case "infinitespire:Starlight":
            {
                replacements.add(BandageUp.ID);
                break;
            }

            case "infinitespire:Fortify":
            {
                replacements.add(ReinforcedBody.ID);
                break;
            }

            case "infinitespire:Punishment":
            {
                replacements.add(MindBlast.ID);
                break;
            }

            case "infinitespire:FutureSight":
            {
                replacements.add(SeeingRed.ID);
                break;
            }

            case "infinitespire:Oblivion":
            {
                replacements.add(Chaos.ID);
                break;
            }

            case "infinitespire:DeathsTouch":
            {
                replacements.add(Bludgeon.ID);
                break;
            }

            case "infinitespire:NeuralNetwork":
            {
                replacements.add(MachineLearning.ID);
                break;
            }

            case "infinitespire:Execution":
            {
                replacements.add(Terror.ID);
                break;
            }

            case "infinitespire:Menacing":
            {
                replacements.add(Apparition.ID);
                replacements.add(Apparition.ID);
                break;
            }

            case "infinitespire:TheBestDefense": // This card...
            {
                replacements.add(Flex.ID);
                replacements.add(Apparition.ID);
                replacements.add(Apparition.ID);
                break;
            }

            case "infinitespire:UltimateForm":
            {
                replacements.add(Inflame.ID);
                replacements.add(Defragment.ID);
                replacements.add(Footwork.ID);
                break;
            }

            case "infinitespire:Collect":
            case "infinitespire:Haul":
            {
                replacements.add(MasterOfStrategy.ID);
                break;
            }

            case "ReplayTheSpireMod:Black Plague":
            {
                replacements.add(Malaise.ID);
                replacements.add(NoxiousFumes.ID);
                break;
            }

            case CurseOfTheBell.ID:
            case Necronomicurse.ID:
            {
                replacements.add(Clumsy.ID);
                break;
            }

            case AscendersBane.ID:
            {
                replacements.add(Curse_AscendersBane.DATA.ID);
                break;
            }

            default:
            {
                boolean forbidden = false;
                if (card.cardID.startsWith("hubris")
                        ||  card.cardID.startsWith("ReplayTheSpireMod")
                        ||  card.cardID.startsWith("infinitespire")
                        ||  card.cardID.startsWith("StuffTheSpire"))
                {
                    forbidden = true;
                }
                else
                {
                    Class c = card.getClass().getSuperclass();
                    if (c != null && c.getSimpleName().equals("AbstractUrbanLegendCard"))
                    {
                        forbidden = true;
                    }
                }

                if (forbidden)
                {
                    replacements.add(HigakiRinne.DATA.ID);
                }
                else if (forceReplace)
                {
                    replacements.add(card.cardID);
                }
            }
        }

        return replacements;
    }
}