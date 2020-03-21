package eatyourbeets.effects.card;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.EYBEffect;

public class UnfadeOutEffect extends EYBEffect
{
    private final AbstractCard card;

    public UnfadeOutEffect(AbstractCard card)
    {
        super(0, Settings.ACTION_DUR_MED, true);

        this.card = card;

        UnfadeOut();
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            UnfadeOut();
        }
    }

    protected void UnfadeOut()
    {
        if (card.fadingOut)
        {
            card.unfadeOut();
        }
    }
}