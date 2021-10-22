package eatyourbeets.cards.animator.enchantments;

import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.relics.animator.StarCompass;
import eatyourbeets.utilities.GameEffects;

public class StarCompassEnchantment extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(StarCompassEnchantment.class);
    public static final int LEVEL = 2;
    public static final int INDEX = 3;

    public StarCompassEnchantment()
    {
        super(DATA, LEVEL, INDEX, new StarCompass());

        Initialize(0, 0);
    }

    @Override
    public void OnObtain()
    {
        GameEffects.TopLevelList.ObtainRelic(new StarCompass());
    }

    @Override
    public void OnStartOfBattle()
    {

    }
}