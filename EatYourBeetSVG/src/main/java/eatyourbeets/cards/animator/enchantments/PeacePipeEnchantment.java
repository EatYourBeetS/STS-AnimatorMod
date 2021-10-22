package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.relics.PeacePipe;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameEffects;

public class PeacePipeEnchantment extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(PeacePipeEnchantment.class);
    public static final int LEVEL = 1;
    public static final int INDEX = 8;

    public PeacePipeEnchantment()
    {
        super(DATA, LEVEL, INDEX, new PeacePipe());

        Initialize(0, 0);
    }

    @Override
    public void OnObtain()
    {
        GameEffects.TopLevelList.ObtainRelic(new PeacePipe());
    }

    @Override
    public void OnStartOfBattle()
    {

    }
}