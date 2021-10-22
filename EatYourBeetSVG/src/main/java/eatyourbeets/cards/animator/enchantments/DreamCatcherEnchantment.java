package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.relics.DreamCatcher;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameEffects;

public class DreamCatcherEnchantment extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(DreamCatcherEnchantment.class);
    public static final int LEVEL = 1;
    public static final int INDEX = 1;

    public DreamCatcherEnchantment()
    {
        super(DATA, LEVEL, INDEX, new DreamCatcher());

        Initialize(0, 0);
    }

    @Override
    public void OnObtain()
    {
        GameEffects.TopLevelList.ObtainRelic(new DreamCatcher());
    }

    @Override
    public void OnStartOfBattle()
    {

    }
}