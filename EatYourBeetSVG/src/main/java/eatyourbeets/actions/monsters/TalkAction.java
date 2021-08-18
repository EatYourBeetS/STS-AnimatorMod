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
        this(source, message, 1.5f, 2f);
    }

    public TalkAction(AbstractCreature source, String message, float actionDuration, float effectDuration)
    {
        super(ActionType.TEXT, actionDuration);

        this.effectDuration = effectDuration;
        this.isRealtime = true;
        this.message = message;

        Initialize(source, 1);
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