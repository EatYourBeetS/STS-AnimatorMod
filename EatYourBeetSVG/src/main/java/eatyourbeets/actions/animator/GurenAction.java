package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.common.WaitRealtimeAction;
import eatyourbeets.cards.animator.Guren;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.GameUtilities;

public class GurenAction extends AbstractGameAction
{
    private final int supportDamage;

    public GurenAction(AbstractCreature target, int supportDamage)
    {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
        this.source = AbstractDungeon.player;
        this.target = target;
        this.supportDamage = supportDamage;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            if (AbstractDungeon.player.drawPile.size() + AbstractDungeon.player.discardPile.size() == 0)
            {
                this.isDone = true;
                return;
            }

            if (AbstractDungeon.player.drawPile.isEmpty())
            {
                AbstractDungeon.actionManager.addToTop(new GurenAction(this.target, this.supportDamage));
                AbstractDungeon.actionManager.addToTop(new EmptyDeckShuffleAction());
                this.isDone = true;
                return;
            }

            if (!AbstractDungeon.player.drawPile.isEmpty())
            {
                AbstractCard card = AbstractDungeon.player.drawPile.getTopCard();
                AbstractDungeon.player.drawPile.group.remove(card);
                AbstractDungeon.getCurrRoom().souls.remove(card);

                boolean skip = (card instanceof Guren && AbstractDungeon.actionManager.cardsPlayedThisTurn.contains(card));
                if (!skip)
                {
                    card.freeToPlayOnce = true;
                }

                AbstractDungeon.player.limbo.group.add(card);
                card.current_y = -200.0F * Settings.scale;
                card.target_x = (float) Settings.WIDTH / 2.0F + 200 * Settings.scale;
                card.target_y = (float) Settings.HEIGHT / 2.0F;
                card.targetAngle = 0.0F;
                card.lighten(false);
                card.drawScale = 0.12F;
                card.targetDrawScale = 0.75F;

                if (card.target == AbstractCard.CardTarget.ENEMY || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY)
                {
                    if (this.target == null || this.target.isDeadOrEscaped())
                    {
                        this.target = GameUtilities.GetRandomEnemy(true);
                    }
                }
                else
                {
                    this.target = null;
                }

                if (GameUtilities.IsCurseOrStatus(card))
                {
                    GameActionsHelper.AddToTop(new UnlimboAction(card));
                    GameActionsHelper.AddToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.limbo));
                    GameActionsHelper.AddToTop(new WaitRealtimeAction(0.4F));

                    //GameActionsHelper.ApplyPower(source, source, new SupportDamagePower(source, supportDamage), supportDamage);
                }
                else if (skip || !card.canUse(AbstractDungeon.player, (AbstractMonster) this.target))
                {
                    GameActionsHelper.AddToTop(new UnlimboAction(card));
                    GameActionsHelper.AddToTop(new DiscardSpecificCardAction(card, AbstractDungeon.player.limbo));
                    GameActionsHelper.AddToTop(new WaitRealtimeAction(0.4F));
                }
                else
                {
                    if (card.type == AbstractCard.CardType.ATTACK)
                    {
                        card.exhaustOnUseOnce = true;
                        //GameActionsHelper.ApplyPower(source, source, new SupportDamagePower(source, supportDamage), supportDamage);
                    }

                    card.applyPowers();
                    GameActionsHelper.AddToTop(new QueueCardAction(card, this.target));
                    GameActionsHelper.AddToTop(new UnlimboAction(card));
                    if (!Settings.FAST_MODE)
                    {
                        GameActionsHelper.AddToTop(new WaitRealtimeAction(Settings.ACTION_DUR_MED));
                    }
                    else
                    {
                        GameActionsHelper.AddToTop(new WaitRealtimeAction(Settings.ACTION_DUR_FAST));
                    }
                }
            }

            this.isDone = true;
        }

    }
}
