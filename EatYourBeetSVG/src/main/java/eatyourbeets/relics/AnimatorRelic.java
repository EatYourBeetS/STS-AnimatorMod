package eatyourbeets.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.resources.AnimatorResources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patches.AbstractEnums;

import java.lang.reflect.InvocationTargetException;

public abstract class AnimatorRelic extends CustomRelic
{
    protected static final Logger logger = LogManager.getLogger(AnimatorRelic.class.getName());

    public static String CreateFullID(String id)
    {
        return AnimatorResources.CreateID(id);
    }

    public AnimatorRelic(String id, String imageID, RelicTier tier, LandingSound sfx)
    {
        super(id, new Texture(AnimatorResources.GetRelicImage(imageID)), tier, sfx);
    }

    public AnimatorRelic(String id, RelicTier tier, LandingSound sfx)
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
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean canSpawn()
    {
        return AbstractDungeon.player.chosenClass == AbstractEnums.Characters.THE_ANIMATOR;
    }
}
