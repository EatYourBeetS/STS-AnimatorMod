package eatyourbeets.monsters.UnnamedReign.UnnamedCultist;

import com.megacrit.cardcrawl.cards.status.VoidCard;
import eatyourbeets.actions.monsters.SummonMonsterAction;
import eatyourbeets.actions.special.SendMinionsAway;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.monsters.SharedMoveset.EYBMove_Unknown;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.TheUnnamed_Doll;
import eatyourbeets.powers.monsters.TheUnnamedCultistPower;
import eatyourbeets.utilities.GameActions;

public class TheUnnamed_Cultist_DollSummoner extends TheUnnamed_Cultist
{
    private final EYBAbstractMove moveSummonEnemy;
    private final EYBAbstractMove moveTalk;

    public TheUnnamed_Cultist_DollSummoner(float x, float y)
    {
        super(x, y);

        moveTalk = moveset.AddSpecial(new EYBMove_Unknown())
        .SetOnUse((m, t) -> GameActions.Bottom.Talk(this, STRINGS.DIALOG[m.misc.Calculate()]));

        moveSummonEnemy = moveset.Special.Add(new EYBMove_Unknown())
        .SetUses(1)
        .SetOnUse((m, t) ->
        {
            GameActions.Bottom.Talk(this, STRINGS.DIALOG[1], 0.5f, 2f);
            GameActions.Bottom.WaitRealtime(2f);
            GameActions.Bottom.Add(new SummonMonsterAction(new TheUnnamed_Doll(null, -40, 135), true));
        });

        //Rotation:
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
        GameActions.Bottom.Talk(this, STRINGS.DIALOG[5], 1f, 2.5f);
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
                moveTalk.SetMisc(6);
                moveTalk.Select();
            }
            else if (historySize == 1)
            {
                moveTalk.SetMisc(8);
                moveTalk.Select();
            }
            else
            {
                moveSummonEnemy.Select();
            }

            return;
        }

        super.SetNextMove(roll, historySize, previousMove);
    }
}
