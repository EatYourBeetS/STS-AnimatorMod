package eatyourbeets.cards.animator.enchantments;

import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.relics.animator.TheMissingPiece;
import eatyourbeets.utilities.GameEffects;

public class MissingPieceEnchantment extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(MissingPieceEnchantment.class);
    public static final int LEVEL = 1;
    public static final int INDEX = 2;

    public MissingPieceEnchantment()
    {
        super(DATA, LEVEL, INDEX, new TheMissingPiece());

        Initialize(0, 0);
    }

    @Override
    public void OnObtain()
    {
        GameEffects.TopLevelList.ObtainRelic(new TheMissingPiece());
    }

    @Override
    public void OnStartOfBattle()
    {

    }
}