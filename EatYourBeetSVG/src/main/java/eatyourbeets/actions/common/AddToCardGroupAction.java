package eatyourbeets.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.utilities.GameActionsHelper;
import patches.AbstractEnums;

import java.util.ArrayList;

public class AddToCardGroupAction extends AbstractGameAction
{
    private final CardGroup group;
    private final ArrayList<AbstractCard> cards;

    public AddToCardGroupAction(AbstractCard card, CardGroup group)
    {
        this.cards = new ArrayList<>();
        this.cards.add(card);

        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.group = group;
    }

    public AddToCardGroupAction(ArrayList<AbstractCard> cards, CardGroup group)
    {
        this.cards = cards;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.group = group;
    }

    public void update()
    {
        group.group.addAll(cards);
        this.isDone = true;
    }
}
