package eatyourbeets.monsters.UnnamedReign.UnnamedCultist;

import com.megacrit.cardcrawl.cards.status.VoidCard;
import eatyourbeets.actions.special.SendMinionsAway;
import eatyourbeets.monsters.SharedMoveset_Old.Move_Talk;
import eatyourbeets.monsters.UnnamedReign.UnnamedCultist.Moveset.Move_SummonEnemy;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.TheUnnamed_Doll;
import eatyourbeets.powers.monsters.TheUnnamedCultistPower;
import eatyourbeets.utilities.GameActions;

public class TheUnnamed_Cultist_DollSummoner extends TheUnnamed_Cultist
{
    private final Move_SummonEnemy moveSummonEnemy;
    private final Move_Talk moveTalk;

    public TheUnnamed_Cultist_DollSummoner(float x, float y)
    {
        super(x, y);

        moveTalk = moveset.AddSpecial(new Move_Talk());
        moveSummonEnemy = moveset.AddSpecial(new Move_SummonEnemy());

        moveset.Normal.AttackDefend(12, 12)
        .SetDamageScaling(0.25f)
        .SetBlockScaling(0.25f);

        moveset.Normal.Attack(9, 2)
        .SetDamageScaling(0.2f);

        moveset.Normal.ShuffleCard(new VoidCard(), 3);
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActions.Bottom.ApplyPower(this, this, new TheUnnamedCultistPower(this, 12), 12);
        GameActions.Bottom.WaitRealtime(0.2f);
        GameActions.Bottom.Talk(this, data.strings.DIALOG[5], 1f, 2.5f);
    }

    @Override
    public void die()
    {
        super.die();

        GameActions.Bottom.Add(new SendMinionsAway());
    }

    @Override
    protected void SetNextMove(int roll, int historySize, Byte previousMove)
    {
        if (moveSummonEnemy.CanUse(previousMove))
        {
            if (historySize == 0)
            {
                moveTalk.SetLine(data.strings.DIALOG[6]);
                moveTalk.Select();
            }
            else if (historySize == 1)
            {
                moveTalk.SetLine(data.strings.DIALOG[8]);
                moveTalk.Select();
            }
            else
            {
                moveSummonEnemy.SetSummon(new TheUnnamed_Doll(null, -40, 135));
                moveSummonEnemy.Select();
            }

            return;
        }

        super.SetNextMove(roll, historySize, previousMove);
    }
}
