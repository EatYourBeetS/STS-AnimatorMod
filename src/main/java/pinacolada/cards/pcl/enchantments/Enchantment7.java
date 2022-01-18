package pinacolada.cards.pcl.enchantments;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.commons.lang3.ArrayUtils;
import pinacolada.cards.base.CardEffectChoice;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Enchantment7 extends Enchantment
{
    public static final PCLCardData DATA = RegisterInternal(Enchantment7.class);
    public static final int INDEX = 1;
    public PCLAffinity currentAffinity;

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Enchantment7()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 1, 9);
    }

    @Override
    public int GetMaxUpgradeIndex()
    {
        return 7;
    }

    @Override
    protected void OnUpgrade()
    {
        if (auxiliaryData.form < 7)
        {
            upgradeSecondaryValue(2);
        }
        else {
            upgradeSecondaryValue(3);
        }
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        currentAffinity = GetAffinity();
        if (currentAffinity != null) {
            PCLActions.Bottom.ObtainAffinityToken(currentAffinity, false);
        }
        else if (auxiliaryData.form == 7) {
            PCLActions.Bottom.ObtainAffinityToken(PCLGameUtilities.GetRandomElement(PCLAffinity.Extended(), PCLCard.rng), true);
        }
        else {
            PCLActions.Bottom.ObtainAffinityToken(PCLGameUtilities.GetRandomElement(PCLAffinity.Basic(), PCLCard.rng), false);
        }
    }

    @Override
    public PCLAffinity[] GetAffinityList() {
        switch(auxiliaryData.form) {
            case 1:
                return ArrayUtils.removeElement(PCLAffinity.Extended(), PCLAffinity.Red);
            case 2:
                return ArrayUtils.removeElement(PCLAffinity.Extended(), PCLAffinity.Green);
            case 3:
                return ArrayUtils.removeElement(PCLAffinity.Extended(), PCLAffinity.Blue);
            case 4:
                return ArrayUtils.removeElement(PCLAffinity.Extended(), PCLAffinity.Orange);
            case 5:
                return ArrayUtils.removeElement(PCLAffinity.Extended(), PCLAffinity.Light);
            case 6:
                return ArrayUtils.removeElement(PCLAffinity.Extended(), PCLAffinity.Dark);
        }
        return PCLAffinity.Extended();
    }

    public PCLAffinity GetAffinity()
    {
        switch (auxiliaryData.form)
        {
            case 1: return PCLAffinity.Red;
            case 2: return PCLAffinity.Green;
            case 3: return PCLAffinity.Blue;
            case 4: return PCLAffinity.Orange;
            case 5: return PCLAffinity.Light;
            case 6: return PCLAffinity.Dark;
            default: return null;
        }
    }
}