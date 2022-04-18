package eatyourbeets.powers.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.OrbWalker;
import com.megacrit.cardcrawl.powers.GenericStrengthUpPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.actions.monsters.SummonMonsterAction;
import eatyourbeets.blights.animator.UltimateCubeBlight;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.monsters.UnnamedReign.Shapes.Cube.Cube;
import eatyourbeets.monsters.UnnamedReign.Shapes.Cube.UltimateCube;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.monsters.UnnamedReign.Shapes.UnnamedShape;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.Mathf;
import eatyourbeets.utilities.RandomizedList;

public class UltimateCubePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(UltimateCubePower.class);
    public static final int DAMAGE_TICKS = 10;

    private final RandomizedList<MonsterElement> unselectedElements = new RandomizedList<>();
    private final int[] stageThresholds = new int[3];
    private final Random rng;
    private int stage;

    public UltimateCubePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        priority = -100;

        if (owner.maxHealth >= 500)
        {
            stageThresholds[0] = 400;
            stageThresholds[1] = 200;
            stageThresholds[2] = 0;
        }
        else
        {
            stageThresholds[0] = Mathf.CeilToInt(owner.maxHealth / 20.0f) * 10;
            stageThresholds[1] = Mathf.CeilToInt(owner.maxHealth / 40.0f) * 10;
            stageThresholds[2] = 0;
        }

        stage = 0;
        rng = GR.Common.Dungeon.GetRNG();
        canBeZero = true;
        canGoNegative = true;

        Initialize(amount);
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c)
    {
        if (amount >= 0)
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, c);
        }
        else
        {
            super.renderAmount(sb, x, y, c);
        }
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, stageThresholds[0], stageThresholds[1], stageThresholds[2], amount, GetExplosionDamageTick(), DAMAGE_TICKS);
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount)
    {
        super.wasHPLost(info, damageAmount);

        GameActions.Bottom.Callback(() ->
        {
            while (stage < stageThresholds.length && owner.currentHealth <= stageThresholds[stage])
            {
                GameActions.Bottom.Callback(stage += 1, (s, __) -> OnStageReached(s));
                this.flash();
            }
        });
    }

    @Override
    public void duringTurn()
    {
        super.duringTurn();

        if (amount > 0)
        {
            this.amount -= 1;
            updateDescription();

            if (amount <= 0)
            {
                Explode();
            }
            else if (CombatStats.TurnCount(true) % 3 == 0)
            {
                GameActions.Bottom.StackPower(new LaserDefensePower(owner, 1));
            }
        }
    }

    private void Explode()
    {
        final int damageStep = GetExplosionDamageTick();
        for (int i = 0; i < DAMAGE_TICKS; i++)
        {
            GameActions.Bottom.Wait(0.3f);
            GameActions.Bottom.VFX(VFX.SmallExplosion(owner.hb, 0.3f), 0f);
            GameActions.Bottom.DealDamage(owner, player, damageStep, DamageInfo.DamageType.THORNS, AttackEffects.NONE);
        }

        GameActions.Bottom.Add(new SuicideAction((AbstractMonster)this.owner));
        GameActions.Bottom.LoseHP(owner, owner, GameUtilities.GetHP(owner, true, false) + 1, AttackEffects.NONE);
    }

    private void OnStageReached(int stage)
    {
        if (stage == 1)
        {
            if (rng.randomBoolean())
            {
                unselectedElements.Add(MonsterElement.Dark);
                SummonCube(MonsterElement.Fire, -125, 13f, stage);
            }
            else
            {
                unselectedElements.Add(MonsterElement.Fire);
                SummonCube(MonsterElement.Dark, -125, 13f, stage);
            }
        }
        else if (stage == 2)
        {
            if (rng.randomBoolean())
            {
                unselectedElements.Add(MonsterElement.Healing);
                SummonCube(MonsterElement.Frost, -290, -9f, stage);
            }
            else
            {
                unselectedElements.Add(MonsterElement.Frost);
                SummonCube(MonsterElement.Healing, -290, -9f, stage);
            }
        }
        else if (stage == 3)
        {
            unselectedElements.Add(MonsterElement.Lightning);
            SummonCube(unselectedElements.Retrieve(rng), 120, 12f, stage);
        }
    }

    @Override
    public void onDeath()
    {
        super.onDeath();

        if (!player.hasBlight(UltimateCubeBlight.ID))
        {
            GameUtilities.ObtainBlight(player.hb.cX, player.hb.cY, new UltimateCubeBlight());
        }
    }

    private void SummonCube(MonsterElement element, float x, float y, int stage)
    {
        final UnnamedShape cube = Cube.CreateEnemy(stage == 3 ? MonsterTier.Advanced : MonsterTier.Normal, element, x, y);
        cube.currentHealth = (cube.maxHealth *= 0.8f);

        if (stage >= 3)
        {
            cube.drawX = owner.drawX;
            GameActions.Top.Add(new SummonMonsterAction(cube, false)).SetAnimationOffset(0);
            GameActions.Bottom.Callback(cube, (enemy, __) ->
            {
                ((UltimateCube) owner).die(true);

                if (enemy.data.element == MonsterElement.Healing)
                {
                    GameActions.Bottom.StackPower(new StrengthPower(enemy, 4))
                    .ShowEffect(false, true);
                }
                else if (enemy.data.element == MonsterElement.Lightning || enemy.data.element == MonsterElement.Frost)
                {
                    GameActions.Bottom.StackPower(new GenericStrengthUpPower(enemy, OrbWalker.MOVES[0], 4))
                    .ShowEffect(false, true);
                }
                else
                {
                    GameActions.Bottom.StackPower(new GenericStrengthUpPower(enemy, OrbWalker.MOVES[0], 6))
                    .ShowEffect(false, true);
                }

                GameActions.Bottom.StackPower(new LaserDefensePower(enemy, 1))
                .ShowEffect(false, true);
            });
            SetEnabled(false);
        }
        else
        {
            GameActions.Top.Add(new SummonMonsterAction(cube, false))
            .SetAnimationOffset(-x).SetDuration(0.6f, true);
        }
    }

    private int GetExplosionDamageTick()
    {
        return Math.max(1, (owner.currentHealth / 2) / DAMAGE_TICKS);
    }
}