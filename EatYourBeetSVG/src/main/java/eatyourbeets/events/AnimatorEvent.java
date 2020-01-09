package eatyourbeets.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.ui.buttons.DialogRelicButton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.function.Consumer;

// TODO: Create common event class
public abstract class AnimatorEvent extends AbstractImageEvent
{
    protected static final Logger logger = LogManager.getLogger(EYBAction.class.getName());

    public static String CreateFullID(String id)
    {
        return GR.Animator.CreateID(id);
    }

    private static final FieldInfo<ArrayList<LargeDialogOptionButton>> optionListField = JavaUtilities.GetPrivateField("optionList", GenericEventDialog.class);

    protected final ArrayList<PhaseHandler> phaseHandlers = new ArrayList<>();
    protected final EventStrings eventStrings;
    protected final String[] DESCRIPTIONS;

    protected int phase;

    public AnimatorEvent(String id, String imageUrl)
    {
        super("NAME", "BODY", "images/events/" + imageUrl);

        this.eventStrings = AnimatorResources.GetEventStrings(id);
        NAME = eventStrings.NAME;
        OPTIONS = eventStrings.OPTIONS;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;

        this.title = NAME;
        this.body = eventStrings.DESCRIPTIONS[0];
        this.phase = 0;
    }

    public AnimatorEvent(String id)
    {
        this(id, "placeholder.jpg");
    }

    protected void RegisterPhase(int phase, Runnable create, Consumer<Integer> handle)
    {
        phaseHandlers.add(new PhaseHandler(phase, create, handle));
    }

    protected void ProgressPhase(int phase)
    {
        this.phase = phase;
        for (PhaseHandler p : phaseHandlers)
        {
            if (p.phase == phase)
            {
                p.create.run();
                return;
            }
        }
    }

    protected void ProgressPhase()
    {
        phase += 1;
        for (PhaseHandler p : phaseHandlers)
        {
            if (p.phase == phase)
            {
                p.create.run();
                return;
            }
        }
    }

    @Override
    protected void buttonEffect(int i)
    {
        for (PhaseHandler p : phaseHandlers)
        {
            if (p.phase == phase)
            {
                p.handle.accept(i);
                return;
            }
        }

        this.openMap();
    }

    protected void UpdateBodyText(String text, boolean clearAll)
    {
        this.imageEventText.updateBodyText(text);

        if (clearAll)
        {
            this.imageEventText.clearAllDialogs();
        }
    }

    protected void UpdateDialogOption(int slot, String text)
    {
        this.imageEventText.updateDialogOption(slot, text);
    }

    protected void UpdateDialogOption(int slot, String text, boolean disabled)
    {
        this.imageEventText.updateDialogOption(slot, text, disabled);
    }

    protected void UpdateDialogOption(int slot, String text, AbstractCard card)
    {
        this.imageEventText.updateDialogOption(slot, text, card);
    }

    protected void UpdateDialogOption(int slot, String text, AbstractRelic relic)
    {
        ArrayList<LargeDialogOptionButton> optionList = optionListField.Get(imageEventText);

        if (!optionList.isEmpty())
        {
            if (optionList.size() > slot)
            {
                optionList.set(slot, new DialogRelicButton(slot, text, false, relic));
            }
            else
            {
                optionList.add(new DialogRelicButton(slot, text, false, relic));
            }
        }
        else
        {
            optionList.add(new DialogRelicButton(slot, text, false, relic));
        }
    }

    protected static class PhaseHandler
    {
        public final int phase;
        public final Runnable create;
        public final Consumer<Integer> handle;

        public PhaseHandler(int phase, Runnable create, Consumer<Integer> handle)
        {
            this.phase = phase;
            this.create = create;
            this.handle = handle;
        }
    }
}