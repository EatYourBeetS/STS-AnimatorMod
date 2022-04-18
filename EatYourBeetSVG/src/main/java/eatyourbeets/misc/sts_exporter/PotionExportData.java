package eatyourbeets.misc.sts_exporter;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

class PotionExportData implements Comparable<PotionExportData>
{
    public AbstractPotion potion;
    public ModExportData mod;
    public ExportPath image;
    public String id, name, rarity;
    public String description, descriptionHTML, descriptionPlain;
    public String playerClass;

    PotionExportData(ExportHelper export, AbstractPotion potion, AbstractPlayer.PlayerClass cls)
    {
        this.potion = potion;
        this.mod = export.findMod(potion.getClass());
        this.mod.potions.add(this);
        this.id = potion.ID;
        this.name = potion.name;
        this.description = potion.description;
        this.descriptionHTML = RelicExportData.smartTextToHTML(potion.description, true, true);
        this.descriptionPlain = RelicExportData.smartTextToPlain(potion.description, true, true);
        this.rarity = Exporter.rarityName(potion.rarity);
        this.playerClass = playerClass == null ? "" : playerClass;
        this.image = export.exportPath(this.mod, "potions", this.name, ".png");
    }

    public void exportImages()
    {
        this.image.mkdir();
        exportImageToFile(this.image.absolute);
    }

    // Note: We can't use SingleRelicViewPopup, because that plays a sound.
    private void exportImageToFile(String imageFile)
    {
        Exporter.logger.info("Rendering potion image to " + imageFile);
        // Render to a png
        potion.move(32.0f, 32.0f);
        float width = 64.0f, height = 64.0f;
        float x = 0;
        float y = 0;
        float x_padding = 0.0f;
        float y_padding = 0.0f;
        ExportHelper.renderSpriteBatchToPNG(x - x_padding, y - y_padding, width + 2 * x_padding, height + 2 * y_padding, 1.0f, imageFile, potion::render);
    }

    public static ArrayList<PotionExportData> exportAllPotions(ExportHelper export)
    {
        ArrayList<PotionExportData> potions = new ArrayList<>();
        for (HashMap.Entry<String, AbstractPlayer.PlayerClass> potionID : getAllPotionIds().entrySet())
        {
            potions.add(new PotionExportData(export, PotionHelper.getPotion(potionID.getKey()), potionID.getValue()));
        }
        Collections.sort(potions);
        return potions;
    }

    public static HashMap<String, AbstractPlayer.PlayerClass> getAllPotionIds()
    {
        HashMap<String, AbstractPlayer.PlayerClass> potions = new HashMap<>();
        for (AbstractPlayer.PlayerClass playerClass : AbstractPlayer.PlayerClass.values())
        {
            PotionHelper.initialize(playerClass);
            for (String potionID : PotionHelper.potions)
            {
                if (potions.containsKey(potionID))
                {
                    potions.put(potionID, null);
                }
                else
                {
                    potions.put(potionID, playerClass);
                }
            }
        }
        return potions;
    }

    @Override
    public int compareTo(PotionExportData that)
    {
        if (potion.rarity != that.potion.rarity)
        {
            return potion.rarity.compareTo(that.potion.rarity);
        }
        return name.compareTo(that.name);
    }
}