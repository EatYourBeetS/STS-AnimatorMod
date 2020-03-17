package eatyourbeets.utilities;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.actions.utility.WaitRealtimeAction;
import eatyourbeets.effects.combatOnly.TalkEffect;
import eatyourbeets.effects.player.ObtainRelicEffect;
import eatyourbeets.effects.player.RemoveRelicEffect;
import eatyourbeets.effects.player.SpawnRelicEffect;
import eatyourbeets.effects.utility.CallbackEffect;
import eatyourbeets.effects.utility.CallbackEffect2;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;

import java.util.ArrayList;

public final class GameEffects
{
    public final static GameEffects List = new GameEffects(EffectType.List);
    public final static GameEffects Queue = new GameEffects(EffectType.Queue);
    public final static GameEffects TopLevelList = new GameEffects(EffectType.TopLevelList);
    public final static GameEffects TopLevelQueue = new GameEffects(EffectType.TopLevelQueue);

    protected final EffectType effectType;

    protected GameEffects(EffectType effectType)
    {
        this.effectType = effectType;
    }

    public ArrayList<AbstractGameEffect> GetList()
    {
        switch (effectType)
        {
            case List:
                return AbstractDungeon.effectList;

            case Queue:
                return AbstractDungeon.effectsQueue;

            case TopLevelList:
                return AbstractDungeon.topLevelEffects;

            case TopLevelQueue:
                return AbstractDungeon.topLevelEffectsQueue;
        }

        throw new RuntimeException("Enum value does not exist.");
    }

    public int Count()
    {
        return GetList().size();
    }

    public <T extends AbstractGameEffect> T Add(T effect)
    {
        GetList().add(effect);

        return effect;
    }

    public CallbackEffect2 Callback(AbstractGameEffect effect)
    {
        return Add(new CallbackEffect2(effect));
    }

    public CallbackEffect2 Callback(AbstractGameEffect effect, ActionT0 onCompletion)
    {
        return Add(new CallbackEffect2(effect, onCompletion));
    }

    public CallbackEffect2 Callback(AbstractGameEffect effect, ActionT1<AbstractGameEffect> onCompletion)
    {
        return Add(new CallbackEffect2(effect, onCompletion));
    }

    public CallbackEffect2 Callback(AbstractGameEffect effect, Object state, ActionT2<Object, AbstractGameEffect> onCompletion)
    {
        return Add(new CallbackEffect2(effect, state, onCompletion));
    }

    public CallbackEffect Callback(AbstractGameAction action)
    {
        return Add(new CallbackEffect(action));
    }

    public CallbackEffect Callback(AbstractGameAction effect, ActionT0 onCompletion)
    {
        return Add(new CallbackEffect(effect, onCompletion));
    }

    public CallbackEffect Callback(AbstractGameAction action, ActionT1<AbstractGameAction> onCompletion)
    {
        return Add(new CallbackEffect(action, onCompletion));
    }

    public CallbackEffect Callback(AbstractGameAction action, Object state, ActionT2<Object, AbstractGameAction> onCompletion)
    {
        return Add(new CallbackEffect(action, state, onCompletion));
    }

    public SpawnRelicEffect SpawnRelic(AbstractRelic relic, float x, float y)
    {
        return Add(new SpawnRelicEffect(relic, x, y));
    }

    public ObtainRelicEffect ObtainRelic(AbstractRelic relic)
    {
        return Add(new ObtainRelicEffect(relic));
    }

    public RemoveRelicEffect RemoveRelic(AbstractRelic relic)
    {
        return Add(new RemoveRelicEffect(relic));
    }

    public ShowCardBrieflyEffect ShowCardBriefly(AbstractCard card)
    {
        return Add(new ShowCardBrieflyEffect(card));
    }

    public ShowCardBrieflyEffect ShowCardBriefly(AbstractCard card, float x, float y)
    {
        return Add(new ShowCardBrieflyEffect(card, x, y));
    }

    public TalkEffect Talk(AbstractCreature source, String message)
    {
        return Add(new TalkEffect(source, message));
    }

    public TalkEffect Talk(AbstractCreature source, String message, float duration)
    {
        return Add(new TalkEffect(source, message, duration));
    }

    public CallbackEffect WaitRealtime(float duration)
    {
        return Add(new CallbackEffect(new WaitRealtimeAction(duration)));
    }

    public enum EffectType
    {
        List,
        Queue,
        TopLevelList,
        TopLevelQueue
    }
}