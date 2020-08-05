package eatyourbeets.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public abstract class EYBAction extends AbstractGameAction
{
    public boolean canCancel;
    public boolean isRealtime;

    protected final ArrayList<Object> tags = new ArrayList<>(1);
    protected final AbstractPlayer player;
    protected final Random rng;
    protected AbstractCard card;
    protected String message;
    protected String name;
    protected int ticks;

    public EYBAction(ActionType type)
    {
        this(type, Settings.ACTION_DUR_FAST);
    }

    public EYBAction(ActionType type, float duration)
    {
        this.rng = AbstractDungeon.cardRandomRng;
        this.player = AbstractDungeon.player;
        this.duration = this.startDuration = duration;
        this.actionType = type;
        this.canCancel = true;
    }

    public boolean HasTag(Object tag)
    {
        return this.tags.contains(tag);
    }

    public EYBAction AddTag(Object tag)
    {
        this.tags.add(tag);

        return this;
    }

    public EYBAction IsCancellable(boolean canCancel)
    {
        this.canCancel = canCancel;

        return this;
    }

    public EYBAction SetRealtime(boolean isRealtime)
    {
        this.isRealtime = isRealtime;

        return this;
    }

    public EYBAction SetDuration(float duration, boolean isRealtime)
    {
        this.isRealtime = isRealtime;
        this.duration = this.startDuration = duration;

        return this;
    }

    public EYBAction AddDuration(float duration, boolean isRealtime)
    {
        this.isRealtime = isRealtime;
        this.duration = (this.startDuration += duration);

        return this;
    }

    protected void Initialize(int amount)
    {
        Initialize(player, amount, null);
    }

    protected void Initialize(int amount, String name)
    {
        Initialize(player, amount, name);
    }

    protected void Initialize(AbstractCreature target, int amount)
    {
        Initialize(target, target, amount, null);
    }

    protected void Initialize(AbstractCreature target, int amount, String name)
    {
        Initialize(target, target, amount, name);
    }

    protected void Initialize(AbstractCreature source, AbstractCreature target, int amount)
    {
        Initialize(source, target, amount, null);
    }

    protected void Initialize(AbstractCreature source, AbstractCreature target, int amount, String name)
    {
        this.source = source;
        this.target = target;
        this.amount = amount;
        this.name = name;
    }

    protected String CreateMessageInternal(String defaultMessage)
    {
        if (message == null)
        {
            message = defaultMessage;
        }

        if (name != null && !name.equals(""))
        {
            return JUtils.Format(message, amount) + " [" + name + "]";
        }
        else
        {
            return JUtils.Format(message, amount);
        }
    }

    public String CreateMessage()
    {
        return CreateMessageInternal("");
    }

    @Override
    public void update()
    {
        if (duration == startDuration)
        {
            FirstUpdate();

            if (!this.isDone)
            {
                TickDuration(GetDeltaTime());
            }
        }
        else
        {
            UpdateInternal(GetDeltaTime());
        }
    }

    protected void FirstUpdate()
    {

    }

    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete();
        }
    }

    protected void Complete()
    {
        this.isDone = true;
    }

    protected float GetDeltaTime()
    {
        return isRealtime ? Gdx.graphics.getRawDeltaTime() : Gdx.graphics.getDeltaTime();
    }

    protected boolean TickDuration(float deltaTime)
    {
        this.ticks += 1;
        this.duration -= deltaTime;

        if (this.duration < 0f && ticks >= 3) // ticks are necessary for SuperFastMode at 1000% speed
        {
            this.isDone = true;
        }

        return isDone;
    }

    @Override
    protected final void tickDuration()
    {
        TickDuration(GetDeltaTime());
    }

    protected void Import(EYBAction other)
    {
        SetDuration(other.startDuration, other.isRealtime);
        IsCancellable(other.canCancel);
        tags.addAll(other.tags);
        name = other.name;
        message = other.message;
    }
}
