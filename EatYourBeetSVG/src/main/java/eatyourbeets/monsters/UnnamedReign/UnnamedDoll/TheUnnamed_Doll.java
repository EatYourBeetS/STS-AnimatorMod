package eatyourbeets.monsters.UnnamedReign.UnnamedDoll;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.WraithFormPower;
import com.megacrit.cardcrawl.vfx.BobEffect;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset.Move_GainTempThornsAndBlockAll;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.monsters.AnimatorMonster;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.monsters.SharedMoveset.Move_AttackMultiple;
import eatyourbeets.monsters.SharedMoveset.Move_ShieldAll;
import eatyourbeets.monsters.AbstractMonsterData;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset.Move_AttackFrailAndDexLoss;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset.Move_AttackWeakAndStrLoss;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset.Move_GainRitualAndArtifactAll;
import eatyourbeets.powers.UnnamedReign.CursedStabsPower;
import eatyourbeets.powers.UnnamedReign.UnnamedDollPower;
import eatyourbeets.utilities.GameUtilities;

public class TheUnnamed_Doll extends AnimatorMonster
{
    public static final String ID = CreateFullID(TheUnnamed_Doll.class);

    private final BobEffect bobEffect = new BobEffect(1);

    private final TheUnnamed theUnnamed;
    private final Move_GainRitualAndArtifactAll ritualAndArtifactAll;


    public TheUnnamed_Doll(TheUnnamed theUnnamed, float x, float y)
    {
        super(new Data(ID), EnemyType.NORMAL, x, y);

        this.theUnnamed = theUnnamed;

        this.data.SetIdleAnimation(this, 1);

        moveset.AddSpecial(new Move_AttackMultiple(4, 12));

        ritualAndArtifactAll = (Move_GainRitualAndArtifactAll)
                moveset.AddSpecial(new Move_GainRitualAndArtifactAll(2, 2), 1);

        boolean asc18 = GameUtilities.GetAscensionLevel() >= 18;

        int debuffAmount = asc18 ? 2 : 1;
        int tempThorns = asc18 ? 3 : 2;

        moveset.AddNormal(new Move_ShieldAll(16));
        moveset.AddNormal(new Move_GainTempThornsAndBlockAll(tempThorns, 9));
        moveset.AddNormal(new Move_AttackFrailAndDexLoss(1, debuffAmount));
        moveset.AddNormal(new Move_AttackWeakAndStrLoss(1, debuffAmount));
    }

    @Override
    public void init()
    {
        super.init();

        GameActions.Bottom.ApplyPower(this, this, new CursedStabsPower(this));
        GameActions.Bottom.ApplyPower(this, this, new UnnamedDollPower(this));
        GameActions.Bottom.GainBlock(this, 26 + (int)(GameUtilities.GetActualAscensionLevel() * 0.66f));
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
        else if (GameUtilities.GetPowerAmount(IntangiblePlayerPower.POWER_ID) >= 2 && !AbstractDungeon.player.hasPower(WraithFormPower.POWER_ID))
        {
            moveset.GetMove(Move_AttackMultiple.class).SetMove();
        }
        else
        {
            super.SetNextMove(roll, historySize, previousMove);
        }
    }

    protected static class Data extends AbstractMonsterData
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
