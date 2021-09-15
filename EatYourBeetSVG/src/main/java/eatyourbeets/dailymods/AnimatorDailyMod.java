package eatyourbeets.dailymods;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.daily.mods.AbstractDailyMod;
import com.megacrit.cardcrawl.localization.RunModStrings;

public class AnimatorDailyMod extends AbstractDailyMod {

    public AnimatorDailyMod(String modID, boolean positive) {
        super(modID, "", "", modID+".png", positive);

        String ID = modID;
        RunModStrings modStrings = CardCrawlGame.languagePack.getRunModString(ID);
        this.name = modStrings.NAME;
        this.description = modStrings.DESCRIPTION;
    }
}
