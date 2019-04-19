package eatyourbeets.actions;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.AnimatorResources;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class VariableExhaustAction extends AnimatorAction
{
    public final boolean canPickZero;
    public final boolean anyNumber;

    private final Object state;
    private final ArrayList<AbstractCard> exhausted;
    private final BiConsumer<Object, ArrayList<AbstractCard>> onExhaust;

    public VariableExhaustAction(AbstractPlayer player, int exhaust, Object state, BiConsumer<Object, ArrayList<AbstractCard>> onExhaust)
    {
        this.canPickZero = true;
        this.anyNumber = true;
        this.exhausted = new ArrayList<>();
        this.state = state;
        this.onExhaust = onExhaust;
        this.target = player;
        this.amount = exhaust;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.DISCARD;
    }

    public void update()
    {
        AbstractPlayer p = (AbstractPlayer) target;
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            if (p.hand.size() == 0)
            {
                exhausted.clear();
                this.isDone = true;
            }
            else
            {
                String discardMessage = AnimatorResources.GetUIStrings(AnimatorResources.UIStringType.Actions).TEXT[2];
                AbstractDungeon.handCardSelectScreen.open(discardMessage, this.amount, this.anyNumber, this.canPickZero);
            }
        }
        else if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved)
        {
            for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group)
            {
                p.hand.moveToExhaustPile(card);
                AbstractDungeon.player.hand.applyPowers();

                exhausted.add(card);
            }

            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }

        this.tickDuration();

        if (this.isDone)
        {
            onExhaust.accept(state, exhausted);
        }
    }
}
