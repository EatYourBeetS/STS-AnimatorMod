package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.relics.PandorasBox;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameEffects;

public class PandorasBoxEnchantment extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(PandorasBoxEnchantment.class);
    public static final int LEVEL = 1;
    public static final int INDEX = 7;

    public PandorasBoxEnchantment()
    {
        super(DATA, LEVEL, INDEX, new PandorasBox());

        Initialize(0, 0);
    }

    @Override
    public void OnObtain()
    {
        GameEffects.TopLevelList.ObtainRelic(new PandorasBox());
    }

    @Override
    public void OnStartOfBattle()
    {

    }
}