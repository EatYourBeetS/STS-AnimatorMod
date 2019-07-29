package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
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
            AbstractRoom room = AbstractDungeon.getCurrRoom();
            if (!monster.hasPower(MinionPower.POWER_ID) && room instanceof MonsterRoomElite || room instanceof MonsterRoomBoss)
            {
                krul.ObtainReward();
            }
        }

        this.isDone = true;
    }
}
