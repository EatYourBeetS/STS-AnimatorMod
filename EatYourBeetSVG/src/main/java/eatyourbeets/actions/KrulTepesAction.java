package eatyourbeets.actions;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.KrulTepes;

public class KrulTepesAction extends AnimatorAction
{
    private final KrulTepes krul;

    public KrulTepesAction(AbstractCreature target, KrulTepes krul)
    {
        this.target = target;
        this.krul = krul;
        this.actionType = ActionType.SPECIAL;
        this.duration = 0.1F;
    }

    public void update()
    {
        AbstractMonster monster = ((AbstractMonster)this.target);

//        if (!monster.hasPower(RegrowPower.POWER_ID))
//        {
//            GameActionsHelper.GainEnergy(1);
//            AbstractDungeon.player.heal(5, true);
//        }

        if (krul.CanGetReward())
        {
            if ((monster.type == AbstractMonster.EnemyType.ELITE || monster.type == AbstractMonster.EnemyType.BOSS))
            {
                krul.ObtainReward();
            }
        }

        this.isDone = true;
    }
}
