package eatyourbeets.monsters.UnnamedReign;

import basemod.BaseMod;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import eatyourbeets.monsters.UnnamedReign.Shapes.UnnamedShape;
import eatyourbeets.utilities.RandomizedList;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.monsters.UnnamedReign.Shapes.Crystal.Crystal;
import eatyourbeets.monsters.UnnamedReign.Shapes.Crystal.UltimateCrystal;
import eatyourbeets.monsters.UnnamedReign.Shapes.Cube.*;
import eatyourbeets.monsters.UnnamedReign.UnnamedCultist.TheUnnamed_Cultist_BEHOLD;
import eatyourbeets.monsters.UnnamedReign.UnnamedCultist.TheUnnamed_Cultist_DollSummoner;
import eatyourbeets.monsters.UnnamedReign.UnnamedCultist.TheUnnamed_Cultist_Single;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterShape;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.monsters.UnnamedReign.Shapes.Wisp.UltimateWisp;
import eatyourbeets.monsters.UnnamedReign.Shapes.Wisp.Wisp;
import eatyourbeets.monsters.UnnamedReign.UnnamedHat.TheUnnamed_Hat;
import eatyourbeets.powers.PlayerStatistics;

public class UnnamedEnemyGroup
{
    public static String TWO_SHAPES = "animator:DOUBLE_SHAPES_WEAK";
    public static String UNNAMED_HAT = "animator:UNNAMED_HAT";
    public static String THREE_NORMAL_SHAPES = "animator:TRIPLE_SHAPES_WEAK";
    public static String CULTIST = "animator:UNNAMED_CULTIST_1";
    public static String LARGE_CRYSTAL = "animator:LARGE_CRYSTAL";
    public static String LARGE_CUBE = "animator:LARGE_CUBE";
    public static String LARGE_WISP = "animator:LARGE_WISP";
    public static String CULTIST_AND_TWO_SHAPES = "animator:UNNAMED_CULTIST_2";
    public static String THREE_LAGAVULIN = "animator:TRIPLE_LAGAVULIN";
    public static String ULTIMATE_CRYSTAL = "animator:ULTIMATE_CRYSTAL";
    public static String ULTIMATE_CUBE = "animator:ULTIMATE_CUBE";
    public static String ULTIMATE_WISP = "animator:ULTIMATE_WISP";
    public static String THE_UNNAMED = TheUnnamed.ID;

    private final static float CULTIST_X = 180;
    private final static float CULTIST_Y = 12;
    private final static float H4T_X = 270;
    private final static float H4T_Y = -32;

    public static void RegisterMonsterGroups()
    {
        BaseMod.addMonster(TWO_SHAPES, UnnamedEnemyGroup::TwoShapes);
        BaseMod.addMonster(THREE_NORMAL_SHAPES, UnnamedEnemyGroup::ThreeNormalShapes);
        BaseMod.addMonster(CULTIST, UnnamedEnemyGroup::Cultist);
        BaseMod.addMonster(UNNAMED_HAT, UnnamedEnemyGroup::UnnamedHat);
        BaseMod.addMonster(LARGE_CRYSTAL, UnnamedEnemyGroup::LargeCrystal);
        BaseMod.addMonster(LARGE_CUBE, UnnamedEnemyGroup::LargeCube);
        BaseMod.addMonster(LARGE_WISP, UnnamedEnemyGroup::LargeWisp);
        BaseMod.addMonster(CULTIST_AND_TWO_SHAPES, UnnamedEnemyGroup::CultistAndTwoShapes);
        BaseMod.addMonster(THREE_LAGAVULIN, UnnamedEnemyGroup::ThreeLagavulin);
        BaseMod.addMonster(ULTIMATE_CRYSTAL, UltimateCrystal.NAME, UnnamedEnemyGroup::UltimateCrystal);
        BaseMod.addMonster(ULTIMATE_CUBE, UltimateCube.NAME, UnnamedEnemyGroup::UltimateCube);
        BaseMod.addMonster(ULTIMATE_WISP, UltimateWisp.NAME, UnnamedEnemyGroup::UltimateWisp);
        BaseMod.addMonster(THE_UNNAMED, TheUnnamed.NAME, TheUnnamed::new);
    }

    public static MonsterGroup ThreeNormalShapes()
    {
        RandomizedList<MonsterShape> shapes = MonsterShape.GetRandomizedList();
        RandomizedList<MonsterElement> elements = MonsterElement.GetRandomizedList();

        AbstractMonster[] enemies = new AbstractMonster[3];
        enemies[2] = Create(0, Retrieve(shapes), MonsterTier.Normal, Retrieve(elements));
        enemies[1] = Create(1, Retrieve(shapes), MonsterTier.Normal, Retrieve(elements));
        enemies[0] = Create(2, Retrieve(shapes), MonsterTier.Normal, Retrieve(elements));

        return new MonsterGroup(enemies);
    }

    public static MonsterGroup TwoShapes()
    {
        RandomizedList<MonsterShape> shapes = MonsterShape.GetRandomizedList();
        RandomizedList<MonsterElement> elements = MonsterElement.GetRandomizedList();

        AbstractMonster[] enemies = new AbstractMonster[2];
        enemies[0] = Create(0, Retrieve(shapes), MonsterTier.Advanced, Retrieve(elements));
        enemies[1] = Create(1, Retrieve(shapes), MonsterTier.Normal, Retrieve(elements));

        return new MonsterGroup(enemies);
    }

    public static MonsterGroup CultistAndTwoShapes()
    {
        RandomizedList<MonsterShape> shapes = MonsterShape.GetRandomizedList();
        RandomizedList<MonsterElement> elements = MonsterElement.GetRandomizedList();

        AbstractMonster[] enemies = new AbstractMonster[3];
        enemies[2] = new TheUnnamed_Cultist_DollSummoner(CULTIST_X, CULTIST_Y);
        enemies[1] = Create(1, Retrieve(shapes), MonsterTier.Advanced, Retrieve(elements));
        enemies[0] = Create(2, Retrieve(shapes), MonsterTier.Normal, Retrieve(elements));

        return new MonsterGroup(enemies);
    }

    public static MonsterGroup ThreeLagavulin()
    {
        AbstractMonster[] enemies = new AbstractMonster[3];

        for (int i = 0; i < 3; i++)
        {
            Lagavulin lagavulin = new Lagavulin(true);
            lagavulin.currentHealth = lagavulin.maxHealth += 40;
            lagavulin.type = AbstractMonster.EnemyType.NORMAL;

            if (i == 0)
            {
                lagavulin.drawX += Settings.scale * 200;
                lagavulin.drawY += Settings.scale * 26;
            }
            else if (i == 1)
            {
                lagavulin.drawX += Settings.scale * -150;
                lagavulin.drawY += Settings.scale * -21;
            }
            else
            {
                lagavulin.drawX += Settings.scale * -460;
                lagavulin.drawY += Settings.scale * 24;
            }

            enemies[i] = lagavulin;
        }

        return new LagavulinMonsterGroup(enemies);
    }

    public static MonsterGroup LargeCrystal()
    {
        RandomizedList<MonsterShape> shapes = MonsterShape.GetRandomizedList(MonsterShape.Crystal);
        RandomizedList<MonsterElement> elements = MonsterElement.GetRandomizedList();

        AbstractMonster[] enemies = new AbstractMonster[3];
        enemies[2] = Create(0, Retrieve(shapes), MonsterTier.Normal, Retrieve(elements));
        enemies[1] = Create(1, MonsterShape.Crystal, MonsterTier.Advanced, Retrieve(elements));
        enemies[0] = Create(2, Retrieve(shapes), MonsterTier.Normal, Retrieve(elements));

        return new MonsterGroup(enemies);
    }

    public static MonsterGroup LargeWisp()
    {
        RandomizedList<MonsterShape> shapes = MonsterShape.GetRandomizedList(MonsterShape.Wisp);
        RandomizedList<MonsterElement> elements = MonsterElement.GetRandomizedList();

        AbstractMonster[] enemies = new AbstractMonster[3];
        enemies[2] = Create(0, Retrieve(shapes), MonsterTier.Normal, Retrieve(elements));
        enemies[1] = Create(1, MonsterShape.Wisp, MonsterTier.Advanced, Retrieve(elements));
        enemies[0] = Create(2, Retrieve(shapes), MonsterTier.Normal, Retrieve(elements));

        return new MonsterGroup(enemies);
    }

    public static MonsterGroup LargeCube()
    {
        RandomizedList<MonsterShape> shapes = MonsterShape.GetRandomizedList(MonsterShape.Cube);
        RandomizedList<MonsterElement> elements = MonsterElement.GetRandomizedList();

        AbstractMonster[] enemies = new AbstractMonster[3];
        enemies[2] = Create(0, Retrieve(shapes), MonsterTier.Normal, Retrieve(elements));
        enemies[1] = Create(1, MonsterShape.Cube, MonsterTier.Advanced, Retrieve(elements));
        enemies[0] = Create(2, Retrieve(shapes), MonsterTier.Normal, Retrieve(elements));

        return new MonsterGroup(enemies);
    }

    public static MonsterGroup UnnamedHat()
    {
        return new MonsterGroup(new TheUnnamed_Hat(H4T_X, H4T_Y));
    }

    public static MonsterGroup Cultist()
    {
        return new MonsterGroup(new TheUnnamed_Cultist_Single(CULTIST_X, CULTIST_Y));
    }

    public static MonsterGroup UltimateCrystal()
    {
        return new MonsterGroup(new TheUnnamed_Cultist_BEHOLD(CULTIST_X, CULTIST_Y));
    }

    public static MonsterGroup UltimateWisp()
    {
        return new MonsterGroup(new UltimateWisp());
    }

    public static MonsterGroup UltimateCube()
    {
        return new MonsterGroup(new UltimateCube());
    }


    // Utility

    protected static <T> T Retrieve(RandomizedList<T> list)
    {
        return list.Retrieve(PlayerStatistics.SaveData.GetRNG());
    }

    private static UnnamedShape Create(int index, MonsterShape shape, MonsterTier tier, MonsterElement element)
    {
        switch (shape)
        {
            case Cube:
            {
                return Cube.CreateEnemy(tier, element, xPos[index], yPos[index]);
            }

            case Crystal:
            {
                return Crystal.CreateEnemy(tier, element, xPos[index], yPos[index]);
            }

            case Wisp:
            {
                return Wisp.CreateEnemy(tier, element, xPos[index], yPos[index]);
            }
        }

        return null;
    }


    private static final float[] xPos = new float[4];
    private static final float[] yPos = new float[4];
    static
    {
        yPos[0] = 12;
        yPos[1] = -8;
        yPos[2] = 8;
        yPos[3] = -12;

        xPos[0] = 0;
        xPos[1] = -240;
        xPos[2] = -480;
        xPos[3] = 240;
    }

    public static class LagavulinMonsterGroup extends MonsterGroup
    {
        public LagavulinMonsterGroup(AbstractMonster[] input)
        {
            super(input);
        }

        public LagavulinMonsterGroup(AbstractMonster m)
        {
            super(m);
        }

        @Override
        public void usePreBattleAction()
        {
            super.usePreBattleAction();

            CardCrawlGame.music.fadeAll();
        }
    }
}