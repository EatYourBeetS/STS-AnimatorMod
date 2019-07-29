package eatyourbeets.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.utilities.GameActionsHelper;
import patches.AbstractEnums;

public class AddToCardGroupAction extends AbstractGameAction
{
    private final CardGroup group;
    private final AbstractCard card;

    public AddToCardGroupAction(AbstractCard card, CardGroup group)
    {
        this.card = card;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.group = group;
    }

    public void update()
    {
        group.addToTop(card);
        this.isDone = true;
    }
}
