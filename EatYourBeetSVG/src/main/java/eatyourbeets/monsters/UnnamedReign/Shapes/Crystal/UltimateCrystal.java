package eatyourbeets.monsters.UnnamedReign.Shapes.Crystal;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WraithFormPower;
import eatyourbeets.actions.monsters.MoveMonsterAction;
import eatyourbeets.actions.monsters.SummonMonsterAction;
import eatyourbeets.actions.utility.WaitRealtimeAction;
import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.effects.SFX;
import eatyourbeets.monsters.EYBMoveset;
import eatyourbeets.monsters.SharedMoveset.EYBMove_Attack;
import eatyourbeets.monsters.SharedMoveset.special.EYBMove_AttackDefend_ViceCrush;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterShape;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.monsters.UltimateCrystalPower;
import eatyourbeets.powers.replacement.AntiArtifactSlowPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class UltimateCrystal extends Crystal
{
    public static final String ID = CreateFullID(MonsterShape.Crystal, MonsterElement.Ultimate, MonsterTier.Ultimate);
    public static final String NAME = "Ultimate Crystal";

    private final UltimateCrystal original;
    private final EYBMove_Attack antiIntangible;
    private int currentStrength = -1;

    public UltimateCrystal()
    {
        this(0, 0, null);
    }

    public UltimateCrystal(float x, float y, UltimateCrystal original)
    {
        super(MonsterElement.Ultimate, MonsterTier.Ultimate, x, y);

        this.original = original;

        antiIntangible = moveset.Special.Attack(6, 30);

        moveset.SetFindSpecialMove(roll ->
        {
            if (GameUtilities.GetPowerAmount(IntangiblePlayerPower.POWER_ID) >= 2 && !AbstractDungeon.player.hasPower(WraithFormPower.POWER_ID))
            {
                return antiIntangible;
            }
            else
            {
                return null;
            }
        });

        // Rotation:
        moveset.mode = EYBMoveset.Mode.Sequential;

        if (original == null)
        {
            moveset.Normal.Buff(PowerHelper.Strength, 3)
            .AddPower(PowerHelper.Artifact, 2);

            moveset.Normal.Add(new EYBMove_AttackDefend_ViceCrush(1, 11))
            .SetBlockScaling(0.5f);

            moveset.Normal.ShuffleCard(new Crystallize(), 2)
            .SetMiscBonus(4, 1);
        }
        else
        {
            moveset.Normal.ShuffleCard(new Crystallize(), 2)
            .SetMiscBonus(4, 1);

            moveset.Normal.Buff(PowerHelper.Strength, 3)
            .AddPower(PowerHelper.Artifact, 2);

            moveset.Normal.Add(new EYBMove_AttackDefend_ViceCrush(1, 11))
            .SetBlockScaling(0.5f);
        }
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        int strength = GameUtilities.GetPowerAmount(this, StrengthPower.POWER_ID);
        if (strength != currentStrength)
        {
            currentStrength = strength;

            float scale = data.scale * (1 - (0.7f * (Math.min(100, strength) / 100f)));
            loadAnimation(data.atlasUrl, data.jsonUrl, scale);

            AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
            e.setTimeScale(0.5f);
            e.setTime(e.getEndTime() * MathUtils.random());
        }
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        if (original == null)
        {
            GameActions.Bottom.StackPower(new UltimateCrystalPower(this, 6));
            GameActions.Bottom.StackPower(new AntiArtifactSlowPower(this, 1));

            GameEffects.List.Callback(new WaitRealtimeAction(15), () -> CardCrawlGame.music.unsilenceBGM());

            CardCrawlGame.sound.play(SFX.ANIMATOR_THE_ULTIMATE_CRYSTAL, true);
        }
        else
        {
            this.powers.clear();
            for (AbstractPower p : original.powers)
            {
                CloneablePowerInterface cloneablePower = JUtils.SafeCast(p, CloneablePowerInterface.class);
                if (cloneablePower != null)
                {
                    final AbstractPower power = cloneablePower.makeCopy();
                    if (power != null)
                    {
                        power.amount = p.amount;
                        power.owner = this;
                        this.powers.add(power);
                    }
                }
            }

            this.currentHealth = original.currentHealth;
            this.maxHealth = original.maxHealth;
            this.healthBarUpdatedEvent();
            this.applyPowers();
        }
    }

    @Override
    public void die()
    {
        super.die();

        if (GameUtilities.GetEnemies(true).isEmpty())
        {
            AbstractDungeon.scene.fadeInAmbiance();
            CardCrawlGame.music.fadeOutTempBGM();
        }
    }

    public void SummonCopy()
    {
        UltimateCrystal copy = new UltimateCrystal(-310, -14, this);

        float targetX = copy.drawX;
        float targetY = copy.drawY;

        copy.drawX = this.drawX - 30;
        copy.drawY = this.drawY;

        GameActions.Bottom.Add(new SummonMonsterAction(copy, false));
        GameActions.Bottom.Add(new MoveMonsterAction(copy, targetX, targetY, 1.5f));
    }
}