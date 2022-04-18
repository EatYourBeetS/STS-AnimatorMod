package eatyourbeets.actions.cardManipulation;

import com.badlogic.gdx.math.MathUtils;
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
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import eatyourbeets.actions.EYBActionWithCallbackT2;
import eatyourbeets.actions.special.DelayAllActions;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

// If this action needs 1 more refactoring due to queueing a card not counting
// as an action, completely override AbstractDungeon.actionManager instead.
public class PlayCard extends EYBActionWithCallbackT2<AbstractMonster, AbstractCard>
{
    public static final float DEFAULT_TARGET_X_LEFT = (Settings.WIDTH / 2f) - (300f * Settings.scale);
    public static final float DEFAULT_TARGET_X_RIGHT = (Settings.WIDTH / 2f) + (200f * Settings.scale);
    public static final float DEFAULT_TARGET_Y = (Settings.HEIGHT / 2f);

    protected final boolean isCopy;
    protected FuncT1<AbstractCard, CardGroup> findCard;
    protected CardGroup sourcePile;
    protected int sourcePileIndex;
    protected boolean purge;
    protected boolean exhaust;
    protected boolean spendEnergy;
    protected boolean autoPlay;
    protected Vector2 currentPosition;
    protected Vector2 targetPosition;
    protected boolean renderLast;

    public PlayCard(FuncT1<AbstractCard, CardGroup> findCard, CardGroup sourcePile, AbstractCreature target)
    {
        super(ActionType.WAIT, Settings.ACTION_DUR_FAST);

        this.autoPlay = true;
        this.spendEnergy = false;
        this.isCopy = false;
        this.isRealtime = true;
        this.findCard = findCard;
        this.sourcePile = sourcePile;

        Initialize(target, 1);
    }

    public PlayCard(AbstractCard card, AbstractCreature target, boolean copy, boolean renderLast)
    {
        super(ActionType.WAIT, Settings.ACTION_DUR_FAST);

        this.autoPlay = true;
        this.spendEnergy = false;
        this.isRealtime = true;

        if (isCopy = copy)
        {
            this.card = card.makeSameInstanceOf();
            this.card.energyOnUse = card.energyOnUse;
        }
        else
        {
            this.card = card;
        }

        if (this.card != null)
        {
            this.card.unfadeOut();
            this.card.lighten(true);
        }

        this.renderLast = renderLast;

        AddToLimbo();

        Initialize(target, 1);
    }

    public PlayCard SpendEnergy(boolean spendEnergy, boolean autoPlay)
    {
        this.spendEnergy = spendEnergy;
        this.autoPlay = autoPlay;

        return this;
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

    public PlayCard SetCurrentPosition(float x, float y, boolean updateImmediately)
    {
        currentPosition = new Vector2(x, y);

        if (updateImmediately && card != null)
        {
            card.current_x = card.target_x = currentPosition.x;
            card.current_y = card.target_y = currentPosition.y;
        }

        return this;
    }

    public PlayCard SetCurrentPosition(float x, float y)
    {
        return SetCurrentPosition(x, y, false);
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
                Cancel();
                return;
            }
            else
            {
                GameUtilities.TrySetPosition(sourcePile, card);
            }
        }

        if (!CheckConditions(card))
        {
            Cancel();
            return;
        }

        if (sourcePile != null)
        {
            sourcePileIndex = sourcePile.group.indexOf(card);
            if (sourcePileIndex >= 0)
            {
                sourcePile.group.remove(sourcePileIndex);
            }
            else
            {
                JUtils.LogWarning(this, "Could not find " + card.cardID + " in " + sourcePile.type.name().toLowerCase());
                Cancel();
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
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            if (GameUtilities.RequiresTarget(card) && (target == null || GameUtilities.IsDeadOrEscaped(target)))
            {
                target = GameUtilities.GetRandomEnemy(true);
            }

            if (!spendEnergy)
            {
                card.freeToPlayOnce = true;
                card.ignoreEnergyOnUse = false;
            }

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
            else if (spendEnergy && sourcePile == player.hand)
            {
                player.limbo.removeCard(card);
                sourcePile.group.add(MathUtils.clamp(sourcePileIndex, 0, sourcePile.size()), card);
            }
            else
            {
                GameActions.Top.Discard(card, player.limbo).SetRealtime(true);
                GameActions.Top.Add(new WaitAction(Settings.ACTION_DUR_FAST));
            }

            if (card.cantUseMessage != null)
            {
                GameEffects.List.Add(new ThoughtBubble(player.dialogX, player.dialogY, 3, card.cantUseMessage, true));
            }

            card.freeToPlayOnce = false;
        }
    }

    protected boolean CanUse()
    {
        return card.canUse(player, (AbstractMonster) target) || card.dontTriggerOnUseCard;
    }

    protected void ShowCard()
    {
        AddToLimbo();

        GameUtilities.RefreshHandLayout();
        AbstractDungeon.getCurrRoom().souls.remove(card);

        if (currentPosition != null)
        {
            card.current_x = currentPosition.x;
            card.current_y = currentPosition.y;
        }

        card.target_x = targetPosition.x;
        card.target_y = targetPosition.y;
        card.targetAngle = 0f;
        card.unfadeOut();
        card.lighten(true);
        card.drawScale = 0.5f;
        card.targetDrawScale = 0.75f;
    }

    protected void QueueCardItem()
    {
        AddToLimbo();

        final AbstractMonster enemy = (AbstractMonster) target;

        if (!spendEnergy)
        {
            card.freeToPlayOnce = true;
        }

        card.exhaustOnUseOnce = exhaust;
        card.purgeOnUse = purge;
        card.calculateCardDamage(enemy);

        //GameActions.Top.Add(new UnlimboAction(card));
        GameActions.Top.Wait(Settings.FAST_MODE ? Settings.ACTION_DUR_FASTER : Settings.ACTION_DUR_MED);

        int energyOnUse = EnergyPanel.getCurrentEnergy();

        if (spendEnergy)
        {
            GameActions.Top.Add(new DelayAllActions()) // So the result of canUse() does not randomly change after queueing the card
            .Except(a -> a instanceof UnlimboAction || a instanceof WaitAction);
        }
        else if (card.energyOnUse != -1)
        {
            energyOnUse = card.energyOnUse;
        }

        GameUtilities.SetCardTag(card, GR.Enums.CardTags.AUTOPLAYED, autoPlay);
        GameUtilities.SetCardTag(card, GR.Enums.CardTags.AUTOPLAYED_COPY, isCopy);

        AbstractDungeon.actionManager.cardQueue.add(0, new CardQueueItem(card, enemy, energyOnUse, !spendEnergy, autoPlay || !spendEnergy));

        Complete(enemy);
    }

    protected void AddToLimbo()
    {
        if (card != null && !player.limbo.contains(card))
        {
            if (renderLast)
            {
                player.limbo.addToTop(card);
            }
            else
            {
                player.limbo.addToBottom(card);
            }
        }
    }

    protected void Cancel()
    {
        Complete();

        if (card != null)
        {
            player.limbo.removeCard(card);
            card.unfadeOut();
        }
    }
}
