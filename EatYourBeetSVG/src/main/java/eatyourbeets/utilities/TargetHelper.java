package eatyourbeets.utilities;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TargetHelper
{
    public enum Mode
    {
        Normal,
        Enemies,
        Player,
        Source,
        Random,
        RandomEnemy,
        ALL
    }

    public final Mode mode;

    protected AbstractCreature source;
    protected AbstractCreature target;
    protected List<AbstractCreature> targets;

    public static TargetHelper Normal(AbstractCreature target)
    {
        return Normal(AbstractDungeon.player, target);
    }

    public static TargetHelper Normal(AbstractCreature source, AbstractCreature target)
    {
        return new TargetHelper(Mode.Normal, source, target);
    }

    public static TargetHelper Enemies()
    {
        return Enemies(AbstractDungeon.player);
    }

    public static TargetHelper Enemies(AbstractCreature source)
    {
        return new TargetHelper(Mode.Enemies, source, null);
    }

    public static TargetHelper Enemies(AbstractCreature source, AbstractCreature... enemies)
    {
        return new TargetHelper(Mode.Enemies, source, null, Arrays.asList(enemies));
    }

    public static TargetHelper Player()
    {
        return Player(AbstractDungeon.player);
    }

    public static TargetHelper Player(AbstractCreature source)
    {
        return new TargetHelper(Mode.Player, source, AbstractDungeon.player);
    }

    public static TargetHelper Source()
    {
        return Source(AbstractDungeon.player);
    }

    public static TargetHelper Source(AbstractCreature source)
    {
        return new TargetHelper(Mode.Normal, source, source);
    }

    public static TargetHelper Random()
    {
        return Random(AbstractDungeon.player);
    }

    public static TargetHelper Random(AbstractCreature source)
    {
        return new TargetHelper(Mode.Random, source, null);
    }

    public static TargetHelper RandomEnemy()
    {
        return RandomEnemy(AbstractDungeon.player);
    }

    public static TargetHelper RandomEnemy(AbstractCreature source)
    {
        return new TargetHelper(Mode.RandomEnemy, source, null);
    }

    public static TargetHelper All()
    {
        return All(AbstractDungeon.player);
    }

    public static TargetHelper All(AbstractCreature source)
    {
        return new TargetHelper(Mode.ALL, source, null);
    }

    protected TargetHelper(Mode mode, AbstractCreature source, AbstractCreature target)
    {
        this.mode = mode;
        this.source = source;
        this.target = target;
    }

    protected TargetHelper(Mode mode, AbstractCreature source, AbstractCreature target, List<AbstractCreature> targets)
    {
        this(mode, source, target);

        this.targets = targets;

        if (target != null && targets.size() > 0)
        {
            this.target = targets.get(0);
        }
    }

    public void SetSource(AbstractMonster owner)
    {
        this.source = owner;
    }

    public AbstractCreature GetSource()
    {
        return source;
    }

    public List<AbstractCreature> GetTargets()
    {
        if (targets == null)
        {
            targets = new ArrayList<>();

            switch (mode)
            {
                case Normal:
                {
                    if (target == null)
                    {
                        throw new RuntimeException("TargetSelection.Mode." + mode.name() + " requires a fixed target.");
                    }
                    targets.add(target);
                    break;
                }

                case Source:
                    targets.add(GetSource());
                    break;

                case Enemies:
                    targets.addAll(GameUtilities.GetEnemies(true));
                    break;

                case Player:
                    targets.add(AbstractDungeon.player);
                    break;

                case Random:
                    targets.add(GameUtilities.GetRandomCharacter(true));
                    break;

                case RandomEnemy:
                    targets.add(GameUtilities.GetRandomEnemy(true));
                    break;

                case ALL:
                    targets.addAll(GameUtilities.GetAllCharacters(true));
                    break;
            }
        }

        return targets;
    }
}
