package eatyourbeets.cards;

import com.megacrit.cardcrawl.localization.CardStrings;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class EYBCardText
{
    public static boolean Toggled;

    public String overrideDescription;
    public boolean canUpdate;
    public int index;

    protected String[] descriptions;
    protected String[] upgradedDescriptions;

    private final EYBCard card;

    public EYBCardText(EYBCard card, CardStrings cardStrings)
    {
        this.card = card;
        this.index = 0;
        this.descriptions = cardStrings.DESCRIPTION.split(Pattern.quote(" || "));
        this.canUpdate = true;

        if (StringUtils.isNotEmpty(cardStrings.UPGRADE_DESCRIPTION))
        {
            this.upgradedDescriptions = cardStrings.UPGRADE_DESCRIPTION.split(Pattern.quote(" || "));
        }
        else
        {
            this.upgradedDescriptions = null;
        }
    }

    public void Update(int index, boolean forceUpdate)
    {
        if (forceUpdate || this.index != index)
        {
            this.index = index;

            if (overrideDescription != null)
            {
                card.rawDescription = overrideDescription;
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
}
