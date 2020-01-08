package eatyourbeets.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public abstract class EYBAction extends AbstractGameAction
{
    protected final ArrayList<Object> tags = new ArrayList<>(1);
    protected AbstractCard card;
    protected final AbstractPlayer player;
    protected String message;
    protected String name;
    protected boolean isRealtime;
    protected int ticks;

    public EYBAction(ActionType type)
    {
        this(type, Settings.ACTION_DUR_FAST);
    }

    public EYBAction(ActionType type, float duration)
    {
        this.player = AbstractDungeon.player;
        this.duration = this.startDuration = duration;
        this.actionType = type;
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
            return message + " [" + name + "]";
        }

        return message;
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
                tickDuration();
            }
        }
        else
        {
            UpdateInternal();
        }
    }

    protected void FirstUpdate()
    {

    }

    protected void UpdateInternal()
    {
        tickDuration();
    }

    protected void Complete()
    {
        this.isDone = true;
    }

    protected void tickDuration()
    {
        this.ticks += 1;

        if (isRealtime)
        {
            this.duration -= Gdx.graphics.getRawDeltaTime();
        }
        else
        {
            this.duration -= Gdx.graphics.getDeltaTime();
        }

        if (this.duration < 0.0F && ticks >= 3) // ticks are necessary for SuperFastMode at 1000% speed
        {
            this.isDone = true;
        }
    }
}
