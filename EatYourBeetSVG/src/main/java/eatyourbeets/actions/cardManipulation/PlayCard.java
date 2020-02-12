package eatyourbeets.actions.cardManipulation;

import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.csharp.FuncT1;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

public class PlayCard extends EYBActionWithCallback<AbstractMonster>
{
    public static final float DEFAULT_TARGET_X_LEFT = (Settings.WIDTH / 2.0F) - (300.0F * Settings.scale);
    public static final float DEFAULT_TARGET_X_RIGHT = (Settings.WIDTH / 2.0F) + (200.0F * Settings.scale);
    public static final float DEFAULT_TARGET_Y = (Settings.HEIGHT / 2.0F);

    protected FuncT1<AbstractCard, CardGroup> findCard;
    protected CardGroup sourcePile;
    protected boolean purge;
    protected boolean exhaust;
    protected Vector2 currentPosition;
    protected Vector2 targetPosition;

    public PlayCard(FuncT1<AbstractCard, CardGroup> findCard, CardGroup sourcePile, AbstractCreature target)
    {
        super(ActionType.WAIT, Settings.ACTION_DUR_FAST);

        this.isRealtime = true;
        this.findCard = findCard;
        this.sourcePile = sourcePile;

        Initialize(target, 1);
    }

    public PlayCard(AbstractCard card, AbstractCreature target, boolean copy)
    {
        super(ActionType.WAIT, Settings.ACTION_DUR_FAST);

        this.isRealtime = true;

        if (copy)
        {
            this.card = card.makeSameInstanceOf();
            this.card.energyOnUse = card.energyOnUse;
        }
        else
        {
            this.card = card;
        }

        Initialize(target, 1);
    }

    public PlayCard SetSourcePile(CardGroup sourcePile)
    {
        this.sourcePile = sourcePile;

        if (card != null)
        {
            GameUtilities.TrySetPosition(sourcePile, card);
        }

        return this;
    }

    public PlayCard SetCurrentPosition(float x, float y)
    {
        currentPosition = new Vector2(x, y);

        return this;
    }

    public PlayCard SetTargetPosition(float x, float y)
    {
        targetPosition = new Vector2(x, y);

        return this;
    }

    public PlayCard SetExhaust(boolean exhaust)
    {
        if (this.exhaust = exhaust)
        {
            this.purge = false;
        }

        return this;
    }

    public PlayCard SetPurge(boolean purge)
    {
        if (this.purge = purge)
        {
            this.exhaust = false;
        }

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        super.FirstUpdate();

        if (findCard != null)
        {
            if (sourcePile.size() > 0)
            {
                card = findCard.Invoke(sourcePile);
            }

            if (card == null)
            {
                Complete();
                return;
            }
            else
            {
                GameUtilities.TrySetPosition(sourcePile, card);
            }
        }

        if (sourcePile != null)
        {
            if (sourcePile.contains(card))
            {
                sourcePile.removeCard(card);
            }
            else
            {
                JavaUtilities.GetLogger(getClass()).warn("Could not find " + card.cardID + " in " + sourcePile.type.name().toLowerCase());
                Complete();
                return;
            }
        }

        if (targetPosition == null)
        {
            SetTargetPosition(DEFAULT_TARGET_X_LEFT, DEFAULT_TARGET_Y);
        }

        ShowCard();
    }

    @Override
    protected void UpdateInternal()
    {
        tickDuration();

        if (isDone)
        {
            if (target == null || GameUtilities.IsDeadOrEscaped(target))
            {
                target = GameUtilities.GetRandomEnemy(true);
            }

            card.freeToPlayOnce = true;

            if (CanUse())
            {
                QueueCardItem();
                return;
            }
            else if (purge)
            {
                GameActions.Top.Add(new UnlimboAction(card));
            }
            else if (exhaust)
            {
                GameActions.Top.Exhaust(card, player.limbo).SetRealtime(true);
            }
            else
            {
                GameActions.Top.Discard(card, player.limbo).SetRealtime(true);
                GameActions.Top.Add(new WaitAction(Settings.ACTION_DUR_FAST));
            }

            card.freeToPlayOnce = false;
        }
    }

    protected boolean CanUse()
    {
        return card.canUse(player, (AbstractMonster)target);
    }

    protected void ShowCard()
    {
        GameUtilities.RefreshHandLayout();
        AbstractDungeon.getCurrRoom().souls.remove(card);
        player.limbo.group.add(card);

        if (currentPosition != null)
        {
            card.current_x = currentPosition.x;
            card.current_y = currentPosition.y;
        }

        card.target_x = targetPosition.x;
        card.target_y = targetPosition.y;
        card.targetAngle = 0.0F;
        card.unfadeOut();
        card.lighten(true);
        card.drawScale = 0.5F;
        card.targetDrawScale = 0.75F;
    }

    protected void QueueCardItem()
    {
        AbstractMonster enemy = (AbstractMonster) target;

        card.freeToPlayOnce = true;
        card.exhaustOnUseOnce = exhaust;
        card.purgeOnUse = purge;
        card.calculateCardDamage(enemy);

        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(card, enemy, card.energyOnUse, true, true), true);
        GameActions.Top.Add(new UnlimboAction(card));
        if (Settings.FAST_MODE)
        {
            GameActions.Top.Add(new WaitAction(Settings.ACTION_DUR_FASTER));
        }
        else
        {
            GameActions.Top.Add(new WaitAction(Settings.ACTION_DUR_MED));
        }

        Complete(enemy);
    }
}
