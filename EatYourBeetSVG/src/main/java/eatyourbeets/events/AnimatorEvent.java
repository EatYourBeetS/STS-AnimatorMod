package eatyourbeets.events;

import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import eatyourbeets.AnimatorResources;
import eatyourbeets.actions.AnimatorAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AnimatorEvent extends AbstractImageEvent
{
    protected static final Logger logger = LogManager.getLogger(AnimatorAction.class.getName());

    public static String CreateFullID(String id)
    {
        return "Animator_" + id;
    }

    protected final EventStrings eventStrings;

    public AnimatorEvent(String id)
    {
        super("NAME", "BODY", "images/events/placeholder.jpg");

        this.eventStrings = AnimatorResources.GetEventStrings(id);
        NAME = eventStrings.NAME;
        OPTIONS = eventStrings.OPTIONS;

        this.title = NAME;
        this.body = eventStrings.DESCRIPTIONS[0];
    }

    @Override
    protected abstract void buttonEffect(int i);
}