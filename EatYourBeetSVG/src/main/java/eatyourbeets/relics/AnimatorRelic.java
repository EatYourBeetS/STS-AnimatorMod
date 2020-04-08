package eatyourbeets.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.*;
import eatyourbeets.relics.animator.AbstractMissingPiece;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorDungeonData;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;

public abstract class AnimatorRelic extends EYBRelic
{
    protected static final FieldInfo<Float> _offsetX = JavaUtilities.GetField("offsetX", AbstractRelic.class);

    public static String CreateFullID(Class<? extends AnimatorRelic> type)
    {
        return GR.Animator.CreateID(type.getSimpleName());
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
            data.RemoveRelic(CeramicFish.ID);

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

    public AnimatorRelic(String id, RelicTier tier, LandingSound sfx)
    {
        super(id, id, tier, sfx);
    }

    @Override
    public boolean canSpawn()
    {
        return AbstractDungeon.player.chosenClass == GR.Animator.PlayerClass;
    }
}
