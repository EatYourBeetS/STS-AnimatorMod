package eatyourbeets.utilities;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.actions.special.GainGold;

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

    public enum Type
    {
        List,
        Queue,
        TopLevelList,
        TopLevelQueue
    }
}