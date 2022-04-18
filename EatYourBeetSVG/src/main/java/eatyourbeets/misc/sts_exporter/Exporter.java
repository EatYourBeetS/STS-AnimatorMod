package eatyourbeets.misc.sts_exporter;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.cards.base.AnimatorCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class Exporter
{
    public static final Logger logger = LogManager.getLogger(Exporter.class.getName());

    public static final String CONFIG_EXPORT_AT_START = "export_at_startup";
    public static final String CONFIG_INCLUDE_BASE_GAME = "include_base_game";
    public static final String CONFIG_RENDER_IMAGES = "render_images";
    public static final String CONFIG_EXPORT_DIR = "export_dir";

    public static void ExportAll(ArrayList<AnimatorCard> cards)
    {
        final ExportHelper helper = new ExportHelper();
        for (AnimatorCard c : cards)
        {
            c.drawScale = 1 / Settings.scale;
            new CardExportData(helper, c, true).exportImages();
        }
    }

    public static String typeString(AbstractCard.CardType type)
    {
        switch (type)
        {
            case ATTACK:
            {
                return AbstractCard.TEXT[0];
            }
            case SKILL:
            {
                return AbstractCard.TEXT[1];
            }
            case POWER:
            {
                return AbstractCard.TEXT[2];
            }
            case STATUS:
            {
                return AbstractCard.TEXT[7];
            }
            case CURSE:
            {
                return AbstractCard.TEXT[3];
            }
            default:
            {
                return AbstractCard.TEXT[5];
            }
        }
    }

    public static String toTitleCase(String input)
    {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;
        for (char c : input.toCharArray())
        {
            if (Character.isSpaceChar(c))
            {
                nextTitleCase = true;
            }
            else if (nextTitleCase)
            {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }
            else
            {
                c = Character.toLowerCase(c);
            }
            titleCase.append(c);
        }
        return titleCase.toString();
    }

    public static String rarityName(AbstractCard.CardRarity rarity)
    {
        return toTitleCase(rarity.toString()); // TODO: localize?
    }

    public static String rarityName(AbstractPotion.PotionRarity rarity)
    {
        return toTitleCase(rarity.toString()); // TODO: localize?
    }

    public static String tierName(AbstractRelic.RelicTier tier)
    {
        return toTitleCase(tier.toString()); // TODO: localize?
    }

    public static String colorName(AbstractCard.CardColor color)
    {
        return toTitleCase(color.toString()); // TODO: localize?
    }
}