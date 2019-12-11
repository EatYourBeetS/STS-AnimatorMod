package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.SetDontTriggerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AnimatorCard_Status extends AnimatorCard
{
    protected AnimatorCard_Status(String id, int cost, CardRarity rarity, CardTarget target)
    {
        super(id, cost, CardType.STATUS, CardColor.COLORLESS, rarity, target);
    }

    @Override
    public void triggerWhenDrawn()
    {
        AbstractDungeon.actionManager.addToBottom(new SetDontTriggerAction(this, false));
        if (AbstractDungeon.player.hasPower("Evolve") && !AbstractDungeon.player.hasPower("No Draw"))
        {
            AbstractDungeon.player.getPower("Evolve").flash();
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, AbstractDungeon.player.getPower("Evolve").amount));
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        this.dontTriggerOnUseCard = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }

    @Override
    public boolean canPlay(AbstractCard card)
    {
        if (card == this)
        {
            return true;
        }

        return super.canPlay(card);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        return this.cardPlayable(m) && this.hasEnoughEnergy();
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void upgrade()
    {

    }
}