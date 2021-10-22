package eatyourbeets.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.*;
import eatyourbeets.dailymods.NoRelics;
import eatyourbeets.relics.animator.AbstractMissingPiece;
import eatyourbeets.relics.animator.ColorlessFragment;
import eatyourbeets.relics.animator.RacePiece;
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

            data.AddRelic(MarkOfPain.ID, AbstractRelic.RelicTier.BOSS);
            data.AddRelic(VioletLotus.ID, AbstractRelic.RelicTier.BOSS);
            data.AddRelic(HoveringKite.ID, AbstractRelic.RelicTier.BOSS);
            data.AddRelic(NuclearBattery.ID, AbstractRelic.RelicTier.BOSS);

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
            data.AddRelic(RunicCube.ID, AbstractRelic.RelicTier.UNCOMMON);
            data.AddRelic(WristBlade.ID, AbstractRelic.RelicTier.COMMON);


            data.AddRelic(BurningBlood.ID, AbstractRelic.RelicTier.SHOP);
            data.AddRelic(RingOfTheSerpent.ID, AbstractRelic.RelicTier.SHOP);
            data.AddRelic(CrackedCore.ID, AbstractRelic.RelicTier.SHOP);
            data.AddRelic(PureWater.ID, AbstractRelic.RelicTier.SHOP);
            data.AddRelic(SneckoSkull.ID, AbstractRelic.RelicTier.COMMON);
            data.AddRelic(Damaru.ID, AbstractRelic.RelicTier.COMMON);
            data.AddRelic(SelfFormingClay.ID, AbstractRelic.RelicTier.UNCOMMON);
            data.AddRelic(NinjaScroll.ID, AbstractRelic.RelicTier.UNCOMMON);
            data.AddRelic(GoldPlatedCables.ID, AbstractRelic.RelicTier.UNCOMMON);
            data.AddRelic(SymbioticVirus.ID, AbstractRelic.RelicTier.UNCOMMON);
            data.AddRelic(TeardropLocket.ID, AbstractRelic.RelicTier.UNCOMMON);
            data.AddRelic(Duality.ID, AbstractRelic.RelicTier.UNCOMMON);
            data.AddRelic(MagicFlower.ID, AbstractRelic.RelicTier.RARE);
            data.AddRelic(TheSpecimen.ID, AbstractRelic.RelicTier.RARE);
            data.AddRelic(Inserter.ID, AbstractRelic.RelicTier.RARE);

            data.AddRelic(Tingsha.ID, AbstractRelic.RelicTier.RARE);
            data.AddRelic(ToughBandages.ID, AbstractRelic.RelicTier.RARE);
            data.AddRelic(EmotionChip.ID, AbstractRelic.RelicTier.RARE);
            data.AddRelic(GoldenEye.ID, AbstractRelic.RelicTier.RARE);

            data.AddRelic(Melange.ID, AbstractRelic.RelicTier.SHOP);

            data.RemoveRelic(Astrolabe.ID);
            data.RemoveRelic(PeacePipe.ID);
            data.RemoveRelic(PandorasBox.ID);
            data.RemoveRelic(QuestionCard.ID);
            data.RemoveRelic(DreamCatcher.ID);
            data.RemoveRelic(RacePiece.ID);
            data.RemoveRelic(SingingBowl.ID);
            data.RemoveRelic(EmptyCage.ID);
            data.RemoveRelic(Orrery.ID);
            data.RemoveRelic(DollysMirror.ID);
            data.RemoveRelic(PrayerWheel.ID);
            data.RemoveRelic(TinyHouse.ID);
            data.RemoveRelic(Waffle.ID);
            data.RemoveRelic(ColorlessFragment.ID);

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
