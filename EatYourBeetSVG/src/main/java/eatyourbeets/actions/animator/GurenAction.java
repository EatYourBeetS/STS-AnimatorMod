package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.cards.animator.series.OwariNoSeraph.Guren;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

public class GurenAction extends EYBAction
{
    public GurenAction(AbstractCreature target)
    {
        super(ActionType.WAIT);

        Initialize(player, target, 1);
    }

    @Override
    protected void FirstUpdate()
    {
        if (player.drawPile.isEmpty())
        {
            if (player.discardPile.size() > 0)
            {
                GameActions.Top.Add(new GurenAction(this.target));
                GameActions.Top.Add(new EmptyDeckShuffleAction());
            }

            Complete();
            return;
        }

        AbstractCard card = player.drawPile.getTopCard();
        player.drawPile.group.remove(card);
        AbstractDungeon.getCurrRoom().souls.remove(card);

        boolean skip = false;
        Guren guren = JavaUtilities.SafeCast(card, Guren.class);
        if (guren == null || guren.CanAutoPlay(this))
        {
            card.freeToPlayOnce = true;
        }
        else
        {
            skip = true;
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
            GameActions.Top.Add(new UnlimboAction(card));
            GameActions.Top.Exhaust(card, AbstractDungeon.player.limbo).SetRealtime(true);
        }
        else if (skip || !card.canUse(AbstractDungeon.player, (AbstractMonster) this.target))
        {
            GameActions.Top.Add(new UnlimboAction(card));
            GameActions.Top.Discard(card, AbstractDungeon.player.limbo).SetRealtime(true);
        }
        else
        {
            if (card.type == AbstractCard.CardType.ATTACK)
            {
                card.exhaustOnUseOnce = true;
            }

            card.applyPowers();

            GameActions.Top.Add(new QueueCardAction(card, this.target));
            GameActions.Top.Add(new UnlimboAction(card));

            if (!Settings.FAST_MODE)
            {
                GameActions.Top.WaitRealtime(Settings.ACTION_DUR_MED);
            }
            else
            {
                GameActions.Top.WaitRealtime(Settings.ACTION_DUR_FAST);
            }
        }
    }
}
