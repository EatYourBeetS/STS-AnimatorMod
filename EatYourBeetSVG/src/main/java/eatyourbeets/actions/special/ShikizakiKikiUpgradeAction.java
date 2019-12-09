package eatyourbeets.actions.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;
import java.util.Iterator;


public class ShikizakiKikiUpgradeAction extends AbstractGameAction
{
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;
    private ArrayList<AbstractCard> cannotUpgrade = new ArrayList<>();

    public ShikizakiKikiUpgradeAction()
    {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update()
    {
        Iterator var1;
        AbstractCard c;
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            var1 = this.p.hand.group.iterator();

            while (var1.hasNext())
            {
                c = (AbstractCard) var1.next();
                if (!c.canUpgrade())
                {
                    this.cannotUpgrade.add(c);
                }
            }

            if (this.cannotUpgrade.size() == this.p.hand.group.size())
            {
                this.isDone = true;
                return;
            }

            if (this.p.hand.group.size() - this.cannotUpgrade.size() == 1)
            {
                var1 = this.p.hand.group.iterator();

                while (var1.hasNext())
                {
                    c = (AbstractCard) var1.next();
                    if (c.canUpgrade())
                    {
                        UpgradeCardPermanently(c);
                        this.isDone = true;
                        return;
                    }
                }
            }

            this.p.hand.group.removeAll(this.cannotUpgrade);
            if (this.p.hand.group.size() > 1)
            {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, true);
                this.tickDuration();
                return;
            }

            if (this.p.hand.group.size() == 1)
            {
                UpgradeCardPermanently(this.p.hand.getTopCard());
                this.returnCards();
                this.isDone = true;
            }
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved)
        {
            var1 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();

            while (var1.hasNext())
            {
                c = (AbstractCard) var1.next();
                UpgradeCardPermanently(c);
                this.p.hand.addToTop(c);
            }

            this.returnCards();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.isDone = true;
        }

        this.tickDuration();
    }

    private void UpgradeCardPermanently(AbstractCard card)
    {
        card.upgrade();
        card.superFlash();

        AbstractCard deckInstance = null;
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
        {
            if (c.uuid == card.uuid)
            {
                deckInstance = c;
                break;
            }
            else if (c.cardID.equals(card.cardID) && c.canUpgrade())
            {
                deckInstance = c;
            }
        }

        if (deckInstance != null && deckInstance.canUpgrade())
        {
            deckInstance.upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(deckInstance);
            AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 4.0F, (float) Settings.HEIGHT / 2.0F));
            AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(deckInstance.makeStatEquivalentCopy(), (float) Settings.WIDTH / 4.0F, (float) Settings.HEIGHT / 2.0F));
        }
    }

    private void returnCards()
    {
        for (AbstractCard c : this.cannotUpgrade)
        {
            this.p.hand.addToTop(c);
        }

        this.p.hand.refreshHandLayout();
    }

    static
    {
        uiStrings = CardCrawlGame.languagePack.getUIString("ArmamentsAction");
        TEXT = uiStrings.TEXT;
    }
}
