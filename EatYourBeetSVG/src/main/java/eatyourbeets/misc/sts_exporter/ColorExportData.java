package eatyourbeets.misc.sts_exporter;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;

public class ColorExportData
{
    public AbstractCard.CardColor color;
    public String id;
    public String name;
    public ArrayList<CardExportData> cards = new ArrayList<>();
    public ArrayList<RelicExportData> relics = new ArrayList<>();

    public ColorExportData(ExportHelper export, AbstractCard.CardColor color)
    {
        this.color = color;
        this.id = color.toString();
        this.name = Exporter.colorName(color);
    }

    public static ArrayList<ColorExportData> exportAllColors(ExportHelper export)
    {
        ArrayList<ColorExportData> colors = new ArrayList<>();
        for (AbstractCard.CardColor color : AbstractCard.CardColor.values())
        {
            colors.add(new ColorExportData(export, color));
        }
        return colors;
    }
}