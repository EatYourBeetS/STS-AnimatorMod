package eatyourbeets.effects.card;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.effects.EYBEffect;

public class RenderCardEffect extends EYBEffect
{
    private final AbstractCard card;

    public RenderCardEffect(AbstractCard card, float duration, boolean isRealtime)
    {
        super(duration, isRealtime);

        this.card = card;
    }

    public void render(SpriteBatch sb)
    {
        card.render(sb);
    }

    public void dispose()
    {

    }
}