package eatyourbeets.relics;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.*;
import eatyourbeets.relics.animator.AbstractMissingPiece;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorDungeonData;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public abstract class UnnamedRelic extends EYBRelic
{
    protected static final FieldInfo<Float> _offsetX = JUtils.GetField("offsetX", AbstractRelic.class);

    public static String CreateFullID(Class<? extends UnnamedRelic> type)
    {
        return GR.Unnamed.CreateID(type.getSimpleName());
    }

    public static void UpdateRelics(boolean isAnimator)
    {
        if (isAnimator)
        {
            final AnimatorDungeonData data = GR.Animator.Dungeon;

            //.RemoveRelic(PenNib.ID);
            //data.RemoveRelic(Kunai.ID);
            //data.RemoveRelic(StrikeDummy.ID);
            data.RemoveRelic(SneckoEye.ID);
            data.RemoveRelic(Cauldron.ID);
            data.RemoveRelic(ChemicalX.ID);
            data.RemoveRelic(TinyHouse.ID);
            data.RemoveRelic(SacredBark.ID);
            //data.RemoveRelic(VelvetChoker.ID);
            data.RemoveRelic(RunicPyramid.ID);
            data.RemoveRelic(CeramicFish.ID);
            //data.RemoveRelic(IncenseBurner.ID);

            if (GameUtilities.GetAscensionLevel() >= 14)
            {
                data.RemoveRelic(PrismaticShard.ID);
            }

            data.AddRelic(MarkOfPain.ID, RelicTier.BOSS);
            //data.AddRelic(RunicCapacitor.ID, RelicTier.SHOP);
            data.AddRelic(TwistedFunnel.ID, RelicTier.SHOP);
            data.AddRelic(Brimstone.ID, RelicTier.SHOP);
            //data.AddRelic(DataDisk.ID, RelicTier.SHOP);
            data.AddRelic(SacredBark.ID, RelicTier.SHOP);
            data.AddRelic(CloakClasp.ID, RelicTier.RARE);
            //data.AddRelic(CharonsAshes.ID, RelicTier.RARE);
            //data.AddRelic(ChampionsBelt.ID, RelicTier.RARE);
            //data.AddRelic(PaperCrane.ID, RelicTier.UNCOMMON);
            //data.AddRelic(PaperFrog.ID, RelicTier.UNCOMMON);

            AbstractMissingPiece.RefreshDescription();
        }
    }

    public UnnamedRelic(String id, RelicTier tier, LandingSound sfx)
    {
        super(id, id, tier, sfx);
    }

    @Override
    public boolean canSpawn()
    {
        return AbstractDungeon.player.chosenClass == GetPlayerClass();
    }

    @Override
    public AbstractPlayer.PlayerClass GetPlayerClass()
    {
        return GR.Unnamed.PlayerClass;
    }
}
