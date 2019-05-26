package eatyourbeets.dungeons;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.TheBeyondScene;

import java.util.ArrayList;

import eatyourbeets.monsters.UnnamedReign.UnnamedEnemyGroup;
import eatyourbeets.scenes.TheUnnamedReignScene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TheUnnamedReign extends AbstractDungeon
{
    private static final Logger logger = LogManager.getLogger(TheUnnamedReign.class.getName());
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public static final String NAME;
    public static final String ID = "TheUnnamedReign";

    public TheUnnamedReign(AbstractPlayer p, ArrayList<String> specialOneTimeEventList)
    {
        super(NAME, ID, p, specialOneTimeEventList);

        if (scene != null)
        {
            scene.dispose();
        }

        scene = new TheUnnamedReignScene();
        fadeColor = Color.valueOf("140a1eff");
        this.initializeLevelSpecificChances();
        mapRng = new Random(Settings.seed + (long) (AbstractDungeon.actNum * 200));
        generateMap();
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

        scene = new TheBeyondScene();
        fadeColor = Color.valueOf("140a1eff");
        this.initializeLevelSpecificChances();
        miscRng = new Random(Settings.seed + (long) saveFile.floor_num);
        CardCrawlGame.music.changeBGM(id);
        mapRng = new Random(Settings.seed + (long) (saveFile.act_num * 200));
        generateMap();
        firstRoomChosen = true;
        this.populatePathTaken(saveFile);
    }

    protected void initializeLevelSpecificChances()
    {
        shopRoomChance = 0.05F;
        restRoomChance = 0.12F;
        treasureRoomChance = 0.0F;
        eventRoomChance = 0.22F;
        eliteRoomChance = 0.08F;
        smallChestChance = 0;
        mediumChestChance = 0;
        largeChestChance = 100;
        commonRelicChance = 100;
        uncommonRelicChance = 0;
        rareRelicChance = 0;
        colorlessRareChance = 0.3F;
        if (AbstractDungeon.ascensionLevel >= 12)
        {
            cardUpgradedChance = 0.25F;
        }
        else
        {
            cardUpgradedChance = 0.5F;
        }
    }

    protected void generateMonsters()
    {
        this.generateWeakEnemies(2);
        this.generateStrongEnemies(12);
        this.generateElites(10);
    }

    protected void generateWeakEnemies(int count)
    {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(UnnamedEnemyGroup.TWO_SHAPES, 2.0F));
        monsters.add(new MonsterInfo(UnnamedEnemyGroup.THREE_NORMAL_SHAPES, 2.0F));
        monsters.add(new MonsterInfo(UnnamedEnemyGroup.CULTIST, 2.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, false);
    }

    protected void generateStrongEnemies(int count)
    {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(UnnamedEnemyGroup.CULTIST_AND_TWO_SHAPES, 1.0F));
        monsters.add(new MonsterInfo(UnnamedEnemyGroup.THREE_LAGAVULIN, 1.0F));
        monsters.add(new MonsterInfo(UnnamedEnemyGroup.LARGE_CRYSTAL, 1.0F));
        monsters.add(new MonsterInfo(UnnamedEnemyGroup.LARGE_CUBE, 1.0F));
        monsters.add(new MonsterInfo(UnnamedEnemyGroup.LARGE_WISP, 1.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateFirstStrongEnemy(monsters, this.generateExclusions());
        this.populateMonsterList(monsters, count, false);
    }

    protected void generateElites(int count)
    {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(UnnamedEnemyGroup.ULTIMATE_CRYSTAL, 1.0F));
        monsters.add(new MonsterInfo(UnnamedEnemyGroup.ULTIMATE_CUBE, 1.0F));
        monsters.add(new MonsterInfo(UnnamedEnemyGroup.ULTIMATE_WISP, 1.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, true);
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
        eventList.add("Falling");
        eventList.add("MindBloom");
        eventList.add("The Moai Head");
        eventList.add("Mysterious Sphere");
        eventList.add("SensoryStone");
        eventList.add("Tomb of Lord Red Mask");
        eventList.add("Winding Halls");
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
        shrineList.add("Match and Keep!");
        shrineList.add("Wheel of Change");
        shrineList.add("Golden Shrine");
        shrineList.add("Transmorgrifier");
        shrineList.add("Purifier");
        shrineList.add("Upgrade Shrine");
    }

    static
    {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
        NAME = TEXT[0];
    }
}