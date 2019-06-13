package eatyourbeets.potions;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import eatyourbeets.utilities.GameActionsHelper;

public class FalseLifePotion extends AbstractPotion
{
    public static final String POTION_ID = "animator:FalseLifePotion";
    private static final PotionStrings potionStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public FalseLifePotion()
    {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.HEART, PotionColor.ANCIENT);
        this.potency = this.getPotency();
        this.description = DESCRIPTIONS[0] + this.potency + DESCRIPTIONS[1];
        this.isThrown = false;
        this.tips.add(new PowerTip(this.name, this.description));
    }

    public void use(AbstractCreature target)
    {
        GameActionsHelper.GainTemporaryHP(AbstractDungeon.player, AbstractDungeon.player, this.potency);
    }

    public AbstractPotion makeCopy()
    {
        return new FalseLifePotion();
    }

    public int getPotency(int ascensionLevel)
    {
        if (ascensionLevel < 7)
        {
            return 8;
        }
        else if (ascensionLevel < 14)
        {
            return 10;
        }
        else
        {
            return 12;
        }
    }

    static
    {
        potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
        NAME = potionStrings.NAME;
        DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    }
}
