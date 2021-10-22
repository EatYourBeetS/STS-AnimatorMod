package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.relics.animator.AncientTome;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class AncientTomeEnchantment extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(AncientTomeEnchantment.class);
    public static final int LEVEL = 2;
    public static final int INDEX = 5;

    public AncientTomeEnchantment()
    {
        super(DATA, LEVEL, INDEX, new AncientTome());

        Initialize(0, 0);
    }

    @Override
    public void OnObtain()
    {
        GameEffects.TopLevelList.ObtainRelic(new AncientTome());
    }

    @Override
    public void OnStartOfBattle()
    {
        final Random rng = new Random(Settings.seed + (AbstractDungeon.actNum * 17) + (AbstractDungeon.floorNum * 23));

        if (AbstractDungeon.actNum == 3 && !GameUtilities.InBossRoom() && (GameUtilities.InEliteRoom() || rng.randomBoolean(0.2f))) {
            GameUtilities.GetCurrentRoom(true).addRelicToRewards(new AncientTome());
        }
    }
}