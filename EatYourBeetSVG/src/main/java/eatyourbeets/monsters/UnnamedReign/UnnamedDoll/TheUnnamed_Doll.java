package eatyourbeets.monsters.UnnamedReign.UnnamedDoll;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.vfx.BobEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.monsters.AnimatorMonster;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.monsters.SharedMoveset.Move_AttackMultiple;
import eatyourbeets.monsters.SharedMoveset.Move_ShieldAll;
import eatyourbeets.monsters.UnnamedReign.AbstractMonsterData;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset.Move_AttackFrailAndDexLoss;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset.Move_AttackWeakAndStrLoss;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset.Move_GainRitualAndArtifactAll;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.UnnamedReign.CursedStabsPower;
import eatyourbeets.powers.UnnamedReign.UnnamedDollPower;

public class TheUnnamed_Doll extends AnimatorMonster
{
    public static final String ID = "Animator_TheUnnamed_Doll";

    private final BobEffect bobEffect = new BobEffect(1);

    private final TheUnnamed theUnnamed;
    private Move_GainRitualAndArtifactAll ritualAndArtifactAll;


    public TheUnnamed_Doll(TheUnnamed theUnnamed, float x, float y)
    {
        super(new Data(ID), EnemyType.NORMAL, x, y);

        this.theUnnamed = theUnnamed;

        this.data.SetIdleAnimation(this, 1);

        int level = AbstractDungeon.ascensionLevel;

        moveset.AddSpecial(new Move_AttackMultiple(4, 12));

        ritualAndArtifactAll = (Move_GainRitualAndArtifactAll)
                moveset.AddSpecial(new Move_GainRitualAndArtifactAll(2, 2), 1);

        moveset.AddNormal(new Move_ShieldAll(16));
        moveset.AddNormal(new Move_AttackFrailAndDexLoss(1, 1));
        moveset.AddNormal(new Move_AttackWeakAndStrLoss(1, 1));
    }

    @Override
    public void init()
    {
        super.init();

        GameActionsHelper.ApplyPower(this, this, new CursedStabsPower(this));
        if (PlayerStatistics.GetAscensionLevel() >= 7)
        {
            GameActionsHelper.ApplyPower(this, this, new UnnamedDollPower(this, 4), 4);
        }
        else
        {
            GameActionsHelper.ApplyPower(this, this, new UnnamedDollPower(this, 4), 3);
        }
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
    protected void ExecuteNextMove()
    {
        AbstractMove move = moveset.GetMove(nextMove);
        if (move instanceof Move_AttackMultiple)
        {
            AbstractPower cs = getPower(CursedStabsPower.POWER_ID);
            if (cs != null)
            {
                ((CursedStabsPower)cs).usesThisTurn = 1;
            }
        }

        move.Execute(AbstractDungeon.player);
    }

    @Override
    protected void SetNextMove(int roll, int historySize, Byte previousMove)
    {
        if (historySize >= 2 && ritualAndArtifactAll.CanUse(previousMove))
        {
            ritualAndArtifactAll.SetMove();
        }
        else
        {
            AbstractPower power = AbstractDungeon.player.getPower(IntangiblePlayerPower.POWER_ID);
            if (power != null && power.amount > 1)
            {
                moveset.GetMove(Move_AttackMultiple.class).SetMove();
            }
            else
            {
                super.SetNextMove(roll, historySize, previousMove);
            }
        }
    }

    protected static class Data extends AbstractMonsterData
    {
        public Data(String id)
        {
            super(id);

            if (PlayerStatistics.GetAscensionLevel() > 7)
            {
                maxHealth = 211;
            }
            else
            {
                maxHealth = 189;
            }

            atlasUrl = "images/monsters/Animator_TheUnnamed/TheUnnamedMinion.atlas";
            jsonUrl = "images/monsters/Animator_TheUnnamed/TheUnnamedMinion.json";
            scale = 2;

            SetHB(0,-20,120,140, 0, 60);
        }
    }
}
