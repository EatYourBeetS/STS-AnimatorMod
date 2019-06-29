package eatyourbeets.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import eatyourbeets.ui.GridCardSelectScreenPatch;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class ChooseFromAnyPileAction extends AbstractGameAction
{
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    private final String message;
    private final ArrayList<AbstractCard> selectedCards;
    private final BiConsumer<Object, ArrayList<AbstractCard>> onCompletion;
    private final Object state;

    private final CardGroup fakeHandGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    public ChooseFromAnyPileAction(int amount, BiConsumer<Object, ArrayList<AbstractCard>> onCompletion, Object state, String message)
    {
        this.message = message;
        this.selectedCards = new ArrayList<>();
        this.onCompletion = onCompletion;
        this.state = state;

        this.setValues(AbstractDungeon.player, AbstractDungeon.player, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_MED)
        {
            AbstractPlayer p = AbstractDungeon.player;

            fakeHandGroup.group.addAll(p.hand.group);
            p.hand.clear();

            GridCardSelectScreenPatch.AddGroup(fakeHandGroup);
            GridCardSelectScreenPatch.AddGroup(p.drawPile);
            GridCardSelectScreenPatch.AddGroup(p.discardPile);

            CardGroup group = GridCardSelectScreenPatch.GetCardGroup();
            if (group.isEmpty())
            {
                this.isDone = true;
                return;
            }

            AbstractDungeon.gridSelectScreen.open(group, this.amount, message,
                    false, false, false, false);

            this.tickDuration();
        }
        else
        {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0)
            {
                selectedCards.addAll(AbstractDungeon.gridSelectScreen.selectedCards);

                GridCardSelectScreenPatch.Clear();
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.group.addAll(fakeHandGroup.group);

                onCompletion.accept(state, selectedCards);
            }

            this.tickDuration();
        }
    }

    static
    {
        uiStrings = CardCrawlGame.languagePack.getUIString("ExhaustAction");
        TEXT = uiStrings.TEXT;
    }
}
