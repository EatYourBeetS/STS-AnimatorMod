package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.animator.Entoma;

public class EntomaAction extends AnimatorAction
{
    private final Entoma entoma;

    public EntomaAction(Entoma entoma)
    {
        this.entoma = entoma;
        this.actionType = ActionType.SPECIAL;
        this.duration = 0.1F;
    }

    public void update()
    {
        AbstractDungeon.player.increaseMaxHp(2, false);
        for (AbstractCard c : entoma.GetAllInstances())
        {
            Entoma card = (Entoma)c;
            card.upgrade();
//            card.secondaryValue += 1;
//            card.baseSecondaryValue = card.secondaryValue;
//            card.applyPowers();
        }

        this.isDone = true;
    }
}
