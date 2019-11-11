package eatyourbeets.cards;

import com.megacrit.cardcrawl.localization.CardStrings;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class EYBCardText
{
    public static boolean Toggled;
    public static int Index;

    public String overrideDescription;
    public String overrideSecondaryDescription;
    public boolean canUpdate;
    public int index;

    protected String[] descriptions;
    protected String[] upgradedDescriptions;

    private final EYBCard card;

    public EYBCardText(EYBCard card, CardStrings cardStrings)
    {
        this.card = card;
        this.index = Index;
        this.descriptions = cardStrings.DESCRIPTION.split(Pattern.quote(" || "));
        this.canUpdate = true;

        String[] ext = cardStrings.EXTENDED_DESCRIPTION;
        if (ext != null && ext.length > 2 && ext[0].equals("~TIP~"))
        {
            this.card.AddExtendedDescription(1, 2);
        }

        if (StringUtils.isNotEmpty(cardStrings.UPGRADE_DESCRIPTION))
        {
            this.upgradedDescriptions = cardStrings.UPGRADE_DESCRIPTION.split(Pattern.quote(" || "));
        }
        else
        {
            this.upgradedDescriptions = null;
        }
    }

    public void Update(boolean forceUpdate)
    {
        if (forceUpdate || this.index != Index)
        {
            this.index = Index;

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
}
