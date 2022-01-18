package pinacolada.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;

public class PCLCardSaveData
{
    public int form;
    public int modifiedDamage;
    public int modifiedBlock;
    public int modifiedMagicNumber;
    public int modifiedSecondaryValue;
    public int modifiedHitCount;
    public int modifiedCost;
    public int[] modifiedAffinities;
    public int[] modifiedScaling;
    public ArrayList<AbstractCard.CardTags> modifiedTags = new ArrayList<>();
    public ArrayList<String> additionalData = new ArrayList<>();

    public PCLCardSaveData() {
        this.form = 0;
    }

    public PCLCardSaveData(int form) {
        this.form = form;
    }

    public PCLCardSaveData(int form, ArrayList<String> additionalData) {
        this.form = form;
        if (additionalData != null) {
            this.additionalData = new ArrayList<>();
            this.additionalData.addAll(additionalData);
        }
    }

    public PCLCardSaveData(PCLCardSaveData original) {
        this.form = original.form;
        this.modifiedDamage = original.modifiedDamage;
        this.modifiedBlock = original.modifiedBlock;
        this.modifiedMagicNumber = original.modifiedMagicNumber;
        this.modifiedSecondaryValue = original.modifiedSecondaryValue;
        this.modifiedHitCount = original.modifiedHitCount;
        this.modifiedCost = original.modifiedCost;
        this.modifiedAffinities = original.modifiedAffinities;
        this.modifiedScaling = original.modifiedScaling;
        if (original.modifiedTags != null) {
            this.modifiedTags = new ArrayList<>();
            this.modifiedTags.addAll(original.modifiedTags);
        }
        if (original.additionalData != null) {
            this.additionalData = new ArrayList<>();
            this.additionalData.addAll(original.additionalData);
        }
    }
}
