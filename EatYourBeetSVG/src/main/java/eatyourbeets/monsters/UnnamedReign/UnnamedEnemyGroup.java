package eatyourbeets.monsters.UnnamedReign;

import basemod.BaseMod;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.misc.RandomizedList;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.monsters.UnnamedReign.Crystal.Crystal;
import eatyourbeets.monsters.UnnamedReign.Crystal.UltimateCrystal;
import eatyourbeets.monsters.UnnamedReign.Cube.*;
import eatyourbeets.monsters.UnnamedReign.Cultist.TheUnnamed_Cultist;
import eatyourbeets.monsters.UnnamedReign.Cultist.TheUnnamed_Cultist_BEHOLD;
import eatyourbeets.monsters.UnnamedReign.Cultist.TheUnnamed_Cultist_DollSummoner;
import eatyourbeets.monsters.UnnamedReign.Cultist.TheUnnamed_Cultist_Single;
import eatyourbeets.monsters.UnnamedReign.Wisp.UltimateWisp;
import eatyourbeets.monsters.UnnamedReign.Wisp.Wisp;

public class UnnamedEnemyGroup
{
    public static String TWO_SHAPES = "Animator_DOUBLE_SHAPES_WEAK";
    public static String THREE_NORMAL_SHAPES = "Animator_TRIPLE_SHAPES_WEAK";
    public static String CULTIST = "Animator_UNNAMED_CULTIST_1";
    public static String LARGE_CRYSTAL = "Animator_LARGE_CRYSTAL";
    public static String LARGE_CUBE = "Animator_LARGE_CUBE";
    public static String LARGE_WISP = "Animator_LARGE_WISP";
    public static String CULTIST_AND_TWO_SHAPES = "Animator_UNNAMED_CULTIST_2";
    public static String THREE_LAGAVULIN = "Animator_TRIPLE_LAGAVULIN";
    public static String ULTIMATE_CRYSTAL = "Animator_ULTIMATE_CRYSTAL";
    public static String ULTIMATE_CUBE = "Animator_ULTIMATE_CUBE";
    public static String ULTIMATE_WISP = "Animator_ULTIMATE_WISP";
    public static String THE_UNNAMED = TheUnnamed.ID;

    private static Random rng = new Random();

    private final static float CULTIST_X = 180;
    private final static float CULTIST_Y = 12;

    public static void RegisterMonsterGroups()
    {
        BaseMod.addMonster(TWO_SHAPES, UnnamedEnemyGroup::TwoShapes);
        BaseMod.addMonster(THREE_NORMAL_SHAPES, UnnamedEnemyGroup::ThreeNormalShapes);
        BaseMod.addMonster(CULTIST, UnnamedEnemyGroup::Cultist);
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
        RandomizedList<MonsterShape> shapes = GetShapes();
        RandomizedList<MonsterElement> elements = GetElements();

        AbstractMonster[] enemies = new AbstractMonster[3];
        enemies[0] = Create(0, Retrieve(shapes), MonsterTier.Normal, Retrieve(elements));
        enemies[1] = Create(1, Retrieve(shapes), MonsterTier.Normal, Retrieve(elements));
        enemies[2] = Create(2, Retrieve(shapes), MonsterTier.Normal, Retrieve(elements));

        return new MonsterGroup(enemies);
    }

    public static MonsterGroup TwoShapes()
    {
        RandomizedList<MonsterShape> shapes = GetShapes();
        RandomizedList<MonsterElement> elements = GetElements();

        AbstractMonster[] enemies = new AbstractMonster[2];
        enemies[0] = Create(0, Retrieve(shapes), MonsterTier.Advanced, Retrieve(elements));
        enemies[1] = Create(1, Retrieve(shapes), MonsterTier.Normal, Retrieve(elements));

        return new MonsterGroup(enemies);
    }

    public static MonsterGroup CultistAndTwoShapes()
    {
        RandomizedList<MonsterShape> shapes = GetShapes();
        RandomizedList<MonsterElement> elements = GetElements();

        AbstractMonster[] enemies = new AbstractMonster[3];
        enemies[0] = new TheUnnamed_Cultist_DollSummoner(CULTIST_X, CULTIST_Y);
        enemies[1] = Create(1, Retrieve(shapes), MonsterTier.Advanced, Retrieve(elements));
        enemies[2] = Create(2, Retrieve(shapes), MonsterTier.Normal, Retrieve(elements));

        return new MonsterGroup(enemies);
    }

    public static MonsterGroup ThreeLagavulin()
    {
        AbstractMonster[] enemies = new AbstractMonster[3];

        for (int i = 0; i < 3; i++)
        {
            Lagavulin lagavulin = new Lagavulin(true);
            lagavulin.currentHealth = lagavulin.maxHealth += 90;
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

        return new MonsterGroup(enemies);
    }

    public static MonsterGroup LargeCrystal()
    {
        RandomizedList<MonsterShape> shapes = GetShapes(MonsterShape.Crystal);
        RandomizedList<MonsterElement> elements = GetElements();

        AbstractMonster[] enemies = new AbstractMonster[3];
        enemies[0] = Create(0, shapes.Retrieve(rng), MonsterTier.Normal, elements.Retrieve(rng));
        enemies[1] = Create(1, MonsterShape.Crystal, MonsterTier.Advanced, elements.Retrieve(rng));
        enemies[2] = Create(2, shapes.Retrieve(rng), MonsterTier.Normal, elements.Retrieve(rng));

        return new MonsterGroup(enemies);
    }

    public static MonsterGroup LargeWisp()
    {
        RandomizedList<MonsterShape> shapes = GetShapes(MonsterShape.Wisp);
        RandomizedList<MonsterElement> elements = GetElements();

        AbstractMonster[] enemies = new AbstractMonster[3];
        enemies[0] = Create(0, shapes.Retrieve(rng), MonsterTier.Normal, elements.Retrieve(rng));
        enemies[1] = Create(1, MonsterShape.Wisp, MonsterTier.Advanced, elements.Retrieve(rng));
        enemies[2] = Create(2, shapes.Retrieve(rng), MonsterTier.Normal, elements.Retrieve(rng));

        return new MonsterGroup(enemies);
    }

    public static MonsterGroup LargeCube()
    {
        RandomizedList<MonsterShape> shapes = GetShapes(MonsterShape.Cube);
        RandomizedList<MonsterElement> elements = GetElements();

        AbstractMonster[] enemies = new AbstractMonster[3];
        enemies[0] = Create(0, shapes.Retrieve(rng), MonsterTier.Normal, elements.Retrieve(rng));
        enemies[1] = Create(1, MonsterShape.Cube, MonsterTier.Advanced, elements.Retrieve(rng));
        enemies[2] = Create(2, shapes.Retrieve(rng), MonsterTier.Normal, elements.Retrieve(rng));

        return new MonsterGroup(enemies);
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
        if (AbstractDungeon.mapRng != null)
        {
            return list.Retrieve(AbstractDungeon.mapRng);
        }
        else
        {
            return list.Retrieve(rng);
        }
    }

    protected static RandomizedList<MonsterShape> GetShapes(MonsterShape exception)
    {
        RandomizedList<MonsterShape> result = GetShapes();
        result.GetInnerList().remove(exception);
        return result;
    }

    protected static RandomizedList<MonsterElement> GetElements(MonsterElement exception)
    {
        RandomizedList<MonsterElement> result = GetElements();
        result.GetInnerList().remove(exception);
        return result;
    }

    protected static RandomizedList<MonsterShape> GetShapes()
    {
        RandomizedList<MonsterShape> result = new RandomizedList<>();
        result.Add(MonsterShape.Crystal);
        result.Add(MonsterShape.Cube);
        result.Add(MonsterShape.Wisp);
        return result;
    }

    protected static RandomizedList<MonsterElement> GetElements()
    {
        RandomizedList<MonsterElement> result = new RandomizedList<>();
        result.Add(MonsterElement.Dark);
        result.Add(MonsterElement.Fire);
        result.Add(MonsterElement.Frost);
        result.Add(MonsterElement.Healing);
        result.Add(MonsterElement.Lightning);
        return result;
    }

    private static UnnamedMonster Create(int index, MonsterShape shape, MonsterTier tier, MonsterElement element)
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
        yPos[1] = -6;
        yPos[2] = 6;
        yPos[3] = -12;

        xPos[0] = 0;
        xPos[1] = -240;
        xPos[2] = -480;
        xPos[3] = 240;
    }
}