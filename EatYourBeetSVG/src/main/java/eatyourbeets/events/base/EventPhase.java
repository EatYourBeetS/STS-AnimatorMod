package eatyourbeets.events.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public abstract class EventPhase<Event extends EYBEvent, Text extends EYBEventStrings>
{
    public final ArrayList<EventOption> options = new ArrayList<>();

    public int id;
    public Text text;
    public Event event;
    public AbstractPlayer player;
    public GenericEventDialog dialog;

    public final void OnEnter(Event event, Text text)
    {
        this.text = text;
        this.event = event;
        this.dialog = event.imageEventText;
        this.player = AbstractDungeon.player;

        OnEnter();
    }

    public final boolean OnOptionSelected(int index)
    {
        for (EventOption option : options)
        {
            if (index == option.index)
            {
                if (option.callbacks.isEmpty())
                {
                    event.ProgressPhase();
                }
                else
                {
                    option.OnSelect();
                }
                return true;
            }
        }

        return false;
    }

    protected abstract void OnEnter();

    protected void SetText(String text)
    {
        SetText(text, true);
    }

    protected void SetText(String text, boolean clearAll)
    {
        dialog.updateBodyText(text);

        if (clearAll)
        {
            dialog.clearAllDialogs();
        }
    }

    protected void ClearOptions()
    {
        dialog.clearAllDialogs();
    }

    protected EventOption SetOption(int slot, String text)
    {
        EventOption option = new EventOption(slot, text);
        options.add(option);
        return option;
    }

    protected EventOption SetOption(int slot, String text, AbstractCard card)
    {
        return SetOption(slot, text).SetCard(card);
    }

    protected EventOption SetOption(int slot, String text, AbstractRelic relic)
    {
        return SetOption(slot, text).SetRelic(relic);
    }

    protected void BuildOptions()
    {
        for (EventOption option : options)
        {
            if (option.index >= 0)
            {
                BuildOption(option);
            }
        }
    }

    protected void BuildOption(EventOption option)
    {
        if (option.relic != null)
        {
            if (dialog.optionList.size() > option.index)
            {
                dialog.optionList.set(option.index, new DialogRelicButton(option.index, option.text, option.disabled, option.relic));
            }
            else
            {
                dialog.optionList.add(new DialogRelicButton(option.index, option.text, option.disabled, option.relic));
            }
        }
        else if (option.disabled)
        {
            dialog.updateDialogOption(option.index, option.text, true);
        }
        else if (option.card != null)
        {
            dialog.updateDialogOption(option.index, option.text, option.card);
        }
        else
        {
            dialog.updateDialogOption(option.index, option.text);
        }
    }
}
