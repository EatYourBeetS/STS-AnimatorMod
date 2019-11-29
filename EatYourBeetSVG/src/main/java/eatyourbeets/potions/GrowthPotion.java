package eatyourbeets.potions;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.JavaUtilities;

public class GrowthPotion extends AbstractPotion
{
    public static final String POTION_ID = "animator:GrowthPotion";
    private static final PotionStrings potionStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public GrowthPotion()
    {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.S, PotionColor.NONE);
        this.potency = this.getPotency();
        this.description = JavaUtilities.Format(DESCRIPTIONS[0], this.potency);
        this.isThrown = false;
        this.tips.add(new PowerTip(this.name, this.description));
    }

    public void use(AbstractCreature target)
    {
        GameActionsHelper.GainForce(potency);
        GameActionsHelper.GainIntellect(potency);
        GameActionsHelper.GainAgility(potency);
    }

    public AbstractPotion makeCopy()
    {
        return new GrowthPotion();
    }

    public int getPotency(int ascensionLevel)
    {
        return 2;
    }

    static
    {
        potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
        NAME = potionStrings.NAME;
        DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    }
}
