package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.localization.CardStrings;
import org.apache.commons.lang3.StringUtils;

public abstract class EYBCardText
{
    public static boolean Toggled;
    public static boolean ToggledOnce;
    public static int NewIndex;
    public boolean canUpdate;
    public int index;

    protected final EYBCard card;
    protected final String description;
    protected final String upgradedDescription;
    protected String overrideDescription;
    protected String overrideSecondaryDescription;

    public abstract void InitializeDescription();
    public abstract void RenderDescription(SpriteBatch sb);
    public abstract void RenderTooltips(SpriteBatch sb);

    public EYBCardText(EYBCard card, CardStrings cardStrings)
    {
        this.card = card;
        this.index = NewIndex;
        this.canUpdate = true;
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

    public void Update(boolean forceUpdate)
    {
        if (forceUpdate || this.index != NewIndex)
        {
            this.index = NewIndex;

            if (index == 0 && overrideDescription != null)
            {
                card.rawDescription = overrideDescription;
            }
            else if (overrideSecondaryDescription != null)
            {
                card.rawDescription = overrideSecondaryDescription;
            }
            else if (card.upgraded && upgradedDescription != null)
            {
                if (index == 0)
                {
                    card.rawDescription = upgradedDescription;
                }
                else
                {
                    card.rawDescription = "-";
                }
            }
            else
            {
                if (index == 0)
                {
                    card.rawDescription = description;
                }
                else
                {
                    card.rawDescription = "-";
                }
            }

            card.initializeDescription();
        }
    }

    public void ForceRefresh()
    {
        Update(true);
    }

    public void OverrideDescription(String description, boolean forceRefresh)
    {
        OverrideDescription(description, null, forceRefresh);
    }

    public void OverrideDescription(String description, String secondaryDescription, boolean forceRefresh)
    {
        overrideDescription = description;
        overrideSecondaryDescription = secondaryDescription;

        if (forceRefresh)
        {
            ForceRefresh();
        }
    }
}
