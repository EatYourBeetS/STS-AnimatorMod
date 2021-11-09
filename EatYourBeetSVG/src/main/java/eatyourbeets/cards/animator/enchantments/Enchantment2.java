package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Enchantment2 extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(Enchantment2.class);
    public static final int INDEX = 2;
    public Affinity currentAffinity;

    public Enchantment2()
    {
        super(DATA, INDEX);

        Initialize(0, 0, 2, 1);
    }

    @Override
    public int GetMaxUpgradeIndex()
    {
        return 7;
    }

    @Override
    public void UsePower(AbstractMonster m)
    {
        currentAffinity = GetAffinity();
        if (currentAffinity == null) {
            currentAffinity = GameUtilities.GetRandomElement(Affinity.Extended());
        }
        GameActions.Bottom.AddAffinity(currentAffinity, magicNumber);

        if (auxiliaryData.form == 7) {
            GameActions.Bottom.AddAffinity(GameUtilities.GetRandomElement(Affinity.Extended()), magicNumber);
        }
    }

    public Affinity GetAffinity()
    {
        switch (auxiliaryData.form)
        {
            case 1: return Affinity.Red;
            case 2: return Affinity.Green;
            case 3: return Affinity.Blue;
            case 4: return Affinity.Orange;
            case 5: return Affinity.Light;
            case 6: return Affinity.Dark;
            default: return null;
        }
    }
}