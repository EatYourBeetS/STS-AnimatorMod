package eatyourbeets.blights;

import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.EndPlayerTurnAction;
import eatyourbeets.utilities.GameActionsHelper;

public class CustomTimeMaze extends AbstractBlight
{
    public static final String ID = "animator:TimeMaze";
    private static final BlightStrings blightStrings;
    public static final String NAME;
    public static final String[] DESC;

    public int maxCardsPerTurn;

    public CustomTimeMaze(int maxCardsPerTurn)
    {
        super(ID, NAME, DESC[0] + maxCardsPerTurn + DESC[1], "maze.png", true);
        this.counter = maxCardsPerTurn;
        this.maxCardsPerTurn = maxCardsPerTurn;
    }

    public void onPlayCard(AbstractCard card, AbstractMonster m)
    {
        if (this.counter < maxCardsPerTurn && card.type != CardType.CURSE)
        {
            ++this.counter;
            if (this.counter >= maxCardsPerTurn)
            {
                GameActionsHelper.AddToBottom(new EndPlayerTurnAction());
                this.flash();
            }
        }
    }

    public boolean canPlay(AbstractCard card)
    {
        if (this.counter >= maxCardsPerTurn && card.type != CardType.CURSE)
        {
            card.cantUseMessage = DESC[2] + maxCardsPerTurn + DESC[1];
            return false;
        }
        else
        {
            return true;
        }
    }

    public void onVictory()
    {
        this.counter = -1;
    }

    public void atBattleStart()
    {
        this.counter = 0;
    }

    public void atTurnStart()
    {
        this.counter = 0;
    }

    static
    {
        blightStrings = CardCrawlGame.languagePack.getBlightString("TimeMaze");
        NAME = blightStrings.NAME;
        DESC = blightStrings.DESCRIPTION;
    }
}