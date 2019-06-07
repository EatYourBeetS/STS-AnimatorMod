package eatyourbeets.monsters.UnnamedReign.UnnamedDoll;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BobEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.AnimatorMonster;
import eatyourbeets.monsters.Bosses.TheUnnamed;
import eatyourbeets.monsters.SharedMoveset.*;
import eatyourbeets.monsters.UnnamedReign.AbstractMonsterData;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset.Move_AttackVulnerableAndDexLoss;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset.Move_AttackWeakAndStrLoss;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.Moveset.Move_GainTempThornsAndRegenAll;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.UnnamedReign.CursedStabsPower;
import eatyourbeets.powers.UnnamedReign.UnnamedDollPower;

public class TheUnnamed_Doll extends AnimatorMonster
{
    public static final String ID = "Animator_TheUnnamed_Doll";

    private final BobEffect bobEffect = new BobEffect(1);

    private final TheUnnamed theUnnamed;

    public TheUnnamed_Doll(TheUnnamed theUnnamed, float x, float y)
    {
        super(new Data(ID), EnemyType.NORMAL, x, y);

        this.theUnnamed = theUnnamed;

        this.data.SetIdleAnimation(this, 1);

        int level = AbstractDungeon.ascensionLevel;

        moveset.AddNormal(new Move_ShieldAll(16 + (level / 10)));
        moveset.AddNormal(new Move_GainStrengthAndArtifactAll(3 + (level / 10), 1));
        moveset.AddNormal(new Move_GainTempThornsAndRegenAll(3, 4));
        moveset.AddNormal(new Move_AttackVulnerableAndDexLoss(1, 1));
        moveset.AddNormal(new Move_AttackWeakAndStrLoss(1, 1));
    }

    @Override
    public void init()
    {
        super.init();

        GameActionsHelper.ApplyPower(this, this, new CursedStabsPower(this));
        GameActionsHelper.ApplyPower(this, this, new UnnamedDollPower(this, 30), 30);
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

    protected static class Data extends AbstractMonsterData
    {
        public Data(String id)
        {
            super(id);

            if (PlayerStatistics.GetAscensionLevel() > 7)
            {
                maxHealth = 230;
            }
            else
            {
                maxHealth = 190;
            }

            atlasUrl = "images/monsters/Animator_TheUnnamed/TheUnnamedMinion.atlas";
            jsonUrl = "images/monsters/Animator_TheUnnamed/TheUnnamedMinion.json";
            scale = 2;

            SetHB(0,-20,120,140, 0, 60);
        }
    }
}
