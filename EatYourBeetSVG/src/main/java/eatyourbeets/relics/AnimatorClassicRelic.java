package eatyourbeets.relics;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;

public abstract class AnimatorClassicRelic extends EYBRelic
{
    protected static final FieldInfo<Float> _offsetX = JUtils.GetField("offsetX", AbstractRelic.class);

    public static String CreateFullID(Class<? extends AnimatorClassicRelic> type)
    {
        return GR.AnimatorClassic.CreateID(type.getSimpleName());
    }

    public AnimatorClassicRelic(String id, RelicTier tier, LandingSound sfx)
    {
        super(id, GR.AnimatorClassic.ConvertID(id, false), tier, sfx);
    }

    @Override
    public boolean canSpawn()
    {
        return AbstractDungeon.player.chosenClass == GetPlayerClass();
    }

    @Override
    public AbstractPlayer.PlayerClass GetPlayerClass()
    {
        return GR.AnimatorClassic.PlayerClass;
    }
}
