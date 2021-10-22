package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.relics.animator.TinyHouse;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class TinyHouseEnchantment extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(TinyHouseEnchantment.class);
    public static final int LEVEL = 1;
    public static final int INDEX = 5;

    public TinyHouseEnchantment()
    {
        super(DATA, LEVEL, INDEX, new TinyHouse());

        Initialize(0, 0);
    }

    @Override
    public void OnObtain()
    {
        GameEffects.TopLevelQueue.ObtainRelic(new TinyHouse());
    }

    @Override
    public void OnStartOfBattle()
    {
        if (AbstractDungeon.actNum == 2 && GameUtilities.InEliteRoom()) {
            GameUtilities.GetCurrentRoom(true).addRelicToRewards(new TinyHouse());
        }
    }
}