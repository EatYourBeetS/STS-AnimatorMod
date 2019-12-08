package eatyourbeets.actions._legacy.unnamed;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.UnnamedCard;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.JavaUtilities;

public class MoveToVoidAction extends AbstractGameAction
{
    private final int repeat;
    private final AbstractCard card;

    public MoveToVoidAction(AbstractCard card)
    {
        this(card, 3);
    }

    public MoveToVoidAction(AbstractCard card, int repeat)
    {
        this.card = card;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.repeat = repeat;
    }

    public void update()
    {
        AbstractPlayer p = AbstractDungeon.player;

        p.hand.removeCard(card);
        p.limbo.removeCard(card);
        p.drawPile.removeCard(card);
        p.discardPile.removeCard(card);
        p.exhaustPile.removeCard(card);

        if (repeat > 0)
        {
            GameActionsHelper.AddToBottom(new MoveToVoidAction(card, repeat - 1));
        }
        else
        {
            PlayerStatistics.Void.Initialize(false);
            PlayerStatistics.Void.addToBottom(card);

            UnnamedCard c = JavaUtilities.SafeCast(card, UnnamedCard.class);
            if (c != null)
            {
                c.OnEnteredVoid();
            }
        }

        this.isDone = true;
    }
}
