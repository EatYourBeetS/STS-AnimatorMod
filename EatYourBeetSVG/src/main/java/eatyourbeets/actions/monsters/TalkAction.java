package eatyourbeets.actions.monsters;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.ui.DialogWord;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.effects.combatOnly.TalkEffect;
import eatyourbeets.utilities.GameEffects;

public class TalkAction extends EYBActionWithCallback<AbstractCreature>
{
    protected float effectDuration;
    protected DialogWord.AppearEffect appearEffect;

    public TalkAction(AbstractCreature source, String message)
    {
        this(source, message, 2.0F, 2.0F);
    }

    public TalkAction(AbstractCreature source, String message, float duration, float effectDuration)
    {
        super(ActionType.TEXT, duration);

        this.effectDuration = effectDuration;
        this.isRealtime = true;
        this.message = message;
        this.source = source;
    }

    public TalkAction SetEffect(DialogWord.AppearEffect appearEffect)
    {
        this.appearEffect = appearEffect;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        GameEffects.List.Add(new TalkEffect(source, message)).SetEffect(appearEffect).SetDuration(effectDuration, isRealtime);
    }
}