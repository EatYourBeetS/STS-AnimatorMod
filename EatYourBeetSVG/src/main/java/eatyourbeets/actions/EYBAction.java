package eatyourbeets.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class EYBAction extends AbstractGameAction
{
    protected static final Logger logger = LogManager.getLogger(EYBAction.class.getName());

    protected AbstractCard card;
    protected AbstractPlayer player;
    protected String message;
    protected String name;

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

    protected String CreateMessage()
    {
        if (message == null)
        {
            message = "";
        }

        if (name != null && !name.equals(""))
        {
            return message + " [" + name + "]";
        }

        return message;
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
}
