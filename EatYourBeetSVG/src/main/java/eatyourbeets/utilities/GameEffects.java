package eatyourbeets.utilities;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.actions.utility.WaitRealtimeAction;
import eatyourbeets.effects.utility.CallbackEffect;
import eatyourbeets.effects.utility.CallbackEffect2;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

// TODO: Other effects
public final class GameEffects
{
    public final static GameEffects List = new GameEffects(Type.List);
    public final static GameEffects Queue = new GameEffects(Type.Queue);
    public final static GameEffects TopLevelList = new GameEffects(Type.TopLevelList);
    public final static GameEffects TopLevelQueue = new GameEffects(Type.TopLevelQueue);

    protected final Type effectType;

    protected GameEffects(Type effectType)
    {
        this.effectType = effectType;
    }

    public <T extends AbstractGameEffect> T Add(T effect)
    {
        switch (effectType)
        {
            case List:
                AbstractDungeon.effectList.add(effect);
                break;

            case Queue:
                AbstractDungeon.effectsQueue.add(effect);
                break;

            case TopLevelList:
                AbstractDungeon.topLevelEffects.add(effect);
                break;

            case TopLevelQueue:
                AbstractDungeon.topLevelEffectsQueue.add(effect);
                break;
        }

        return effect;
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

    public enum Type
    {
        List,
        Queue,
        TopLevelList,
        TopLevelQueue
    }
}