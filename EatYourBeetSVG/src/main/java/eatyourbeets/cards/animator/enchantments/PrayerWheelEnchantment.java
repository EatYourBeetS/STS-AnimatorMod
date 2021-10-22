package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.relics.PrayerWheel;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameEffects;

public class PrayerWheelEnchantment extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(PrayerWheelEnchantment.class);
    public static final int LEVEL = 2;
    public static final int INDEX = 1;

    public PrayerWheelEnchantment()
    {
        super(DATA, LEVEL, INDEX, new PrayerWheel());

        Initialize(0, 0);
    }

    @Override
    public void OnObtain() {
        GameEffects.TopLevelList.ObtainRelic(new PrayerWheel());
    }

    @Override
    public void OnStartOfBattle()
    {

    }
}