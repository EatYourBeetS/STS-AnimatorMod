package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.Astrolabe;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class AstrolabeEnchantment extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(AstrolabeEnchantment.class);
    public static final int LEVEL = 1;
    public static final int INDEX = 4;

    public AstrolabeEnchantment()
    {
        super(DATA, LEVEL, INDEX, new Astrolabe());

        Initialize(0, 0);
    }

    @Override
    public void OnObtain()
    {
        GameEffects.TopLevelList.ObtainRelic(new Astrolabe());
    }

    @Override
    public void OnStartOfBattle()
    {
        if (AbstractDungeon.actNum == 2 && GameUtilities.InEliteRoom()) {
            GameUtilities.GetCurrentRoom(true).addRelicToRewards(new Astrolabe());
        }
    }
}