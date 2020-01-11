package eatyourbeets.utilities;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.actions.utility.WaitRealtimeAction;
import eatyourbeets.effects.player.ObtainRelicEffect;
import eatyourbeets.effects.player.RemoveRelicEffect;
import eatyourbeets.effects.utility.CallbackEffect;
import eatyourbeets.effects.utility.CallbackEffect2;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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

    public CallbackEffect2 Callback(AbstractGameEffect effect, Consumer<AbstractGameEffect> onCompletion)
    {
        return Add(new CallbackEffect2(effect, onCompletion));
    }

    public CallbackEffect2 Callback(AbstractGameEffect effect, Object state, BiConsumer<Object, AbstractGameEffect> onCompletion)
    {
        return Add(new CallbackEffect2(effect, state, onCompletion));
    }

    public CallbackEffect Callback(AbstractGameAction action)
    {
        return Add(new CallbackEffect(action));
    }

    public CallbackEffect Callback(AbstractGameAction action, Consumer<AbstractGameAction> onCompletion)
    {
        return Add(new CallbackEffect(action, onCompletion));
    }

    public CallbackEffect Callback(AbstractGameAction action, Object state, BiConsumer<Object, AbstractGameAction> onCompletion)
    {
        return Add(new CallbackEffect(action, state, onCompletion));
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