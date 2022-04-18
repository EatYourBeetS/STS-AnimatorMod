package eatyourbeets.monsters.UnnamedReign;

import basemod.BaseMod;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.monsters.UnnamedReign.Shapes.Crystal.Crystal;
import eatyourbeets.monsters.UnnamedReign.Shapes.Crystal.UltimateCrystal;
import eatyourbeets.monsters.UnnamedReign.Shapes.Cube.Cube;
import eatyourbeets.monsters.UnnamedReign.Shapes.Cube.UltimateCube;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterShape;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.monsters.UnnamedReign.Shapes.UnnamedShape;
import eatyourbeets.monsters.UnnamedReign.Shapes.Wisp.UltimateWisp;
import eatyourbeets.monsters.UnnamedReign.Shapes.Wisp.Wisp;
import eatyourbeets.monsters.UnnamedReign.UltimateShape.UltimateShape;
import eatyourbeets.monsters.UnnamedReign.UnnamedCultist.TheUnnamed_Cultist;
import eatyourbeets.monsters.UnnamedReign.UnnamedCultist.TheUnnamed_Cultist_BEHOLD;
import eatyourbeets.monsters.UnnamedReign.UnnamedCultist.TheUnnamed_Cultist_DollSummoner;
import eatyourbeets.monsters.UnnamedReign.UnnamedCultist.TheUnnamed_Cultist_Single;
import eatyourbeets.monsters.UnnamedReign.UnnamedHat.TheUnnamed_Hat;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.RandomizedList;

public class UnnamedEnemyGroup
{
    public static final String TWO_NORMAL_SHAPES = "animator:DOUBLE_SHAPES_WEAK";
    public static final String TWO_SHAPES = "animator:DOUBLE_SHAPES";
    public static final String UNNAMED_HAT = "animator:UNNAMED_HAT";
    public static final String THREE_NORMAL_SHAPES = "animator:TRIPLE_SHAPES_WEAK";
    public static final String CULTIST = "animator:UNNAMED_CULTIST_1";
    public static final String LARGE_CRYSTAL = "animator:LARGE_CRYSTAL";
    public static final String LARGE_CUBE = "animator:LARGE_CUBE";
    public static final String LARGE_WISP = "animator:LARGE_WISP";
    public static final String CULTIST_AND_TWO_SHAPES = "animator:UNNAMED_CULTIST_2";
    public static final String THREE_LAGAVULIN = "animator:TRIPLE_LAGAVULIN";
    public static final String ULTIMATE_CRYSTAL = "animator:ULTIMATE_CRYSTAL";
    public static final String ULTIMATE_CUBE = "animator:ULTIMATE_CUBE";
    public static final String ULTIMATE_WISP = "animator:ULTIMATE_WISP";
    public static final String ULTIMATE_SHAPE = "animator:ULTIMATE_SHAPE";
    public static final String THE_UNNAMED = TheUnnamed.ID;

    private final static float CULTIST_X = 180;
    private final static float CULTIST_Y = 12;
    private final static float H4T_X = 270;
    private final static float H4T_Y = -32;

    public static void RegisterMonsterGroups()
    {
        BaseMod.addMonster(TWO_NORMAL_SHAPES, UnnamedEnemyGroup::TwoShapesWeak);
        BaseMod.addMonster(TWO_SHAPES, UnnamedEnemyGroup::TwoShapes);
        BaseMod.addMonster(THREE_NORMAL_SHAPES, UnnamedEnemyGroup::ThreeNormalShapes);
        BaseMod.addMonster(CULTIST, TheUnnamed_Cultist.STRINGS.NAME, UnnamedEnemyGroup::Cultist);
        BaseMod.addMonster(UNNAMED_HAT, UnnamedEnemyGroup::UnnamedHat);
        BaseMod.addMonster(LARGE_CRYSTAL, UnnamedEnemyGroup::LargeCrystal);
        BaseMod.addMonster(LARGE_CUBE, UnnamedEnemyGroup::LargeCube);
        BaseMod.addMonster(LARGE_WISP, UnnamedEnemyGroup::LargeWisp);
        BaseMod.addMonster(CULTIST_AND_TWO_SHAPES, UnnamedEnemyGroup::CultistAndTwoShapes);
        BaseMod.addMonster(THREE_LAGAVULIN, Lagavulin.NAME + " (x3)", UnnamedEnemyGroup::ThreeLagavulin);
        BaseMod.addMonster(ULTIMATE_CRYSTAL, UltimateCrystal.NAME, UnnamedEnemyGroup::UltimateCrystal);
        BaseMod.addMonster(ULTIMATE_CUBE, UltimateCube.NAME, UnnamedEnemyGroup::UltimateCube);
        BaseMod.addMonster(ULTIMATE_WISP, UltimateWisp.NAME, UnnamedEnemyGroup::UltimateWisp);
        BaseMod.addMonster(ULTIMATE_SHAPE, UltimateShape.NAME, UnnamedEnemyGroup::UltimateShape);
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

    public static MonsterGroup TwoShapesWeak()
    {
        Random rng = GR.Common.Dungeon.GetRNG();
        AbstractMonster[] enemies = new AbstractMonster[2];
        switch (rng.random(2))
        {
            case 0:
            {
                enemies[0] = Create(0, MonsterShape.Cube, MonsterTier.Small, MonsterElement.Fire);
                enemies[1] = Create(1, MonsterShape.Wisp, MonsterTier.Small, MonsterElement.Healing);
                break;
            }
            case 1:
            {
                enemies[0] = Create(0, MonsterShape.Wisp, MonsterTier.Small, MonsterElement.Dark);
                enemies[1] = Create(1, MonsterShape.Cube, MonsterTier.Small, MonsterElement.Frost);
                break;
            }
            case 2:
            {
                enemies[0] = Create(0, MonsterShape.Cube, MonsterTier.Small, MonsterElement.Dark);
                enemies[1] = Create(1, MonsterShape.Wisp, MonsterTier.Small, MonsterElement.Frost);
                break;
            }
        }

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
        return new MonsterGroup(new UltimateWisp(0, 0));
    }

    public static MonsterGroup UltimateCube()
    {
        return new MonsterGroup(new UltimateCube(120, 0));
    }

    public static MonsterGroup UltimateShape()
    {
        return new MonsterGroup(new UltimateShape(-100, 130));
    }

    // Utility

    public static UnnamedShape Create(int index, MonsterShape shape, MonsterTier tier, MonsterElement element)
    {
        UnnamedShape enemy = null;
        switch (shape)
        {
            case Cube:
            {
                enemy = Cube.CreateEnemy(tier, element, xPos[index], yPos[index]);
                break;
            }

            case Crystal:
            {
                enemy = Crystal.CreateEnemy(tier, element, xPos[index], yPos[index]);
                break;
            }

            case Wisp:
            {
                enemy = Wisp.CreateEnemy(tier, element, xPos[index], yPos[index]);
                break;
            }
        }

        return enemy;
    }

    public static <T> T Retrieve(RandomizedList<T> list)
    {
        return list.Retrieve(GR.Common.Dungeon.GetRNG());
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