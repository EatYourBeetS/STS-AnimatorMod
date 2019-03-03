package eatyourbeets.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.ui.GridCardSelectScreenPatch;

import java.util.function.Consumer;
import java.util.function.Function;

public class OptionalNumberAction extends AbstractGameAction
{
    private final AbstractCard card;
    private final String bottomMessage;
    private final Function<Integer, String> updateCurrentText;
    private final Consumer<AbstractCard> onEffectSelected;

    public OptionalNumberAction(AbstractCard card, String bottomMessage, Function<Integer, String> updateCurrentText, Consumer<AbstractCard> onEffectSelected)
    {
        this.updateCurrentText = updateCurrentText;
        this.onEffectSelected = onEffectSelected;
        this.bottomMessage = bottomMessage;
        this.card = card;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.SPECIAL;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            group.addToTop(card);
            AbstractDungeon.gridSelectScreen.open(group, 1, bottomMessage, false, false, false, false);
            GridCardSelectScreenPatch.Load(card, updateCurrentText);
        }

        if (AbstractDungeon.gridSelectScreen.selectedCards.size() > 0)
        {
            AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            onEffectSelected.accept(card);
        }

        this.tickDuration();
    }
}
