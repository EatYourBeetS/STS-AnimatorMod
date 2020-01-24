package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.localization.CardStrings;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public abstract class EYBCardText
{
    public static boolean Toggled;
    public static boolean ToggledOnce;
    public static int NewIndex;
    public boolean canUpdate;
    public int index;

    protected final EYBCard card;
    protected final String[] descriptions;
    protected final String[] upgradedDescriptions;
    protected String overrideDescription;
    protected String overrideSecondaryDescription;

    public abstract void InitializeDescription();
    public abstract void RenderDescription(SpriteBatch sb);
    public abstract void RenderTooltips(SpriteBatch sb);

    public EYBCardText(EYBCard card, CardStrings cardStrings)
    {
        this.card = card;
        this.index = NewIndex;
        this.descriptions = cardStrings.DESCRIPTION.split(Pattern.quote(" || "));
        this.canUpdate = true;

        if (StringUtils.isNotEmpty(cardStrings.UPGRADE_DESCRIPTION))
        {
            String[] temp = cardStrings.UPGRADE_DESCRIPTION.split(Pattern.quote(" || "));
            this.upgradedDescriptions = new String[2];
            this.upgradedDescriptions[0] = temp[0];

            if (temp.length > 1)
            {
                this.upgradedDescriptions[1] = temp[1];
            }
            else if (descriptions.length > 1)
            {
                this.upgradedDescriptions[1] = descriptions[1];
            }
            else
            {
                this.upgradedDescriptions[1] = "-";
            }
        }
        else
        {
            this.upgradedDescriptions = null;
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
            else if (card.upgraded && upgradedDescriptions != null)
            {
                if (upgradedDescriptions.length > index)
                {
                    card.rawDescription = upgradedDescriptions[index];
                }
                else
                {
                    card.rawDescription = "-";
                }
            }
            else
            {
                if (descriptions.length > index)
                {
                    card.rawDescription = descriptions[index];
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
