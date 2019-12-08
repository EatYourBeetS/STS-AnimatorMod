package eatyourbeets.actions.orbs;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameUtilities;

public class FireOrbEvokeAction extends AbstractGameAction
{
    private final AbstractPlayer p;

    public FireOrbEvokeAction(int orbDamage)
    {
        this.p = AbstractDungeon.player;
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
        this.amount = orbDamage;
    }

    public void update()
    {
        if (this.amount > 0)
        {
            for (AbstractMonster m : GameUtilities.GetCurrentEnemies(true))
            {
                GameActionsHelper.ApplyPower(p, m, new BurningPower(m, p, this.amount), this.amount);
            }
        }

        this.isDone = true;
    }
}
