package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.localization.CardStrings;
import eatyourbeets.utilities.JavaUtilities;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class EYBCardText
{
    public static boolean Toggled;
    public static boolean ToggledOnce;
    public static int NewIndex;
    public boolean canUpdate;
    public int index;

    protected final String[] descriptions;
    protected final String[] upgradedDescriptions;
    protected String overrideDescription;
    protected String overrideSecondaryDescription;

    private final EYBCard card;

    public EYBCardText(EYBCard card, CardStrings cardStrings)
    {
        this.card = card;
        this.index = NewIndex;
        this.descriptions = Converter.Convert(cardStrings.DESCRIPTION).split(Pattern.quote(" || "));
        this.canUpdate = true;

//        String[] ext = cardStrings.EXTENDED_DESCRIPTION;
//        if (ext != null && ext.length > 2 && ext[0].equals("~TIP~"))
//        {
//            this.card.AddExtendedDescription(1, 2);
//        }

        if (StringUtils.isNotEmpty(cardStrings.UPGRADE_DESCRIPTION))
        {
            String[] temp = Converter.Convert(cardStrings.UPGRADE_DESCRIPTION).split(Pattern.quote(" || "));
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
        overrideDescription = Converter.Convert(description);
        overrideSecondaryDescription = Converter.Convert(secondaryDescription);

        if (forceRefresh)
        {
            ForceRefresh();
        }
    }

    protected static class Converter
    {
        private static Character lastCharacter;
        private static StringBuilder builder;
        private static String source;
        private static int remaining;
        private static int index;

        public static String Convert(String text)
        {
            if (text == null || text.equals(""))
            {
                return text;
            }

            builder = new StringBuilder();
            source = text;
            remaining = text.length();
            index = 0;

            while (remaining > 0)
            {
                AddCharacter();
            }

            return builder.toString();
        }

        private static void AddCharacter()
        {
            Character character = source.charAt(index);
            if (character == '<' && lastCharacter == ' ' && remaining >= 3)
            {
                lastCharacter = character;
                AddSpecialCode();
            }
            else if (character == '#' && lastCharacter == ' ' && remaining >= 3)
            {
                lastCharacter = character;
                AddColorCode();
            }
            else
            {
                lastCharacter = character;
                builder.append(character);
                MoveIndex(1);
            }
        }

        private static void AddColorCode()
        {
            switch(source.charAt(index + 1))
            {
                case 'b':
                    builder.append("[#87ceeb]");
                    break;

                case 'g':
                    builder.append("[#7fff00]");
                    break;

                case 'r':
                    builder.append("[#ff6563]");
                    break;

                case 'y':
                    builder.append("[#efc851]");
                    break;

                case 'R':
                    builder.append("[#ef916b]");
                    break;

                case 'B':
                    builder.append("[#49b3dd]");
                    break;

                case 'G':
                    builder.append("[#8edb00]");
                    break;

                default:
                    JavaUtilities.Logger.warn("Unknown Color Code: " + source);
                    builder.append(lastCharacter);
                    MoveIndex(1);
                    return;
            }

            MoveIndex(2);
        }

        private static void AddSpecialCode()
        {
            // @A:1.0, @F:0.2
            char character =  source.charAt(index + 1);
            switch (character)
            {
                case 'A':
                    TryAddScaling("([#8edb00]", " [G] )");
                    break;

                case 'F':
                    TryAddScaling("([#8edb00]", " [R] )");
                    break;

                case 'I':
                    TryAddScaling("([#8edb00]", " [B] )");
                    break;

                default:
                    JavaUtilities.Logger.warn("Unknown Special Code: " + source);
                    builder.append(lastCharacter);
                    MoveIndex(1);
                    break;
            }
        }

        private static void TryAddScaling(String prefix, String suffix)
        {
            builder.append(prefix);
            MoveIndex(2);

            while (remaining > 0)
            {
                char character = source.charAt(index);
                if (character == '>')
                {
                    lastCharacter = character;
                    builder.append(suffix);
                    MoveIndex(1);
                    return;
                }
                else
                {
                    lastCharacter = character;
                    builder.append(character);
                    MoveIndex(1);
                }
            }
        }

        private static boolean MoveIndex(int amount)
        {
            index += amount;
            remaining = source.length() - index;

            return remaining > 0;
        }
    }
}
