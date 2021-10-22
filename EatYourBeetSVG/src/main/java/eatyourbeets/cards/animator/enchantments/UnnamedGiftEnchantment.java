package eatyourbeets.cards.animator.enchantments;

import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.relics.animator.UnnamedGift;
import eatyourbeets.utilities.GameEffects;

public class UnnamedGiftEnchantment extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(UnnamedGiftEnchantment.class);
    public static final int LEVEL = 2;
    public static final int INDEX = 10;

    public UnnamedGiftEnchantment()
    {
        super(DATA, LEVEL, INDEX, new UnnamedGift());

        Initialize(0, 0);
    }

    @Override
    public void OnObtain()
    {
        GameEffects.TopLevelList.ObtainRelic(new UnnamedGift());
    }

    @Override
    public void OnStartOfBattle()
    {

    }
}