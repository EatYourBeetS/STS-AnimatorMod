package eatyourbeets.events.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import eatyourbeets.utilities.GameEffects;

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

    protected abstract void OnEnter();

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

    protected void AddText(String text)
    {
        AddText(text, true);
    }

    protected void AddText(String text, boolean clearAll)
    {
        dialog.updateBodyText(text);

        if (clearAll)
        {
            ClearOptions();
        }
    }

    protected void ClearOptions()
    {
        dialog.clearAllDialogs();
        options.clear();
    }

    protected EYBEventOption AddOption(String text)
    {
        EYBEventOption option = new EYBEventOption(text);
        options.add(option);
        return option;
    }

    protected EYBEventOption AddOption(String text, AbstractCard card)
    {
        return AddOption(text).SetCard(card);
    }

    protected EYBEventOption AddOption(String text, AbstractRelic relic)
    {
        return AddOption(text).SetRelic(relic);
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

    // Helper Methods for commonly used code

    protected static int GetMaxHP(float percentage)
    {
        return (int) Math.ceil(AbstractDungeon.player.maxHealth * percentage / 100f);
    }

    protected EYBEventOption AddLeaveOption()
    {
        return AddOption(EYBEvent.COMMON_STRINGS.Leave()).AddCallback(this::OpenMap);
    }

    protected EYBEventOption AddContinueOption()
    {
        return AddOption(EYBEvent.COMMON_STRINGS.Continue()).AddCallback(this::ProgressPhase);
    }

    protected EYBEventOption AddPhaseChangeOption(String text, Class<? extends EYBEventPhase> phase)
    {
        return AddOption(text).AddCallback(phase, (newPhase, __) -> this.ChangePhase(newPhase));
    }

    protected void RemoveCard(AbstractCard card, boolean playSound)
    {
        if (playSound)
        {
            CardCrawlGame.sound.play("CARD_EXHAUST");
        }

        GameEffects.TopLevelList.Add(new PurgeCardEffect(card));
        player.masterDeck.removeCard(card);
        AbstractEvent.logMetricCardRemoval(event.strings.name, "Removed", card);
    }
}
