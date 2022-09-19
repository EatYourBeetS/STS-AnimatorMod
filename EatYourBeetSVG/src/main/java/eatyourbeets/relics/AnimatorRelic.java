package eatyourbeets.relics;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.relics.*;
import eatyourbeets.relics.animator.AbstractMissingPiece;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorDungeonData;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public abstract class AnimatorRelic extends EYBRelic
{
    protected static final FieldInfo<Float> _offsetX = JUtils.GetField("offsetX", AbstractRelic.class);

    public static String CreateFullID(Class<? extends AnimatorRelic> type)
    {
        return GR.Animator.CreateID(type.getSimpleName());
    }

    public static void UpdateRelics(boolean isAnimator, boolean isAnimatorClassic)
    {
        final AnimatorDungeonData data = GR.Animator.Dungeon;
        if (isAnimator)
        {
            data.RemoveRelic(PenNib.ID);
            data.RemoveRelic(Kunai.ID);
            data.RemoveRelic(StrikeDummy.ID);
            data.RemoveRelic(SneckoEye.ID);
            data.RemoveRelic(Cauldron.ID);
            data.RemoveRelic(ChemicalX.ID);
            data.RemoveRelic(TinyHouse.ID);
            data.RemoveRelic(SacredBark.ID);
            data.RemoveRelic(VelvetChoker.ID);
            data.RemoveRelic(RunicPyramid.ID);
            data.RemoveRelic(CeramicFish.ID);
            data.RemoveRelic(IncenseBurner.ID);

            if (GameUtilities.GetAscensionLevel() >= 14)
            {
                data.RemoveRelic(PrismaticShard.ID);
            }

            data.AddRelic(MarkOfPain.ID, AbstractRelic.RelicTier.BOSS);
            data.AddRelic(RunicCapacitor.ID, AbstractRelic.RelicTier.SHOP);
            data.AddRelic(TwistedFunnel.ID, AbstractRelic.RelicTier.SHOP);
            data.AddRelic(Brimstone.ID, AbstractRelic.RelicTier.SHOP);
            data.AddRelic(DataDisk.ID, AbstractRelic.RelicTier.SHOP);
            data.AddRelic(SacredBark.ID, AbstractRelic.RelicTier.SHOP);
            data.AddRelic(CloakClasp.ID, AbstractRelic.RelicTier.RARE);
            data.AddRelic(CharonsAshes.ID, AbstractRelic.RelicTier.RARE);
            data.AddRelic(ChampionsBelt.ID, AbstractRelic.RelicTier.RARE);
            data.AddRelic(PaperCrane.ID, AbstractRelic.RelicTier.UNCOMMON);
            data.AddRelic(PaperFrog.ID, AbstractRelic.RelicTier.UNCOMMON);

            AbstractMissingPiece.RefreshDescription();
        }

        final ArrayList<String> toRemove = new ArrayList<>();
        for (ArrayList<String> relics : GameUtilities.GetRelicPools())
        {
            for (String relicID : relics)
            {
                if (relicID.startsWith(GR.Animator.Prefix))
                {
                    if (relicID.startsWith(GR.AnimatorClassic.Prefix))
                    {
                        if (!isAnimatorClassic)
                        {
                            toRemove.add(relicID);
                        }
                    }
                    else if (!isAnimator)
                    {
                        toRemove.add(relicID);
                    }
                }
            }
        }

        for (String id : toRemove)
        {
            data.RemoveRelic(id);
        }
    }

    public AnimatorRelic(String id, RelicTier tier, LandingSound sfx)
    {
        super(id, id, tier, sfx);
    }

    @Override
    public boolean canSpawn()
    {
        return GameUtilities.IsPlayerClass(GetPlayerClass());
    }

    @Override
    public AbstractPlayer.PlayerClass GetPlayerClass()
    {
        return GR.Animator.PlayerClass;
    }
}
