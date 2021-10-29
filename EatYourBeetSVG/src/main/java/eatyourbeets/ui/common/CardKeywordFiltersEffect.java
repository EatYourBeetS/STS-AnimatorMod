package eatyourbeets.ui.common;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.resources.GR;

public class CardKeywordFiltersEffect extends EYBEffect
{
    private final CardKeywordFilters screen;

    public CardKeywordFiltersEffect(CardKeywordFilters screen)
    {
        super(Settings.ACTION_DUR_FAST, true);

        this.screen = screen;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        GR.UI.CardFilters.TryUpdate();
        if (TickDuration(deltaTime))
        {
            if (!screen.isActive)
            {
                Complete();
            }
        }
    }

    public void render(SpriteBatch sb)
    {
        GR.UI.CardFilters.TryRender(sb);
    }

}
