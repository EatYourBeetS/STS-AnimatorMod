package pinacolada.effects.card;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import pinacolada.effects.PCLEffect;

public class UnfadeOutEffect extends PCLEffect
{
    private final AbstractCard card;

    public UnfadeOutEffect(AbstractCard card)
    {
        super(Settings.ACTION_DUR_MED, true);

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