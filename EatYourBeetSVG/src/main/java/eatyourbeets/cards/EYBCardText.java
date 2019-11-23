package eatyourbeets.cards;

import com.megacrit.cardcrawl.localization.CardStrings;
import eatyourbeets.utilities.JavaUtilities;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class EYBCardText
{
    public static boolean Toggled;
    public static int Index;

    protected String overrideDescription;
    protected String overrideSecondaryDescription;

    public boolean canUpdate;
    public int index;

    protected String[] descriptions;
    protected String[] upgradedDescriptions;

    private final EYBCard card;

    public EYBCardText(EYBCard card, CardStrings cardStrings)
    {
        this.card = card;
        this.index = Index;
        this.descriptions = ConvertColorCode(cardStrings.DESCRIPTION).split(Pattern.quote(" || "));
        this.canUpdate = true;

//        String[] ext = cardStrings.EXTENDED_DESCRIPTION;
//        if (ext != null && ext.length > 2 && ext[0].equals("~TIP~"))
//        {
//            this.card.AddExtendedDescription(1, 2);
//        }

        if (StringUtils.isNotEmpty(cardStrings.UPGRADE_DESCRIPTION))
        {
            this.upgradedDescriptions = ConvertColorCode(cardStrings.UPGRADE_DESCRIPTION).split(Pattern.quote(" || "));
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

    public void OverrideDescription(String description, boolean forceRefresh)
    {
        OverrideDescription(description, null, forceRefresh);
    }

    public void OverrideDescription(String description, String secondaryDescription, boolean forceRefresh)
    {
        overrideDescription = ConvertColorCode(description);
        overrideSecondaryDescription = ConvertColorCode(secondaryDescription);

        if (forceRefresh)
        {
            ForceRefresh();
        }
    }

    protected String ConvertColorCode(String string)
    {
        if (string == null || string.equals(""))
        {
            return string;
        }

        StringBuilder sb = new StringBuilder(string.length());

        for (int i = 0; i < string.length(); i++)
        {
            char character = string.charAt(i);
            if ((character == '#') && (i == 0 || string.charAt(i-1) == ' ') && ((i + 1) < string.length()))
            {
                switch(string.charAt(i + 1))
                {
                    case 'b':
                        sb.append("[#87ceeb]");
                        i += 1;
                        break;

                    case 'g':
                        sb.append("[#7fff00]");
                        i += 1;
                        break;

                    case 'r':
                        sb.append("[#ff6563]");
                        i += 1;
                        break;

                    case 'y':
                        sb.append("[#efc851]");
                        i += 1;
                        break;

                    default:
                        JavaUtilities.Logger.warn("Wrong Color Code for " + card.cardID);
                        sb.append(character);
                }
            }
            else
            {
                sb.append(character);
            }
        }

        return sb.toString();
    }
}
