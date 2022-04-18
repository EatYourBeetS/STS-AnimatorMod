package eatyourbeets.potions;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import eatyourbeets.relics.animator.unnamedReign.TheEgnaroPiece;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class FalseLifePotion extends AbstractPotion
{
    public static final String POTION_ID = GR.Animator.CreateID(FalseLifePotion.class.getSimpleName());
    public static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    private PowerTip descriptionTip;
    private TheEgnaroPiece relic;

    public FalseLifePotion()
    {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.HEART, PotionColor.ANCIENT);
    }

    @Override
    public void initializeData()
    {
        this.isThrown = false;
        this.potency = this.getPotency();
        UpdateDescription();
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
    public void setAsObtained(int potionSlot)
    {
        super.setAsObtained(potionSlot);

        if ((relic = GameUtilities.GetRelic(TheEgnaroPiece.ID)) != null)
        {
            potency += relic.GetFalseLifePotionPowerIncrease();
            UpdateDescription();
            relic.flash();
            this.flash();
        }
    }

    @Override
    public int getPotency(int ascensionLevel)
    {
        int result = (ascensionLevel < 7) ? 8 : (ascensionLevel < 14) ? 10 : 12;
        if (relic != null)
        {
            result += relic.GetFalseLifePotionPowerIncrease();
        }

        return result;
    }

    protected void UpdateDescription()
    {
        this.description = JUtils.Format(DESCRIPTIONS[0], this.potency) + (relic != null ? (" NL " + relic.GetFalseLifePotionString()) : "");
        if (descriptionTip == null)
        {
            this.descriptionTip = new PowerTip(name, description);
        }
        else
        {
            this.descriptionTip.body = description;
        }

        if (!tips.contains(descriptionTip))
        {
            this.tips.add(descriptionTip);
        }
    }
}
