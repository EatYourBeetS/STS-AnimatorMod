package eatyourbeets.actions._legacy.animator;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.CorruptionPower;
import eatyourbeets.cards.animator.Wiz;

public class WizAction extends AbstractGameAction
{
    private AbstractPlayer p;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    public WizAction(AbstractPlayer player)
    {
        this.p = player;
        this.setValues(this.p, player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            CardGroup tempGroup = new CardGroup(this.p.exhaustPile, CardGroup.CardGroupType.EXHAUST_PILE);
            tempGroup.group.removeIf(c -> c.cardID.equals(Wiz.ID));

            if (tempGroup.group.size() == 0)
            {
                this.isDone = true;
                return;
            }

            AbstractDungeon.gridSelectScreen.open(tempGroup, 1, TEXT[0], false);
            this.tickDuration();
        }
        else
        {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty())
            {
                for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards)
                {
                    if (p.hand.size() >= BaseMod.MAX_HAND_SIZE)
                    {
                        p.createHandIsFullDialog();
                        p.discardPile.addToBottom(card.makeStatEquivalentCopy());
                    }
                    else
                    {
                        this.p.hand.addToHand(card.makeStatEquivalentCopy());
                        if (AbstractDungeon.player.hasPower(CorruptionPower.POWER_ID) && card.type == CardType.SKILL)
                        {
                            card.setCostForTurn(-9);
                        }
                    }
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                this.p.hand.refreshHandLayout();

                for (AbstractCard card : p.exhaustPile.group)
                {
                    card.unhover();
                    card.target_x = (float) CardGroup.DISCARD_PILE_X;
                    card.target_y = 0.0F;
                }
            }

            this.tickDuration();
        }
    }

    static
    {
        uiStrings = CardCrawlGame.languagePack.getUIString("ExhumeAction");
        TEXT = uiStrings.TEXT;
    }
}