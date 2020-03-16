package eatyourbeets.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.*;
import eatyourbeets.relics.animator.AbstractMissingPiece;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorDungeonData;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;

import java.lang.reflect.InvocationTargetException;

public abstract class AnimatorRelic extends CustomRelic
{
    protected static final FieldInfo<Float> _offsetX = JavaUtilities.GetField("offsetX", AbstractRelic.class);

    public static String CreateFullID(String id)
    {
        return GR.Animator.CreateID(id);
    }

    public static void UpdateRelics(boolean isAnimator)
    {
        if (isAnimator)
        {
            final AnimatorDungeonData data = GR.Animator.Dungeon;

            data.RemoveRelic(PenNib.ID);
            data.RemoveRelic(Kunai.ID);
            data.RemoveRelic(StrikeDummy.ID);
            data.RemoveRelic(SneckoEye.ID);
            data.RemoveRelic(RunicPyramid.ID);

            data.AddRelic(MarkOfPain.ID, AbstractRelic.RelicTier.BOSS);
            data.AddRelic(RunicCapacitor.ID, AbstractRelic.RelicTier.SHOP);
            data.AddRelic(TwistedFunnel.ID, AbstractRelic.RelicTier.SHOP);
            data.AddRelic(Brimstone.ID, AbstractRelic.RelicTier.SHOP);
            data.AddRelic(DataDisk.ID, AbstractRelic.RelicTier.SHOP);
            data.AddRelic(CharonsAshes.ID, AbstractRelic.RelicTier.RARE);
            data.AddRelic(ChampionsBelt.ID, AbstractRelic.RelicTier.RARE);
            data.AddRelic(PaperCrane.ID, AbstractRelic.RelicTier.UNCOMMON);
            data.AddRelic(PaperFrog.ID, AbstractRelic.RelicTier.UNCOMMON);
            data.AddRelic(CloakClasp.ID, AbstractRelic.RelicTier.UNCOMMON);
            data.AddRelic(RedSkull.ID, AbstractRelic.RelicTier.COMMON);

            AbstractMissingPiece.RefreshDescription();
        }
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
            JavaUtilities.GetLogger(this).error(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean canSpawn()
    {
        return AbstractDungeon.player.chosenClass == GR.Enums.Characters.THE_ANIMATOR;
    }

    protected String FormatDescription(Object... args)
    {
        return JavaUtilities.Format(DESCRIPTIONS[0], args);
    }
}
