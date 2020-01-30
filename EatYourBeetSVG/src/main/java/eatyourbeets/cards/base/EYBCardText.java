package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.localization.CardStrings;
import org.apache.commons.lang3.StringUtils;

public abstract class EYBCardText
{
    protected final EYBCard card;
    protected final String description;
    protected final String upgradedDescription;
    protected String overrideDescription;

    public abstract void InitializeDescription();
    public abstract void RenderDescription(SpriteBatch sb);
    public abstract void RenderTooltips(SpriteBatch sb);

    public EYBCardText(EYBCard card, CardStrings cardStrings)
    {
        this.card = card;
        this.description = cardStrings.DESCRIPTION;

        if (StringUtils.isNotEmpty(cardStrings.UPGRADE_DESCRIPTION))
        {
            this.upgradedDescription = cardStrings.UPGRADE_DESCRIPTION;
        }
        else
        {
            this.upgradedDescription = null;
        }
    }

    public void ForceRefresh()
    {
        if (overrideDescription != null)
        {
            card.rawDescription = overrideDescription;
        }
        else if (card.upgraded && upgradedDescription != null)
        {
            card.rawDescription = upgradedDescription;
        }
        else
        {
            card.rawDescription = description;
        }

        InitializeDescription();
    }

    public void OverrideDescription(String description, boolean forceRefresh)
    {
        overrideDescription = description;

        if (forceRefresh)
        {
            ForceRefresh();
        }
    }
}
