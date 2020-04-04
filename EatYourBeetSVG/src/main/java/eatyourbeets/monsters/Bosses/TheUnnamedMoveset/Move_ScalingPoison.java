package eatyourbeets.monsters.Bosses.TheUnnamedMoveset;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.EYBAbstractMove;

public class Move_ScalingPoison extends EYBAbstractMove
{
    public int times;
    public int amount;

    public Move_ScalingPoison(int amount, int times)
    {
        this.amount = amount;
        this.times = times;
    }

    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.STRONG_DEBUFF);
    }

    public void QueueActions(AbstractCreature target)
    {
        for (int i = 0; i < times; i++)
        {
            GameActions.Bottom.VFX(new PotionBounceEffect(target.hb.cX + MathUtils.random(-5, 5),
                    target.hb.cY + MathUtils.random(-5, 5), target.hb.cX, target.hb.cY), 0.4f);

            GameActions.Bottom.ApplyPower(owner, target, new PoisonPower(target, owner, amount), amount);
            GameActions.Bottom.WaitRealtime(0.1f);
        }

        this.amount += 1;
    }
}