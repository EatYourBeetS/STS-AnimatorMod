package eatyourbeets.actions._legacy.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class VariableDiscardAction extends AbstractGameAction
{
    private final AbstractCard cardSource;
    private final boolean anyNumber;
    private final Object state;
    private final BiConsumer<Object, ArrayList<AbstractCard>> onDiscard;

    public VariableDiscardAction(AbstractCard cardSource, AbstractPlayer player, int discard, Object state, BiConsumer<Object, ArrayList<AbstractCard>> onDiscard)
    {
        this(cardSource, player, discard, state, onDiscard, true);
    }

    public VariableDiscardAction(AbstractCard cardSource, AbstractPlayer player, int discard, Object state, BiConsumer<Object, ArrayList<AbstractCard>> onDiscard, boolean anyNumber)
    {
        this.cardSource = cardSource;
        this.state = state;
        this.onDiscard = onDiscard;
        this.target = player;
        this.amount = discard;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.DISCARD;
        this.anyNumber = anyNumber;
    }

    public void update()
    {
        AbstractPlayer p = (AbstractPlayer)target;
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            if (p.hand.size() == 0 || this.amount == 0)
            {
                this.isDone = true;
            }
            else
            {
                String discardMessage = DiscardAction.TEXT[0];
                if (cardSource != null)
                {
                    discardMessage += " (" + cardSource.name + ")";
                }

                AbstractDungeon.handCardSelectScreen.open(discardMessage, this.amount, anyNumber, anyNumber);
                this.tickDuration();
            }

            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved)
        {
            ArrayList<AbstractCard> discarded = new ArrayList<>();
            for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group)
            {
                p.hand.moveToDiscardPile(card);
                card.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(false);
                AbstractDungeon.player.hand.applyPowers();

                discarded.add(card);
            }

            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;

            onDiscard.accept(state, discarded);
        }

        this.tickDuration();
    }
}
