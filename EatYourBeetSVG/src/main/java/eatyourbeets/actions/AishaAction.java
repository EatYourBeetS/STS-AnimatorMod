package eatyourbeets.actions;

import com.megacrit.cardcrawl.actions.common.ReduceCostAction;
import com.megacrit.cardcrawl.actions.unique.SkillFromDeckToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.misc.RandomizedList;

public class AishaAction extends AnimatorAction
{
    private final int choices;
    private final int costReduction;
    private final AbstractPlayer p;

    public AishaAction(int choices, int costReduction)
    {
        this.choices = choices;
        this.costReduction = costReduction;
        this.p = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            if (p.hand.size() == 0)
            {
                this.isDone = true;
            }
            else
            {
                CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                RandomizedList<AbstractCard> randomSkills = new RandomizedList<>(p.drawPile.getSkills().group);
                for (int i = 0; i < choices; i++)
                {
                    if (randomSkills.Count() > 0)
                    {
                        group.addToTop(randomSkills.Retrieve(AbstractDungeon.miscRng));
                    }
                }

                if (group.size() > 0)
                {
                    AbstractDungeon.gridSelectScreen.open(group, 1, false, SkillFromDeckToHandAction.TEXT[0]);
                }
                else
                {
                    this.isDone = true;
                }
            }
        }

        if (AbstractDungeon.gridSelectScreen.selectedCards.size() > 0)
        {
            AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);

            if (costReduction > 0)
            {
                AbstractDungeon.actionManager.addToBottom(new ReduceCostAction(card.uuid, costReduction));
            }
            AbstractDungeon.actionManager.addToBottom(new DrawSpecificCardAction(card));

            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            this.isDone = true;
        }

        this.tickDuration();
    }
}
