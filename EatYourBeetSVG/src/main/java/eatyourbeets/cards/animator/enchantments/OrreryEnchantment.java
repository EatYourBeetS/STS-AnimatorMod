package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.Orrery;
import eatyourbeets.cards.base.EYBCardData;
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

    }

    @Override
    public void OnStartOfBattle()
    {
        final Random rng = new Random(Settings.seed + (AbstractDungeon.actNum * 17) + (AbstractDungeon.floorNum * 23));

        if (AbstractDungeon.actNum == 3 && !GameUtilities.InBossRoom() && (GameUtilities.InEliteRoom() || rng.randomBoolean(0.2f))) {
            GameUtilities.GetCurrentRoom(true).addRelicToRewards(new Orrery());
        }
    }
}