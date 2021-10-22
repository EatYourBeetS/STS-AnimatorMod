package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.Waffle;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class LeesWaffleEnchantment extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(LeesWaffleEnchantment.class);
    public static final int LEVEL = 1;
    public static final int INDEX = 10;

    public LeesWaffleEnchantment()
    {
        super(DATA, LEVEL, INDEX, new Waffle());

        Initialize(0, 0);
    }

    @Override
    public void OnObtain()
    {
        GameEffects.TopLevelList.ObtainRelic(new Waffle());
    }

    @Override
    public void OnStartOfBattle()
    {
        if (AbstractDungeon.actNum == 2 && GameUtilities.InEliteRoom()) {
            GameUtilities.GetCurrentRoom(true).addRelicToRewards(new Waffle());
        }
    }
}