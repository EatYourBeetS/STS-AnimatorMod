package pinacolada.potions;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class GrowthPotion extends AbstractPotion
{
    public static final String POTION_ID = GR.PCL.CreateID(GrowthPotion.class.getSimpleName());
    public static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public GrowthPotion()
    {
        super(NAME, POTION_ID, PotionRarity.RARE, PotionSize.S, PotionColor.NONE);
    }

    @Override
    public void initializeData()
    {
        this.potency = this.getPotency();
        this.description = PCLJUtils.Format(DESCRIPTIONS[0], this.potency);
        this.isThrown = false;
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target)
    {
        PCLActions.Bottom.GainMight(potency);
        PCLActions.Bottom.GainWisdom(potency);
        PCLActions.Bottom.GainVelocity(potency);
    }

    @Override
    public AbstractPotion makeCopy()
    {
        return new GrowthPotion();
    }

    @Override
    public int getPotency(int ascensionLevel)
    {
        return 3;
    }
}
