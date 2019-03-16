package eatyourbeets.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.AnimatorResources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patches.AbstractEnums;

public abstract class AnimatorRelic extends CustomRelic
{
    protected static final Logger logger = LogManager.getLogger(AnimatorRelic.class.getName());

    public static String CreateFullID(String id)
    {
        return "Animator_" + id;
    }

    public AnimatorRelic(String id, RelicTier tier, LandingSound sfx)
    {
        super(id, new Texture(AnimatorResources.GetRelicImage(id)), tier, sfx);
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
            return getClass().newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
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
