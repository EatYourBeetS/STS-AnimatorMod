package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.relics.animator.RacePiece;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class RacePieceEnchantment extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(RacePieceEnchantment.class);
    public static final int LEVEL = 2;
    public static final int INDEX = 2;

    public RacePieceEnchantment()
    {
        super(DATA, LEVEL, INDEX, new RacePiece());

        Initialize(0, 0);
    }

    @Override
    public void OnObtain()
    {
        GameEffects.TopLevelList.ObtainRelic(new RacePiece());
    }

    @Override
    public void OnStartOfBattle()
    {
        if (AbstractDungeon.actNum == 3 && GameUtilities.InEliteRoom()) {
            GameUtilities.GetCurrentRoom(true).addRelicToRewards(new RacePiece());
        }
    }
}