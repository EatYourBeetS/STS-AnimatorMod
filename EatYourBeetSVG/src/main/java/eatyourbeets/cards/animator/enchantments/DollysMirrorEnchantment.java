package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.DollysMirror;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class DollysMirrorEnchantment extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(DollysMirrorEnchantment.class);
    public static final int LEVEL = 2;
    public static final int INDEX = 9;

    public DollysMirrorEnchantment()
    {
        super(DATA, LEVEL, INDEX, new DollysMirror());

        Initialize(0, 0);
    }

    @Override
    public void OnObtain()
    {
        GameEffects.TopLevelList.ObtainRelic(new DollysMirror());
    }

    @Override
    public void OnStartOfBattle()
    {
        if (AbstractDungeon.actNum == 3 && GameUtilities.InEliteRoom()) {
            GameUtilities.GetCurrentRoom(true).addRelicToRewards(new DollysMirror());
        }
    }
}