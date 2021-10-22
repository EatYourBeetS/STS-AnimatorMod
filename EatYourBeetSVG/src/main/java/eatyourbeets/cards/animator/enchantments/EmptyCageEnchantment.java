package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.EmptyCage;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class EmptyCageEnchantment extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(EmptyCageEnchantment.class);
    public static final int LEVEL = 2;
    public static final int INDEX = 7;

    public EmptyCageEnchantment()
    {
        super(DATA, LEVEL, INDEX, new EmptyCage());

        Initialize(0, 0);
    }

    @Override
    public void OnObtain()
    {
        GameEffects.TopLevelList.ObtainRelic(new EmptyCage());
    }

    @Override
    public void OnStartOfBattle()
    {
        if (AbstractDungeon.actNum == 3 && GameUtilities.InEliteRoom()) {
            GameUtilities.GetCurrentRoom(true).addRelicToRewards(new EmptyCage());
        }
    }
}