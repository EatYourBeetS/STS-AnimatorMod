package pinacolada.ui.common;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.utilities.GenericCallback;

public class ControllableCard
{
    public ControllableCardPile SourcePile;
    public final AbstractCard card;

    protected boolean enabled;
    protected FuncT1<Boolean, ControllableCard> useCondition;
    protected GenericCallback<ControllableCard> onUpdate;
    protected GenericCallback<ControllableCard> onSelect;
    protected GenericCallback<ControllableCard> onDelete;

    public ControllableCard(ControllableCardPile sourcePile, AbstractCard card)
    {
        this.SourcePile = sourcePile;
        this.card = card;
        this.enabled = true;
    }

    public ControllableCard SetUseCondition(FuncT1<Boolean, ControllableCard> useCondition) {
        this.useCondition = useCondition;

        return this;
    }

    public <S> ControllableCard OnUpdate(S state, ActionT2<S, ControllableCard> onCompletion)
    {
        onUpdate = GenericCallback.FromT2(onCompletion, state);

        return this;
    }

    public ControllableCard OnUpdate(ActionT1<ControllableCard> onCompletion)
    {
        onUpdate = GenericCallback.FromT1(onCompletion);

        return this;
    }

    public <S> ControllableCard OnSelect(S state, ActionT2<S, ControllableCard> onCompletion)
    {
        onSelect = GenericCallback.FromT2(onCompletion, state);

        return this;
    }

    public ControllableCard OnSelect(ActionT1<ControllableCard> onCompletion)
    {
        onSelect = GenericCallback.FromT1(onCompletion);

        return this;
    }

    public <S> ControllableCard OnDelete(S state, ActionT2<S, ControllableCard> onCompletion)
    {
        onDelete = GenericCallback.FromT2(onCompletion, state);

        return this;
    }

    public ControllableCard OnDelete(ActionT1<ControllableCard> onCompletion)
    {
        onDelete = GenericCallback.FromT1(onCompletion);

        return this;
    }

    public boolean CanUse() {
        return enabled && AbstractDungeon.getCurrMapNode() != null && card != null && (useCondition == null || useCondition.Invoke(this));
    }

    public boolean IsEnabled()
    {
        return enabled;
    }


    public void SetEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void Delete()
    {
        SourcePile.Remove(this);

        if (onDelete != null)
        {
            onDelete.Complete(this);
        }
    }

    public void Update()
    {
        if (onUpdate != null)
        {
            onUpdate.Complete(this);
        }
    }

    public void Select()
    {
        if (onSelect != null)
        {
            onSelect.Complete(this);
        }
    }

    public void Update(ControllableCardPile pile)
    {
        card.update();
    }

    public void Render(SpriteBatch sb)
    {
        card.render(sb);
    }
}
