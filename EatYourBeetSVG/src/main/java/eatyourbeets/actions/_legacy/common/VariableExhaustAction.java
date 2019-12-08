package eatyourbeets.actions._legacy.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class VariableExhaustAction extends AbstractGameAction
{
    private static final UIStrings uiStrings;
    private static final String[]  TEXT;

    public final boolean canPickZero;
    public final boolean anyNumber;

    private final String sourceName;
    private final Object state;
    private final ArrayList<AbstractCard> exhausted;
    private final BiConsumer<Object, ArrayList<AbstractCard>> onExhaust;

    public VariableExhaustAction(String sourceName, int amount, Object state, BiConsumer<Object, ArrayList<AbstractCard>> onExhaust)
    {
        this.sourceName = sourceName;
        this.canPickZero = true;
        this.anyNumber = true;
        this.exhausted = new ArrayList<>();
        this.state = state;
        this.onExhaust = onExhaust;
        this.target = AbstractDungeon.player;
        this.amount = amount;
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
                String message = TEXT[0];
                if (sourceName != null)
                {
                    message += " (" + sourceName + ")";
                }

                AbstractDungeon.handCardSelectScreen.open(message, this.amount, this.anyNumber, this.canPickZero);
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

    static
    {
        uiStrings = CardCrawlGame.languagePack.getUIString("ExhaustAction");
        TEXT = uiStrings.TEXT;
    }
}
