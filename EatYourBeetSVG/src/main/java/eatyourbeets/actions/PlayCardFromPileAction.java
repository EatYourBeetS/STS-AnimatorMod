package eatyourbeets.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.PlayerStatistics;

public class PlayCardFromPileAction extends AbstractGameAction
{
    private final CardGroup group;
    private final AbstractCard card;
    private final boolean exhaustCards;
    private final boolean purgeCards;

    public PlayCardFromPileAction(AbstractCard card, CardGroup group, boolean exhausts, boolean purge)
    {
        this.card = card;
        this.group = group;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
        this.source = AbstractDungeon.player;
        this.exhaustCards = exhausts;
        this.purgeCards = purge;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            if (group.size() == 0)
            {
                this.isDone = true;
                return;
            }

            if (group.contains(card))
            {
                group.removeCard(card);
                AbstractDungeon.getCurrRoom().souls.remove(card);
                card.freeToPlayOnce = true;
                card.exhaustOnUseOnce = this.exhaustCards;
                card.purgeOnUse = purgeCards;
                AbstractDungeon.player.limbo.group.add(card);
                card.current_y = -200.0F * Settings.scale;
                card.target_x = (float) Settings.WIDTH / 2.0F + 200.0F * Settings.scale;
                card.target_y = (float) Settings.HEIGHT / 2.0F;
                card.targetAngle = 0.0F;
                card.lighten(false);
                card.drawScale = 0.12F;
                card.targetDrawScale = 0.75F;

                AbstractMonster enemy = PlayerStatistics.GetRandomEnemy(true);

                if (!card.canUse(AbstractDungeon.player, enemy))
                {
                    if (this.purgeCards)
                    {
                        AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
                    }
                    else if (this.exhaustCards)
                    {
                        AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.limbo));
                    }
                    else
                    {
                        AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
                        AbstractDungeon.actionManager.addToTop(new DiscardSpecificCardAction(card, AbstractDungeon.player.limbo));
                        AbstractDungeon.actionManager.addToTop(new WaitRealtimeAction(0.4F));
                    }
                }
                else
                {
                    card.applyPowers();
                    AbstractDungeon.actionManager.addToTop(new QueueCardAction(card, enemy));
                    AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
                    if (!Settings.FAST_MODE)
                    {
                        AbstractDungeon.actionManager.addToTop(new WaitRealtimeAction(Settings.ACTION_DUR_MED));
                    }
                    else
                    {
                        AbstractDungeon.actionManager.addToTop(new WaitRealtimeAction(Settings.ACTION_DUR_FAST));
                    }
                }
            }

            this.isDone = true;
        }

    }
}
