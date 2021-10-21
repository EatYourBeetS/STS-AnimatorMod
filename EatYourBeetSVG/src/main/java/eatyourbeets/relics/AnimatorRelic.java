package eatyourbeets.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.*;
import eatyourbeets.dailymods.AllRelicAnimatorRun;
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

            if (!ModHelper.isModEnabled(AllRelicAnimatorRun.ID)) {
                data.RemoveRelic(PenNib.ID);
                data.RemoveRelic(Kunai.ID);
                data.RemoveRelic(StrikeDummy.ID);
                data.RemoveRelic(SneckoEye.ID);
                data.RemoveRelic(SacredBark.ID);
                data.RemoveRelic(RunicPyramid.ID);
                data.RemoveRelic(CeramicFish.ID);
            }

            data.AddRelic(MarkOfPain.ID, AbstractRelic.RelicTier.BOSS);

            if (ModHelper.isModEnabled(AllRelicAnimatorRun.ID)) {
                data.AddRelic(BlackBlood.ID, AbstractRelic.RelicTier.BOSS);
                data.AddRelic(RunicCube.ID, AbstractRelic.RelicTier.BOSS);
                data.AddRelic(WristBlade.ID, AbstractRelic.RelicTier.BOSS);
                data.AddRelic(HoveringKite.ID, AbstractRelic.RelicTier.BOSS);
                data.AddRelic(FrozenCore.ID, AbstractRelic.RelicTier.BOSS);
                data.AddRelic(Inserter.ID, AbstractRelic.RelicTier.BOSS);
                data.AddRelic(NuclearBattery.ID, AbstractRelic.RelicTier.BOSS);
                data.AddRelic(HolyWater.ID, AbstractRelic.RelicTier.BOSS);
            }

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
            data.AddRelic(TinyHouse.ID, AbstractRelic.RelicTier.COMMON);

            if (ModHelper.isModEnabled(AllRelicAnimatorRun.ID)) {
                data.AddRelic(BurningBlood.ID, AbstractRelic.RelicTier.SHOP);
                data.AddRelic(RingOfTheSerpent.ID, AbstractRelic.RelicTier.SHOP);
                data.AddRelic(CrackedCore.ID, AbstractRelic.RelicTier.SHOP);
                data.AddRelic(PureWater.ID, AbstractRelic.RelicTier.SHOP);
                data.AddRelic(SneckoSkull.ID, AbstractRelic.RelicTier.COMMON);
                data.AddRelic(SelfFormingClay.ID, AbstractRelic.RelicTier.UNCOMMON);
                data.AddRelic(NinjaScroll.ID, AbstractRelic.RelicTier.UNCOMMON);
                data.AddRelic(GoldPlatedCables.ID, AbstractRelic.RelicTier.UNCOMMON);
                data.AddRelic(SymbioticVirus.ID, AbstractRelic.RelicTier.UNCOMMON);
                data.AddRelic(Duality.ID, AbstractRelic.RelicTier.UNCOMMON);
                data.AddRelic(MagicFlower.ID, AbstractRelic.RelicTier.RARE);
                data.AddRelic(TheSpecimen.ID, AbstractRelic.RelicTier.RARE);
                data.AddRelic(Tingsha.ID, AbstractRelic.RelicTier.RARE);
                data.AddRelic(ToughBandages.ID, AbstractRelic.RelicTier.RARE);
                data.AddRelic(EmotionChip.ID, AbstractRelic.RelicTier.RARE);
                data.AddRelic(GoldenEye.ID, AbstractRelic.RelicTier.RARE);
                data.AddRelic(Melange.ID, AbstractRelic.RelicTier.SHOP);
            }

            AbstractMissingPiece.RefreshDescription();
        }
    }

    public AnimatorRelic(String id, RelicTier tier, LandingSound sfx)
    {
        super(id, id, tier, sfx);
    }

    public AnimatorRelic(String id, String imageID, RelicTier tier, LandingSound sfx)
    {
        super(id, imageID, tier, sfx);
    }

    @Override
    public boolean canSpawn()
    {
        return AbstractDungeon.player.chosenClass == GR.Animator.PlayerClass;
    }
}
