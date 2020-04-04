package eatyourbeets.monsters.Elites.Moveset;

import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.powers.common.TemporaryConfusionPower;
import eatyourbeets.utilities.GameActions;

public class Move_TemporaryConfusion extends EYBAbstractMove
{
    public void Select()
    {
        owner.setMove(id, AbstractMonster.Intent.STRONG_DEBUFF);
    }

    public void QueueActions(AbstractCreature target)
    {
        GameActions.Bottom.StackPower(owner, new TemporaryConfusionPower(target));
        GameActions.Bottom.MakeCardInDrawPile(new Dazed());
    }
}