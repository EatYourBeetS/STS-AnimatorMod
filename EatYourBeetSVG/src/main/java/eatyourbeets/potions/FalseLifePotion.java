package eatyourbeets.potions;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class FalseLifePotion extends AbstractPotion
{
    public static final String POTION_ID = "animator:FalseLifePotion";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public FalseLifePotion()
    {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.HEART, PotionColor.ANCIENT);
    }

    @Override
    public void initializeData()
    {
        this.potency = this.getPotency();
        this.description = JUtils.Format(DESCRIPTIONS[0], this.potency);
        this.isThrown = false;
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target)
    {
        GameActions.Bottom.GainTemporaryHP(this.potency);
    }

    @Override
    public AbstractPotion makeCopy()
    {
        return new FalseLifePotion();
    }

    @Override
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
}
