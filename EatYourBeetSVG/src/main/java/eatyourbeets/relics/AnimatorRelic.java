package eatyourbeets.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.*;
import eatyourbeets.dailymods.NoRelics;
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

    public static void UpdateRelics(boolean isAnimator)
    {
        if (isAnimator)
        {
            final AnimatorDungeonData data = GR.Animator.Dungeon;

            data.RemoveRelic(PenNib.ID);
            data.RemoveRelic(Kunai.ID);
            data.RemoveRelic(StrikeDummy.ID);
            data.RemoveRelic(SneckoEye.ID);
            data.RemoveRelic(SacredBark.ID);
            data.RemoveRelic(RunicPyramid.ID);
            data.RemoveRelic(CeramicFish.ID);

            data.AddRelic(MarkOfPain.ID, AbstractRelic.RelicTier.BOSS);

            if (ModHelper.isModEnabled(NoRelics.ID))
            {
                ArrayList<String> relics = new ArrayList<>();
                relics.addAll(GameUtilities.GetRelicPool(RelicTier.COMMON));
                relics.addAll(GameUtilities.GetRelicPool(RelicTier.UNCOMMON));
                relics.addAll(GameUtilities.GetRelicPool(RelicTier.RARE));
                relics.addAll(GameUtilities.GetRelicPool(RelicTier.SHOP));

                for (String relic : relics)
                {
                    data.RemoveRelic(relic);
                }

                data.AddRelic(Circlet.ID, AbstractRelic.RelicTier.COMMON);
                data.AddRelic(Circlet.ID, AbstractRelic.RelicTier.UNCOMMON);
                data.AddRelic(Circlet.ID, AbstractRelic.RelicTier.RARE);
                data.AddRelic(Circlet.ID, AbstractRelic.RelicTier.SHOP);

                return;
            }

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
