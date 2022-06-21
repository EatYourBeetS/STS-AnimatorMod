package eatyourbeets.utilities;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;

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
        AllCharacters,
        PlayerMinions
    }

    public final Mode mode;

    protected List<AbstractCreature> targets;
    protected AbstractCreature source;
    protected AbstractCreature target;
    protected int amount;

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
        return new TargetHelper(Mode.Enemies, source, null, Arrays.asList(enemies), 1);
    }

    public static TargetHelper Player()
    {
        return Player(AbstractDungeon.player);
    }

    public static TargetHelper Player(AbstractCreature source)
    {
        return new TargetHelper(Mode.Player, source, AbstractDungeon.player);
    }

    public static TargetHelper PlayerMinions()
    {
        return PlayerMinions(AbstractDungeon.player);
    }

    public static TargetHelper PlayerMinions(AbstractCreature source)
    {
        return new TargetHelper(Mode.PlayerMinions, source, AbstractDungeon.player);
    }

    public static TargetHelper Source()
    {
        return Source(AbstractDungeon.player);
    }

    public static TargetHelper Source(AbstractCreature source)
    {
        return new TargetHelper(Mode.Normal, source, source);
    }

    public static TargetHelper RandomCharacter()
    {
        return RandomCharacter(AbstractDungeon.player, 1);
    }

    public static TargetHelper RandomCharacter(AbstractCreature source)
    {
        return RandomCharacter(source, 1);
    }

    public static TargetHelper RandomCharacter(AbstractCreature source, int amount)
    {
        return new TargetHelper(Mode.Random, source, null, null, amount);
    }

    public static TargetHelper RandomEnemy()
    {
        return RandomEnemy(AbstractDungeon.player, 1);
    }

    public static TargetHelper RandomEnemy(AbstractCreature source)
    {
        return RandomEnemy(source, 1);
    }

    public static TargetHelper RandomEnemy(AbstractCreature source, int amount)
    {
        return new TargetHelper(Mode.RandomEnemy, source, null, null, amount);
    }

    public static TargetHelper AllCharacters()
    {
        return AllCharacters(AbstractDungeon.player);
    }

    public static TargetHelper AllCharacters(AbstractCreature source)
    {
        return new TargetHelper(Mode.AllCharacters, source, null);
    }

    protected TargetHelper(Mode mode, AbstractCreature source, AbstractCreature target)
    {
        this(mode, source, target, null, 1);
    }

    protected TargetHelper(Mode mode, AbstractCreature source, AbstractCreature target, List<AbstractCreature> targets, int amount)
    {
        this.mode = mode;
        this.source = source;
        this.target = target;
        this.targets = targets;
        this.amount = amount;

        if (targets != null && targets.size() > 0)
        {
            this.target = targets.get(0);
        }
    }

    protected void SetAmount(int amount)
    {
        this.amount = amount;
    }

    public void SetSource(AbstractMonster owner)
    {
        this.source = owner;
    }

    public AbstractCreature GetSource()
    {
        return source;
    }

    public List<AbstractCreature> GetTargets(boolean forceRefresh)
    {
        if (targets == null || forceRefresh)
        {
            targets = new ArrayList<>();

            switch (mode)
            {
                case Normal:
                {
                    if (target != null)
                    {
                        targets.add(target);
                    }
                    break;
                }

                case Source:
                {
                    targets.add(GetSource());
                    break;
                }

                case Enemies:
                {
                    targets.addAll(GameUtilities.GetEnemies(true));
                    break;
                }

                case Player:
                {
                    targets.add(AbstractDungeon.player);
                    break;
                }

                case Random:
                {
                    final RandomizedList<AbstractCreature> list = new RandomizedList<>(GameUtilities.GetAllCharacters(true));
                    while (list.Size() > 0 && targets.size() < amount)
                    {
                        targets.add(list.Retrieve(GameUtilities.GetRNG()));
                    }
                    break;
                }

                case RandomEnemy:
                {
                    final RandomizedList<AbstractCreature> list = new RandomizedList<>(GameUtilities.GetEnemies(true));
                    while (list.Size() > 0 && targets.size() < amount)
                    {
                        targets.add(list.Retrieve(GameUtilities.GetRNG()));
                    }
                    break;
                }

                case PlayerMinions:
                {
                    targets.addAll(CombatStats.Dolls.GetAll());
                    break;
                }

                case AllCharacters:
                {
                    targets.addAll(GameUtilities.GetAllCharacters(true));
                    break;
                }
            }
        }

        return targets;
    }
}
