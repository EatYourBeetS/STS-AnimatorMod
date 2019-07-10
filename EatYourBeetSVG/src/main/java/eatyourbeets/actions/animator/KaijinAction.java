package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import eatyourbeets.cards.animator.Kaijin;
import eatyourbeets.resources.Resources_Animator;

import java.util.Iterator;

public class KaijinAction extends AbstractGameAction
{
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    public KaijinAction(AbstractCreature source, int amount)
    {
        this.setValues(AbstractDungeon.player, source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update()
    {
        if (this.duration == 0.5F)
        {
            AbstractDungeon.handCardSelectScreen.open(TEXT[0] + " (" + Resources_Animator.GetCardStrings(Kaijin.ID).NAME + ")",
                    1, false, true, false, false, true);

            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25F));
            this.tickDuration();
        }
        else
        {
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved)
            {
                AbstractCard c;
                for (Iterator var1 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator(); var1.hasNext(); AbstractDungeon.player.hand.addToTop(c))
                {
                    c = (AbstractCard) var1.next();
                    if (!c.isEthereal)
                    {
                        c.retain = true;

                        if (c.baseBlock > 0)
                        {
                            c.baseBlock += amount;
                        }

                        if (c.baseDamage > 0)
                        {
                            c.baseDamage += amount;
                        }
                    }
                }

                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            }

            this.tickDuration();
        }
    }

    static
    {
        uiStrings = CardCrawlGame.languagePack.getUIString("RetainCardsAction");
        TEXT = uiStrings.TEXT;
    }
}
