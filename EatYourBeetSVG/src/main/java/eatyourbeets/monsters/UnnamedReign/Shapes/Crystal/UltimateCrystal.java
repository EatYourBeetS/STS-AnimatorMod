package eatyourbeets.monsters.UnnamedReign.Shapes.Crystal;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.actions.monsters.MoveMonsterAction;
import eatyourbeets.actions.monsters.SummonMonsterAction;
import eatyourbeets.actions.utility.WaitRealtimeAction;
import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.effects.utility.CallbackEffect;
import eatyourbeets.monsters.Moveset;
import eatyourbeets.monsters.SharedMoveset.Move_AttackMultiple;
import eatyourbeets.monsters.SharedMoveset.Move_GainStrengthAndArtifact;
import eatyourbeets.monsters.SharedMoveset.Move_ShuffleCard;
import eatyourbeets.monsters.UnnamedReign.Shapes.Crystal.Moveset.Move_UltimateCrystalAttack;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterShape;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.UltimateCrystalPower;
import eatyourbeets.powers.common.AntiArtifactSlowPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

public class UltimateCrystal extends Crystal
{
    public static final String ID = CreateFullID(MonsterShape.Crystal, MonsterElement.Ultimate, MonsterTier.Ultimate);
    public static final String NAME = "Ultimate Crystal";

    public final UltimateCrystal original;

    public UltimateCrystal()
    {
        this(0, 0, null);
    }

    public UltimateCrystal(float x, float y, UltimateCrystal original)
    {
        super(MonsterElement.Ultimate, MonsterTier.Ultimate, x, y);

        moveset.mode = Moveset.Mode.Sequential;

        boolean asc4 = GameUtilities.GetActualAscensionLevel() >= 4;

        moveset.AddSpecial(new Move_AttackMultiple(6, asc4 ? 32 : 24));

        int crystallize = asc4 ? 3 : 2;
        int block = asc4 ? 18 : 11;

        this.original = original;
        if (original == null)
        {
            moveset.AddNormal(new Move_GainStrengthAndArtifact(3, 2));
            moveset.AddNormal(new Move_UltimateCrystalAttack(1, block));
            moveset.AddNormal(new Move_ShuffleCard(new Crystallize(), crystallize));
        }
        else
        {
            moveset.AddNormal(new Move_ShuffleCard(new Crystallize(), crystallize));
            moveset.AddNormal(new Move_GainStrengthAndArtifact(3, 2));
            moveset.AddNormal(new Move_UltimateCrystalAttack(1, block));
        }
    }

    private int currentStrength = -1;

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
    protected void SetNextMove(int roll, int historySize, Byte previousMove)
    {
        if (GameUtilities.GetPowerAmount(IntangiblePlayerPower.POWER_ID) >= 2 && !AbstractDungeon.player.hasPower(WraithFormPower.POWER_ID))
        {
            moveset.GetMove(Move_AttackMultiple.class).SetMove();
        }
        else
        {
            super.SetNextMove(roll, historySize, previousMove);
        }
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        if (original == null)
        {
            GameActions.Bottom.ApplyPower(this, this, new UltimateCrystalPower(this, 6), 6);
            GameActions.Bottom.ApplyPower(this, this, new AntiArtifactSlowPower(this, 1), 1);

            GameEffects.List.Add(new CallbackEffect(new WaitRealtimeAction(15),
                    this, (state, action)-> CardCrawlGame.music.unsilenceBGM()));

            CardCrawlGame.sound.play(GR.Common.Audio_TheUltimateCrystal, true);
        }
        else
        {
            for (AbstractPower p : original.powers)
            {
                CloneablePowerInterface cloneablePower = JavaUtilities.SafeCast(p, CloneablePowerInterface.class);
                if (cloneablePower != null)
                {
                    AbstractPower power = cloneablePower.makeCopy();
                    if (power != null)
                    {
                        power.amount = p.amount;
                        power.owner = this;
                        this.powers.add(power);
                    }
                }
                else if (p instanceof PoisonPower) // Poison does not implement CloneablePowerInterface...
                {
                    PoisonPower poison = JavaUtilities.SafeCast(p, PoisonPower.class);
                    if (poison != null)
                    {
                        AbstractCreature source = JavaUtilities.<AbstractCreature>GetPrivateField("source", PoisonPower.class).Get(poison);

                        this.powers.add(new PoisonPower(this, source, poison.amount));
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

        if (GameUtilities.GetCurrentEnemies(true).isEmpty())
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