package eatyourbeets.actions.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RandomCostReductionAction extends AbstractGameAction
{
    private final AbstractPlayer p;
    private final int costReduction;
    private final boolean permanent;

    public RandomCostReductionAction(int costReduction, boolean permanent)
    {
        this.permanent = permanent;
        this.costReduction = costReduction;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            boolean betterPossible = false;
            boolean possible = false;

            for (AbstractCard c : this.p.hand.group)
            {
                if (c.costForTurn > 0)
                {
                    betterPossible = true;
                }
                else if (c.cost > 0)
                {
                    possible = true;
                }
            }

            if (betterPossible || possible)
            {
                this.findAndModifyCard(betterPossible);
            }
        }

        this.tickDuration();
    }

    private void findAndModifyCard(boolean better)
    {
        AbstractCard c = this.p.hand.getRandomCard(false);
        if (better)
        {
            if (c.costForTurn > 0)
            {
                if (permanent)
                {
                    c.updateCost(Math.max(0, c.cost - costReduction));
                }
                else
                {
                    c.setCostForTurn(Math.max(0, c.costForTurn - costReduction));
                }

                c.superFlash(Color.GOLD.cpy());
            }
            else
            {
                this.findAndModifyCard(true);
            }
        }
        else if (c.cost > 0)
        {
            if (permanent)
            {
                c.updateCost(Math.max(0, c.cost - costReduction));
            }
            else
            {
                c.setCostForTurn(Math.max(0, c.costForTurn - costReduction));
            }

            c.superFlash(Color.GOLD.cpy());
        }
        else
        {
            this.findAndModifyCard(false);
        }

    }
}

