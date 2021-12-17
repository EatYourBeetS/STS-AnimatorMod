package pinacolada.actions.cardManipulation;

import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;
import java.util.UUID;

public class PurgeAnywhere extends EYBActionWithCallback<Boolean>
{
    protected final UUID uuid;
    protected Vector2 targetPosition;
    protected boolean showEffect;
    protected boolean purged;

    public PurgeAnywhere(AbstractCard card)
    {
        this(card, null, 3);
    }

    public PurgeAnywhere(UUID uuid)
    {
        this(null, uuid, 3);
    }

    public PurgeAnywhere(AbstractCard card, UUID uuid, int repeat)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_LONG);

        this.uuid = uuid;
        this.card = card;

        if (this.card != null)
        {
            PCLGameUtilities.SetCardTag(this.card, GR.Enums.CardTags.PURGING, true);
        }

        Initialize(repeat);
    }

    public PurgeAnywhere SetTargetPosition(Vector2 position)
    {
        this.targetPosition = position;

        return this;
    }

    public PurgeAnywhere ShowEffect(boolean value)
    {
        this.showEffect = value;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        super.FirstUpdate();

        final ArrayList<CardGroup> groups = new ArrayList<>();
        groups.add(player.hand);
        groups.add(player.limbo);
        groups.add(player.drawPile);
        groups.add(player.discardPile);
        groups.add(player.exhaustPile);

        if (card != null)
        {
            boolean queueEffect = showEffect;
            for (CardGroup group : groups)
            {
                if (group.contains(card))
                {
                    group.removeCard(card);
                    this.purged = true;

                    if (queueEffect)
                    {
                        PurgeEffect(group, card);
                        queueEffect = false;
                    }
                }
            }
        }

        if (uuid != null)
        {
            for (CardGroup group : groups)
            {
                RemoveAll(group);
            }
        }

        if (amount > 0)
        {
            PCLActions.Bottom.Add(new PurgeAnywhere(card, uuid, amount - 1).SetTargetPosition(targetPosition).ShowEffect(showEffect));
        }

        if (!purged)
        {
            Complete(purged);
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete(purged);
        }
    }

    private void RemoveAll(CardGroup group)
    {
        ArrayList<AbstractCard> toRemove = new ArrayList<>();
        for (AbstractCard c : group.group)
        {
            if (c.uuid.equals(uuid))
            {
                toRemove.add(c);
            }
        }

        for (AbstractCard c : toRemove)
        {
            group.removeCard(c);
            PCLCombatStats.OnPurge(card,group);
            this.purged = true;

            if (showEffect)
            {
                PurgeEffect(group, c);
            }
        }
    }

    private void PurgeEffect(CardGroup group, AbstractCard c)
    {
        if (c.drawScale < 0.3f)
        {
            c.targetDrawScale = 0.75f;
        }

        c.untip();
        c.unhover();
        c.unfadeOut();
        c.targetAngle = 0;

        final Vector2 pos = PCLGameUtilities.TryGetPosition(group, c);
        if (pos != null)
        {
            final AbstractGameEffect effect = PCLGameEffects.List.Add(new PurgeCardEffect(c, pos.x, pos.y));
            if (targetPosition != null)
            {
                c.target_x = targetPosition.x;
                c.target_y = targetPosition.y;
            }

            effect.startingDuration = effect.duration = duration;
        }
    }
}
