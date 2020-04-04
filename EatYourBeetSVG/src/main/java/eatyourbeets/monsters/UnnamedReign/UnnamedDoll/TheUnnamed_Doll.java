package eatyourbeets.monsters.UnnamedReign.UnnamedDoll;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.WraithFormPower;
import com.megacrit.cardcrawl.vfx.BobEffect;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.monsters.EYBMonster;
import eatyourbeets.monsters.EYBMonsterData;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.monsters.CursedStabsPower;
import eatyourbeets.powers.monsters.UnnamedDollPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.PowerTarget;

public class TheUnnamed_Doll extends EYBMonster
{
    public static final String ID = CreateFullID(TheUnnamed_Doll.class);

    private final BobEffect bobEffect = new BobEffect(1);
    private final TheUnnamed theUnnamed;
    private final EYBAbstractMove ritualAndArtifactAll;
    private final EYBAbstractMove antiIntangible;

    public TheUnnamed_Doll(TheUnnamed theUnnamed, float x, float y)
    {
        super(new Data(ID), EnemyType.NORMAL, x, y);

        this.theUnnamed = theUnnamed;
        this.data.SetIdleAnimation(this, 1);

        antiIntangible = moveset.Special.Attack(4, 12);

        ritualAndArtifactAll = moveset.Special.Buff(PowerHelper.Ritual, 2)
        .SetPowerTarget(PowerTarget.Enemies)
        .AddPower(PowerHelper.Artifact)
        .SetUses(1);

        //Rotation:
        moveset.Normal.Defend(16)
        .SetBlockScaling(0.12f)
        .SetBlockAoE(true);

        moveset.Normal.DefendBuff(9, PowerHelper.TemporaryThorns, 2)
        .SetPowerTarget(PowerTarget.Enemies)
        .SetMiscBonus(18, 1)
        .SetBlockAoE(true);

        moveset.Normal.Attack(1).SetMisc(1)
        .SetMiscBonus(18, 1)
        .SetIntent(Intent.ATTACK_DEBUFF)
        .SetOnUse((m, t) ->
        {
            final float roll = AbstractDungeon.aiRng.random();
            final int amount = m.misc.Calculate();

            if (roll < 0.33f)
            {
                GameActions.Bottom.ReduceStrength(t,2, true);
            }
            else if (roll < 0.66f)
            {
                GameActions.Bottom.ApplyFrail(this, t, amount);
            }
            else
            {
                GameActions.Bottom.ApplyWeak(this, t, amount);
            }
        });
    }

    @Override
    public void init()
    {
        super.init();

        GameActions.Bottom.ApplyPower(this, this, new CursedStabsPower(this));
        GameActions.Bottom.ApplyPower(this, this, new UnnamedDollPower(this));
        GameActions.Bottom.GainBlock(this, 26 + (int)(GameUtilities.GetAscensionLevel() * 0.66f));
    }

    @Override
    public void render(SpriteBatch sb)
    {
        animY = this.bobEffect.y;
        super.render(sb);
    }

    @Override
    public void update()
    {
        this.bobEffect.update();
        super.update();
    }

    @Override
    public void escape()
    {
        super.escape();

        if (theUnnamed != null)
        {
            theUnnamed.OnDollDeath();
        }
    }

    @Override
    public void die()
    {
        super.die();

        if (theUnnamed != null)
        {
            theUnnamed.OnDollDeath();
        }
    }

    @Override
    protected void SetNextMove(int roll, int historySize, Byte previousMove)
    {
        if (historySize >= 2 && ritualAndArtifactAll.CanUse(previousMove))
        {
            ritualAndArtifactAll.Select();
        }
        else if (GameUtilities.GetPowerAmount(IntangiblePlayerPower.POWER_ID) >= 2 && !AbstractDungeon.player.hasPower(WraithFormPower.POWER_ID))
        {
            antiIntangible.Select();
        }
        else
        {
            super.SetNextMove(roll, historySize, previousMove);
        }
    }

    protected static class Data extends EYBMonsterData
    {
        public Data(String id)
        {
            super(id);

            if (GameUtilities.GetAscensionLevel() > 7)
            {
                maxHealth = 241;
            }
            else
            {
                maxHealth = 221;
            }

            atlasUrl = "images/monsters/animator/TheUnnamed/TheUnnamedMinion.atlas";
            jsonUrl = "images/monsters/animator/TheUnnamed/TheUnnamedMinion.json";
            scale = 2;

            SetHB(0,-20,120,140, 0, 60);
        }
    }
}
