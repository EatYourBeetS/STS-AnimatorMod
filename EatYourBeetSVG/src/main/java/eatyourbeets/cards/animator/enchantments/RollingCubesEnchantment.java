package eatyourbeets.cards.animator.enchantments;

import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.relics.animator.RollingCubes;
import eatyourbeets.utilities.GameEffects;

public class RollingCubesEnchantment extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(RollingCubesEnchantment.class);
    public static final int LEVEL = 2;
    public static final int INDEX = 6;

    public RollingCubesEnchantment()
    {
        super(DATA, LEVEL, INDEX, new RollingCubes());

        Initialize(0, 0);
    }

    @Override
    public void OnObtain()
    {
        GameEffects.TopLevelList.ObtainRelic(new RollingCubes());
    }

    @Override
    public void OnStartOfBattle()
    {

    }
}