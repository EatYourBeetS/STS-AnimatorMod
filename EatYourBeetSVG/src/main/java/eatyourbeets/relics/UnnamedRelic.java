package eatyourbeets.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.utilities.JUtils;

import java.lang.reflect.InvocationTargetException;

public abstract class UnnamedRelic extends CustomRelic
{
    public static String CreateFullID(Class<? extends UnnamedRelic> type)
    {
        return GR.Unnamed.CreateID(type.getSimpleName());
    }

    public UnnamedRelic(String id, String imageID, RelicTier tier, LandingSound sfx)
    {
        super(id, new Texture(AnimatorResources.GetRelicImage(imageID)), tier, sfx);
    }

    public UnnamedRelic(String id, RelicTier tier, LandingSound sfx)
    {
        this(id, id, tier, sfx);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy()
    {
        try
        {
            return getClass().getConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            JUtils.LogError(this, e.getMessage());
            return null;
        }
    }

    @Override
    public boolean canSpawn()
    {
        return AbstractDungeon.player.chosenClass == GR.Enums.Characters.THE_UNNAMED;
    }
}
