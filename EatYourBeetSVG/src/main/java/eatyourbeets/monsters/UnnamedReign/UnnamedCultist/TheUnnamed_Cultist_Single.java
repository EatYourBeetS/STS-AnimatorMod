package eatyourbeets.monsters.UnnamedReign.UnnamedCultist;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.OrbWalker;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.city.ShelledParasite;
import com.megacrit.cardcrawl.monsters.exordium.GremlinWarrior;
import com.megacrit.cardcrawl.monsters.exordium.Sentry;
import eatyourbeets.actions.monsters.SummonMonsterAction;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.monsters.SharedMoveset.EYBMove_Unknown;
import eatyourbeets.monsters.UnnamedReign.Shapes.Cube.DarkCube;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.monsters.TheUnnamedCultistPower;
import eatyourbeets.utilities.*;

public class TheUnnamed_Cultist_Single extends TheUnnamed_Cultist
{
    protected final RandomizedList<AbstractMonster> pool1 = new RandomizedList<>();
    protected final RandomizedList<AbstractMonster> pool2 = new RandomizedList<>();
    protected final EYBAbstractMove moveSummonEnemy;
    protected AbstractMonster firstSummon;
    protected AbstractMonster secondSummon;

    public TheUnnamed_Cultist_Single(float x, float y)
    {
        super(x, y);

        PreparePool(GameUtilities.GetAscensionLevel());

        moveSummonEnemy = moveset.Special.Add(new EYBMove_Unknown()
        .SetOnUse((m, t) ->
        {
            AbstractMonster summon = JavaUtilities.SafeCast(m.data, AbstractMonster.class);
            if (summon != null)
            {
                if (summon.id.equals(ShelledParasite.ID))
                {
                    GameActions.Bottom.Talk(this, STRINGS.DIALOG[4], 2f, 3f);
                }
                else if (summon.id.equals(GremlinWarrior.ID))
                {
                    GameActions.Bottom.Talk(this, STRINGS.DIALOG[3], 2f, 3f);
                }
                else
                {
                    GameActions.Bottom.Talk(this, JavaUtilities.Format(STRINGS.DIALOG[2], summon.name), 2f, 3f);
                }

                GameActions.Bottom.Add(new SummonMonsterAction(summon, false));
            }
        }));

        //Rotation:
        moveset.Normal.Buff(PowerHelper.PlatedArmor, 4)
        .SetPowerTarget(PowerTarget.Enemies)
        .SetMiscBonus(4, 1);

        moveset.Normal.DefendBuff(8, PowerHelper.Strength, 3)
        .SetPowerTarget(PowerTarget.Enemies)
        .SetMiscBonus(4, 1)
        .SetBlockAoE(true);

        moveset.Normal.AttackDefend(12, 12)
        .SetDamageScaling(0.25f)
        .SetBlockScaling(0.25f);
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActions.Bottom.StackPower(new TheUnnamedCultistPower(this, 15));
        GameActions.Bottom.Talk(this, STRINGS.DIALOG[10], 1f, 3f);
    }

    @Override
    protected void SetNextMove(int roll, int historySize, Byte previousMove)
    {
        if (historySize % 3 == 0 && TrySummon())
        {
            return;
        }

        super.SetNextMove(roll, historySize, previousMove);
    }

    private boolean TrySummon()
    {
        if (firstSummon == null || firstSummon.isDeadOrEscaped())
        {
            firstSummon = pool1.Retrieve(AbstractDungeon.aiRng);
            if (firstSummon != null)
            {
                moveSummonEnemy.SetData(firstSummon);
                moveSummonEnemy.Select();
                return true;
            }
        }

        if (secondSummon == null || secondSummon.isDeadOrEscaped())
        {
            secondSummon = pool2.Retrieve(AbstractDungeon.aiRng);
            if (secondSummon != null)
            {
                moveSummonEnemy.SetData(secondSummon);
                moveSummonEnemy.Select();
                return true;
            }
        }

        return false;
    }

    private void PreparePool(int level)
    {
        AbstractMonster m;
        m = new Byrd(-80, 24);
        m.currentHealth = m.maxHealth += 24 + level / 2;
        pool1.Add(m);

        m = new Sentry(-80, 24);
        m.currentHealth = m.maxHealth += 28 + level / 2;
        pool1.Add(m);

        m = new GremlinWarrior(-80, 24);
        m.currentHealth = m.maxHealth += 34 + level / 2;
        pool1.Add(m);

        m = new OrbWalker(-330, -26);
        m.currentHealth = m.maxHealth += 20 + level / 2;
        pool2.Add(m);

        m = new ShelledParasite(-352, -26);
        m.currentHealth = m.maxHealth += 20 + level / 2;
        pool2.Add(m);

        if (level >= 20)
        {
            m = new DarkCube(MonsterTier.Normal, -330, 2);
            m.currentHealth = m.maxHealth += -30 + level / 2;
            pool2.Add(m);
        }
    }
}