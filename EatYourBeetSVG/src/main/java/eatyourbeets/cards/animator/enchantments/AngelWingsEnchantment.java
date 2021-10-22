package eatyourbeets.cards.animator.enchantments;

import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.relics.animator.AngelWings;
import eatyourbeets.utilities.GameEffects;

public class AngelWingsEnchantment extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(AngelWingsEnchantment.class);
    public static final int LEVEL = 2;
    public static final int INDEX = 4;

    public AngelWingsEnchantment()
    {
        super(DATA, LEVEL, INDEX, new AngelWings());

        Initialize(0, 0);
    }

    @Override
    public void OnObtain()
    {
        GameEffects.TopLevelList.ObtainRelic(new AngelWings());
    }

    @Override
    public void OnStartOfBattle()
    {

    }
}