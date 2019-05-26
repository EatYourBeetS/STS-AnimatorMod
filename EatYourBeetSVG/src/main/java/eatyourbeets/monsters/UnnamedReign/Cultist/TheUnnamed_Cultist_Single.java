package eatyourbeets.monsters.UnnamedReign.Cultist;

import basemod.DevConsole;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.OrbWalker;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.city.ShelledParasite;
import com.megacrit.cardcrawl.monsters.exordium.GremlinWarrior;
import com.megacrit.cardcrawl.monsters.exordium.GremlinWizard;
import com.megacrit.cardcrawl.monsters.exordium.Sentry;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.SummonMonsterAction;
import eatyourbeets.actions.WaitRealtimeAction;
import eatyourbeets.misc.RandomizedList;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.monsters.UnnamedReign.Cultist.Moveset.Move_GuardedAttack;
import eatyourbeets.monsters.UnnamedReign.Cultist.Moveset.Move_SummonEnemy;
import eatyourbeets.monsters.UnnamedReign.Cultist.Moveset.Move_Talk;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.TheUnnamed_Doll;
import eatyourbeets.powers.UnnamedReign.TheUnnamedCultistPower;

import java.util.ArrayList;

public class TheUnnamed_Cultist_Single extends TheUnnamed_Cultist
{
    private final ArrayList<AbstractMove> moveset = new ArrayList<>();

    private Move_SummonEnemy moveSummonEnemy;

    public RandomizedList<AbstractMonster> pool1 = new RandomizedList<>();
    public RandomizedList<AbstractMonster> pool2 = new RandomizedList<>();
    public AbstractMonster firstSummon;
    public AbstractMonster secondSummon;

    public TheUnnamed_Cultist_Single(float x, float y)
    {
        super(x, y);

        int level = AbstractDungeon.ascensionLevel;

        PreparePool(level);

        moveSummonEnemy = new Move_SummonEnemy(0, level, this);
        moveset.add(moveSummonEnemy);
        moveset.add(new Move_GuardedAttack(1, 8, 10, this));
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActionsHelper.ApplyPower(this, this, new TheUnnamedCultistPower(this, 20), 20);
        GameActionsHelper.AddToBottom(new TalkAction(this, "I am a cultist.", 1f, 3f));
    }

    @Override
    public void takeTurn()
    {
        moveset.get(nextMove).Execute(AbstractDungeon.player);

        GameActionsHelper.AddToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i)
    {
        int history = moveHistory.size();

        if (history % 3 == 0)
        {
            if (firstSummon == null || firstSummon.isDeadOrEscaped())
            {
                firstSummon = pool1.Retrieve(AbstractDungeon.aiRng);
                if (firstSummon != null)
                {
                    moveSummonEnemy.SetSummon(firstSummon);
                    moveSummonEnemy.SetMove();
                    return;
                }
            }

            if (secondSummon == null || secondSummon.isDeadOrEscaped())
            {
                secondSummon = pool2.Retrieve(AbstractDungeon.aiRng);
                if (secondSummon != null)
                {
                    moveSummonEnemy.SetSummon(secondSummon);
                    moveSummonEnemy.SetMove();
                    return;
                }
            }
        }

        moveset.get(1).SetMove();
    }

    private void PreparePool(int level)
    {
        AbstractMonster m;
        m = new Byrd(-100, 10);
        m.currentHealth = m.maxHealth += 24 + level/2;
        pool1.Add(m);

        m = new Sentry(-100,10);
        m.currentHealth = m.maxHealth += 28 + level/2;
        pool1.Add(m);

        m = new GremlinWarrior(-100,10);
        m.currentHealth = m.maxHealth += 24 + level/2;
        pool1.Add(m);

        m = new OrbWalker(-320,-20);
        m.currentHealth = m.maxHealth += 20 + level/2;
        pool2.Add(m);

        m = new ShelledParasite(-320,-15);
        m.currentHealth = m.maxHealth += 20 + level/2;
        pool2.Add(m);
    }
}