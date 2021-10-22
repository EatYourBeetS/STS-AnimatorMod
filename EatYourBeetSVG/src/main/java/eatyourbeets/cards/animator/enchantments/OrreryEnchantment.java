package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.Orrery;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class OrreryEnchantment extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(OrreryEnchantment.class);
    public static final int LEVEL = 2;
    public static final int INDEX = 8;

    public OrreryEnchantment()
    {
        super(DATA, LEVEL, INDEX, new Orrery());

        Initialize(0, 0);
    }

    @Override
    public void OnObtain()
    {
        GameEffects.TopLevelList.ObtainRelic(new Orrery());
    }

    @Override
    public void OnStartOfBattle()
    {
        if (AbstractDungeon.actNum == 3 && GameUtilities.InEliteRoom()) {
            GameUtilities.GetCurrentRoom(true).addRelicToRewards(new Orrery());
        }
    }
}