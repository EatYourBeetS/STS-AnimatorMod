package eatyourbeets.blights.common;

import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.player.EndPlayerTurn;
import eatyourbeets.relics.animator.unnamedReign.TheWolleyCore;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class CustomTimeMaze extends AbstractBlight
{
    private static final BlightStrings blightStrings;

    public static final String ID = GR.Common.CreateID("TimeMaze");
    public static final String NAME;
    public static final String[] DESC;

    public int maxCardsPerTurn;
    private TheWolleyCore relic;

    public CustomTimeMaze(int maxCardsPerTurn)
    {
        super(ID, NAME, DESC[0] + maxCardsPerTurn + DESC[1], "maze.png", true);
        this.counter = maxCardsPerTurn;
        this.maxCardsPerTurn = maxCardsPerTurn;
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        relic = GameUtilities.GetRelic(TheWolleyCore.ID);
        if (relic != null)
        {
            relic.flash();
            this.flash();
            maxCardsPerTurn += relic.GetTimeMazeLimitIncrease();
        }

        updateDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public void updateDescription()
    {
        super.updateDescription();

        description = DESC[0] + maxCardsPerTurn + DESC[1];
        if (relic != null)
        {
            description += " NL " + relic.GetTimeMazeString();
        }
    }

    public void onPlayCard(AbstractCard card, AbstractMonster m)
    {
        if (this.counter < maxCardsPerTurn && card.type != CardType.CURSE)
        {
            ++this.counter;
            if (this.counter >= maxCardsPerTurn)
            {
                GameActions.Bottom.Add(new EndPlayerTurn());

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