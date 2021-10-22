package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
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
        final Random rng = new Random(Settings.seed + (AbstractDungeon.actNum * 17) + (AbstractDungeon.floorNum * 23));

        if (AbstractDungeon.actNum == 3 && !GameUtilities.InBossRoom() && (GameUtilities.InEliteRoom() || rng.randomBoolean(0.2f))) {
            GameUtilities.GetCurrentRoom(true).addRelicToRewards(new DollysMirror());
        }
    }
}