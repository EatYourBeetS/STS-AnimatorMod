package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

public class PlayCardFromPile extends EYBActionWithCallback<AbstractMonster>
{
    private CardGroup sourcePile;
    private boolean exhaust;
    private boolean purge;

    public PlayCardFromPile(AbstractCard card, CardGroup sourcePile)
    {
        this(card, sourcePile, null);
    }

    public PlayCardFromPile(AbstractCard card, CardGroup sourcePile, AbstractMonster target)
    {
        super(ActionType.WAIT, Settings.ACTION_DUR_FAST);

        this.card = card;
        this.sourcePile = sourcePile;

        Initialize(player, target, 1);
    }

    public PlayCardFromPile SetExhaust(boolean exhaust)
    {
        this.exhaust = exhaust;
        this.purge = false;

        return this;
    }

    public PlayCardFromPile SetPurge(boolean purge)
    {
        this.exhaust = false;
        this.purge = purge;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (sourcePile == null)
        {
            sourcePile = GameUtilities.FindCardGroup(card, false);

            if (sourcePile == null)
            {
                JavaUtilities.GetLogger(getClass()).warn("Could not find card source.");
                Complete();
                return;
            }
        }

        if (sourcePile.size() == 0 || !sourcePile.contains(card))
        {
            Complete();
        }
        else
        {
            sourcePile.removeCard(card);

            if (target == null)
            {
                target = GameUtilities.GetRandomEnemy(true);
            }

            GameUtilities.PlayCard(card, (AbstractMonster) target, purge, exhaust);
        }
    }

    @Override
    protected void UpdateInternal()
    {
        super.UpdateInternal();

        if (isDone)
        {
            Complete((AbstractMonster) target);
        }
    }
}