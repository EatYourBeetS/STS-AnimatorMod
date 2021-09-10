package eatyourbeets.cards.base;

import java.util.ArrayList;

public class EYBCardSaveData
{
    public int form;
    public ArrayList<String> additionalData = new ArrayList<>();

    public EYBCardSaveData() {
        this.form = 0;
    }

    public EYBCardSaveData(int form) {
        this.form = form;
    }

    public EYBCardSaveData(int form, ArrayList<String> additionalData) {
        this.form = form;
        if (additionalData != null) {
            this.additionalData = new ArrayList<>();
            this.additionalData.addAll(additionalData);
        }
    }
}
