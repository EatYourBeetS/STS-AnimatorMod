package eatyourbeets.events.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public abstract class EYBEventPhase<Event extends EYBEvent, Text extends EYBEventStrings>
{
    public final ArrayList<EYBEventOption> options = new ArrayList<>();
    public final AbstractPlayer player = AbstractDungeon.player;
    public final Random RNG = AbstractDungeon.miscRng;

    public int index;
    public Text text;
    public Event event;
    public GenericEventDialog dialog;

    protected final boolean OnOptionSelected(int index)
    {
        if (options.size() > index)
        {
            EYBEventOption option = options.get(index);
            if (option.callbacks.isEmpty())
            {
                ProgressPhase();
            }
            else
            {
                option.OnSelect();
            }

            return true;
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
        options.clear();
    }

    protected EYBEventOption SetOption(String text)
    {
        EYBEventOption option = new EYBEventOption(text);
        options.add(option);
        return option;
    }

    protected EYBEventOption SetOption(String text, AbstractCard card)
    {
        return SetOption(text).SetCard(card);
    }

    protected EYBEventOption SetOption(String text, AbstractRelic relic)
    {
        return SetOption(text).SetRelic(relic);
    }

    protected void BuildOptions()
    {
        for (int i = 0; i < options.size(); i++)
        {
            BuildOption(options.get(i), i);
        }
    }

    protected void BuildOption(EYBEventOption option, int index)
    {
        if (option.relic != null)
        {
            if (dialog.optionList.size() > index)
            {
                dialog.optionList.set(index, new DialogRelicButton(index, option.text, option.disabled, option.relic));
            }
            else
            {
                dialog.optionList.add(new DialogRelicButton(index, option.text, option.disabled, option.relic));
            }
        }
        else if (option.disabled)
        {
            dialog.updateDialogOption(index, option.text, true);
        }
        else if (option.card != null)
        {
            dialog.updateDialogOption(index, option.text, option.card);
        }
        else
        {
            dialog.updateDialogOption(index, option.text);
        }
    }

    protected static int GetMaxHP(float percentage)
    {
        return (int)Math.ceil(AbstractDungeon.player.maxHealth * percentage / 100f);
    }

    protected void OpenMap()
    {
        event.OpenMap();
    }

    protected void ProgressPhase()
    {
        event.ProgressPhase();
    }

    protected void ChangePhase(Class<? extends EYBEventPhase> newPhase)
    {
        event.ChangePhase(newPhase);
    }

    protected void ChangePhase(int newPhase)
    {
        event.ChangePhase(newPhase);
    }

    protected void Refresh()
    {
        ClearOptions();
        OnEnter();
        BuildOptions();
    }
}
