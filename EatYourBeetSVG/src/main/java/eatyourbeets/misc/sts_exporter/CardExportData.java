package eatyourbeets.misc.sts_exporter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.base.AnimatorCard;

import java.lang.reflect.Method;

public class CardExportData implements Comparable<CardExportData>
{
    public AnimatorCard card;
    public CardExportData upgrade;
    public ExportPath image;
    public ModExportData mod;

    public CardExportData(ExportHelper export, AnimatorCard card)
    {
        this(export, card, true);
    }

    public CardExportData(ExportHelper export, AnimatorCard card, boolean exportUpgrade)
    {
        this.card = card;
        this.card.initializeDescription();
        this.mod = export.findMod(card.getClass());

        if (exportUpgrade && !card.upgraded && card.canUpgrade())
        {
            final AnimatorCard copy = (AnimatorCard) card.makeCopy();
            copy.upgrade();
            copy.displayUpgrades();
            this.upgrade = new CardExportData(export, copy, false);
        }

        // image
        this.image = export.exportPath(this.mod, "Images/Cards/", card.name, ".png");
    }

    public void exportImages()
    {
        image.mkdir();
        exportImageToFileLowResolution(image.absolute);

        if (upgrade != null)
        {
            upgrade.image.mkdir();
            upgrade.exportImageToFileLowResolution(upgrade.image.absolute);
        }
    }

    private void exportImageToFileLowResolution(String imageFile)
    {
        // This is the in-game rendering path
        // Scale and position of the card
        // IMG_WIDTH,IMG_HEIGHT are only for the card border, mana cost and rarity banner is outside that, so add some padding.
        card.drawScale = 1.0f;
        float width = (AbstractCard.IMG_WIDTH + 24.0f) * card.drawScale;
        float height = (AbstractCard.IMG_HEIGHT + 24.0f) * card.drawScale;
        int i_width = Math.round(width), i_height = Math.round(height);
        card.current_x = width / 2;
        card.current_y = height / 2;
        // Render card to png file
        ExportHelper.renderSpriteBatchToPNG(0, 0, width, height, i_width, i_height, imageFile, (SpriteBatch sb) -> card.render(sb, false));
    }

    public static Object callPrivate(Object obj, Class<?> objClass, String methodName, Class<?> paramType, Object arg)
    {
        try
        {
            Method method = objClass.getDeclaredMethod(methodName, paramType);
            method.setAccessible(true);
            return method.invoke(obj, arg);
        }
        catch (Exception ex)
        {
            Exporter.logger.error("Exception occurred when calling private method " + methodName + " of " + objClass.getName(), ex);
            return null;
        }
    }

    @Override
    public int compareTo(CardExportData other)
    {
        int result = card.color.compareTo(other.card.color);
        if (result == 0)
        {
            result = card.rarity.compareTo(other.card.rarity);
        }
        if (result == 0)
        {
            result = card.name.compareTo(other.card.name);
        }

        return result;
    }
}