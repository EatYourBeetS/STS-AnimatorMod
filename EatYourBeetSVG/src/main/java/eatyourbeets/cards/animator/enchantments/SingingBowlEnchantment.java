package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.relics.SingingBowl;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameEffects;

public class SingingBowlEnchantment extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(SingingBowlEnchantment.class);
    public static final int LEVEL = 1;
    public static final int INDEX = 9;

    public SingingBowlEnchantment()
    {
        super(DATA, LEVEL, INDEX, new SingingBowl());

        Initialize(0, 0);
    }

    @Override
    public void OnObtain()
    {
        GameEffects.TopLevelList.ObtainRelic(new SingingBowl());
    }

    @Override
    public void OnStartOfBattle()
    {

    }
}