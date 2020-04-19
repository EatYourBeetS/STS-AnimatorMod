package eatyourbeets.actions.unnamed;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

public class MoveToVoidAction extends EYBAction
{
    public MoveToVoidAction(AbstractCard card)
    {
        this(card, 3);
    }

    public MoveToVoidAction(AbstractCard card, int repeat)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_MED);

        this.card = card;

        Initialize(amount);
    }

    public void update()
    {
        player.hand.removeCard(card);
        player.limbo.removeCard(card);
        player.drawPile.removeCard(card);
        player.discardPile.removeCard(card);
        player.exhaustPile.removeCard(card);

        if (amount > 0)
        {
            GameActions.Bottom.Add(new MoveToVoidAction(card, amount - 1));
        }
        else
        {
            CombatStats.Void.Initialize(false);
            CombatStats.Void.addToBottom(card);

            UnnamedCard c = JavaUtilities.SafeCast(card, UnnamedCard.class);
            if (c != null)
            {
                c.OnEnteredVoid();
            }
        }

        this.isDone = true;
    }
}
