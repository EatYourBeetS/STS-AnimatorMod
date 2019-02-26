package eatyourbeets.misc;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rewards.RewardItem;
import eatyourbeets.Utilities;
import eatyourbeets.cards.animator.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BundledRelicProvider
{
    private static final Map<String, BundledRelic> bundledRelicsPool = new HashMap<>();
    private static final ArrayList<BundledRelic> bundledRelics = new ArrayList<>();

    private static MapRoomNode lastNode = null;

    public static BundledRelicContainer SetupBundledRelics(RewardItem rItem, ArrayList<AbstractCard> cards)
    {
        MapRoomNode mapNode = AbstractDungeon.currMapNode;
        if (lastNode != mapNode)
        {
            lastNode = mapNode;
            bundledRelics.clear();
            Utilities.Logger.info("Clearing Bundles");
        }

        BundledRelicContainer bundle = new BundledRelicContainer(rItem);
        for (AbstractCard c : cards)
        {
            BundledRelic bundledRelic = GetBundle(c.cardID);
            if (bundledRelic != null)
            {
                bundle.bundledRelics.add(bundledRelic);
            }
        }

        return bundle;
    }

    private static BundledRelic GetBundle(String cardID)
    {
        for (BundledRelic relic : bundledRelics)
        {
            if (relic.cardID.equals(cardID))
            {
                return relic;
            }
        }

        BundledRelic relic = bundledRelicsPool.get(cardID);
        if (relic != null)
        {
            relic = relic.Clone(AbstractDungeon.miscRng.random(99));
            bundledRelics.add(relic);
            bundledRelics.add(relic);
        }
        else
        {
            Utilities.Logger.error("Key not found: " + cardID);
        }

        return relic;
    }

    private static void AddBundle(String cardID, String relicID, AbstractRelic.RelicTier tier, int chance)
    {
        bundledRelicsPool.put(cardID, new BundledRelic(cardID, relicID, tier, chance));
    }

    static
    {
        final int R1 = 3;
        final int R2 = 4;
        final int R3 = 5;
        final int R4 = 6;
        final int R5 = 7;
        final int R6 = 9;

        // <Common>
        AddBundle(Azekura.ID, ChampionsBelt.ID, AbstractRelic.RelicTier.SPECIAL, R4);
        AddBundle(DwarfShaman.ID, HandDrill.ID, AbstractRelic.RelicTier.SHOP, R3);
        AddBundle(GuildGirl.ID, LetterOpener.ID, AbstractRelic.RelicTier.UNCOMMON, R3);
        AddBundle(Priestess.ID, IceCream.ID, AbstractRelic.RelicTier.RARE, R1);
        AddBundle(Aqua.ID, MagicFlower.ID, AbstractRelic.RelicTier.SPECIAL, R2);
        AddBundle(Ara.ID, Kunai.ID, AbstractRelic.RelicTier.UNCOMMON, R3);
        AddBundle(Archer.ID, ArtOfWar.ID, AbstractRelic.RelicTier.COMMON, R3);
        AddBundle(Berserker.ID, Girya.ID, AbstractRelic.RelicTier.RARE, R4);
        AddBundle(ChaikaBohdan.ID, MeatOnTheBone.ID, AbstractRelic.RelicTier.UNCOMMON, R2);
        AddBundle(Chung.ID, MercuryHourglass.ID, AbstractRelic.RelicTier.UNCOMMON, R4);
        AddBundle(Cocytus.ID, BronzeScales.ID, AbstractRelic.RelicTier.COMMON, R3);
        AddBundle(DolaCouronne.ID, ClockworkSouvenir.ID, AbstractRelic.RelicTier.SHOP, R3);
        AddBundle(Darkness.ID, RunicCube.ID, AbstractRelic.RelicTier.SPECIAL, R1);
        AddBundle(Demiurge.ID, RedSkull.ID, AbstractRelic.RelicTier.SPECIAL, R5);
        AddBundle(Elsword.ID, Whetstone.ID, AbstractRelic.RelicTier.COMMON, R3);
        AddBundle(Emonzaemon.ID, Shuriken.ID, AbstractRelic.RelicTier.UNCOMMON, R2);
        AddBundle(Gillette.ID, BagOfPreparation.ID, AbstractRelic.RelicTier.COMMON, R2);
        AddBundle(Guy.ID, FrozenEye.ID, AbstractRelic.RelicTier.SHOP, R5);
        AddBundle(Hitei.ID, OrnamentalFan.ID, AbstractRelic.RelicTier.UNCOMMON, R6);
        AddBundle(Jibril.ID, WhiteBeast.ID, AbstractRelic.RelicTier.BOSS, R1);
        AddBundle(Kazuma.ID, OldCoin.ID, AbstractRelic.RelicTier.RARE, R1);
        AddBundle(Konayuki.ID, Girya.ID, AbstractRelic.RelicTier.RARE, R4);
        AddBundle(ChlammyZell.ID, GamblingChip.ID, AbstractRelic.RelicTier.RARE, R3);
        AddBundle(Kuribayashi.ID, Ginger.ID, AbstractRelic.RelicTier.RARE, R3);
        AddBundle(Mikaela.ID, CharonsAshes.ID, AbstractRelic.RelicTier.SPECIAL, R4);
        AddBundle(Mitsuba.ID, Courier.ID, AbstractRelic.RelicTier.UNCOMMON, R3);
        AddBundle(Mitsurugi.ID, Lantern.ID, AbstractRelic.RelicTier.COMMON, R4);
        AddBundle(PandorasActor.ID, SelfFormingClay.ID, AbstractRelic.RelicTier.SPECIAL, R4);
        AddBundle(RinTohsaka.ID, GoldPlatedCables.ID, AbstractRelic.RelicTier.SPECIAL, R4);
        AddBundle(Shalltear.ID, Vajra.ID, AbstractRelic.RelicTier.COMMON, R2);
        AddBundle(AcuraTooru.ID, WristBlade.ID, AbstractRelic.RelicTier.SPECIAL, R1);
        AddBundle(TukaLunaMarceau.ID, Strawberry.ID, AbstractRelic.RelicTier.COMMON, R5);
        AddBundle(Tyuule.ID, SneckoSkull.ID, AbstractRelic.RelicTier.SPECIAL, R1);
        AddBundle(Yuuichirou.ID, Sling.ID, AbstractRelic.RelicTier.SHOP, R3);

        // <Uncommon>
        AddBundle(AcuraAkari.ID, TwistedFunnel.ID, AbstractRelic.RelicTier.SPECIAL, R3);
        AddBundle(HighElfArcher.ID, OddlySmoothStone.ID, AbstractRelic.RelicTier.COMMON, R3);
        AddBundle(LizardPriest.ID, LizardTail.ID, AbstractRelic.RelicTier.BOSS, R1);
        AddBundle(Aisha.ID, Cauldron.ID, AbstractRelic.RelicTier.SHOP, R4);
        AddBundle(Alexander.ID, ArtOfWar.ID, AbstractRelic.RelicTier.COMMON, R4);
        AddBundle(Caster.ID, SymbioticVirus.ID, AbstractRelic.RelicTier.SPECIAL, R4);
        AddBundle(Entoma.ID, TheSpecimen.ID, AbstractRelic.RelicTier.SPECIAL, R3);
        AddBundle(Fredrika.ID, Matryoshka.ID, AbstractRelic.RelicTier.UNCOMMON, R3);
        AddBundle(NarberalGamma.ID, CrackedCore.ID, AbstractRelic.RelicTier.SPECIAL, R2);
        AddBundle(Illya.ID, SelfFormingClay.ID, AbstractRelic.RelicTier.SPECIAL, R4);
        AddBundle(KrulTepes.ID, MagicFlower.ID, AbstractRelic.RelicTier.SPECIAL, R2);
        AddBundle(Lancer.ID, Nunchaku.ID, AbstractRelic.RelicTier.RARE, R2);
        AddBundle(Layla.ID, PotionBelt.ID, AbstractRelic.RelicTier.COMMON, R5);
        AddBundle(LeleiLaLalena.ID, Abacus.ID, AbstractRelic.RelicTier.SHOP, R4);
        AddBundle(Megumin.ID, StoneCalendar.ID, AbstractRelic.RelicTier.RARE, R2);
        AddBundle(Nanami.ID, PaperCrane.ID, AbstractRelic.RelicTier.SPECIAL, R3);
        AddBundle(PinaCoLada.ID, Mango.ID, AbstractRelic.RelicTier.RARE, R2);
        AddBundle(Rena.ID, BagOfMarbles.ID, AbstractRelic.RelicTier.COMMON, R2);
        AddBundle(DolaRiku.ID, UnceasingTop.ID, AbstractRelic.RelicTier.RARE, R1);
        AddBundle(RoryMercury.ID, PenNib.ID, AbstractRelic.RelicTier.COMMON, R2);
        AddBundle(DolaSchwi.ID, EmotionChip.ID, AbstractRelic.RelicTier.SPECIAL, R6);
        AddBundle(Shichika.ID, PaperFrog.ID, AbstractRelic.RelicTier.SPECIAL, R3);
        AddBundle(Shimakaze.ID, Anchor.ID, AbstractRelic.RelicTier.COMMON, R4);
        AddBundle(Shinoa.ID, Calipers.ID, AbstractRelic.RelicTier.RARE, R2);
        AddBundle(DolaStephanie.ID, HappyFlower.ID, AbstractRelic.RelicTier.COMMON, R3);
        AddBundle(Togame.ID, AncientTeaSet.ID, AbstractRelic.RelicTier.COMMON, R2);
        AddBundle(Viivi.ID, Kunai.ID, AbstractRelic.RelicTier.UNCOMMON, R2);
        AddBundle(YaoHaDucy.ID, DreamCatcher.ID, AbstractRelic.RelicTier.COMMON, R2);
        AddBundle(YunYun.ID, WarPaint.ID, AbstractRelic.RelicTier.COMMON, R3);

        // <Rare>
        AddBundle(SwordMaiden.ID, BlueCandle.ID, AbstractRelic.RelicTier.UNCOMMON, R2);
        AddBundle(Albedo.ID, DarkstonePeriapt.ID, AbstractRelic.RelicTier.UNCOMMON, R5);
        AddBundle(Ainz.ID, GremlinHorn.ID, AbstractRelic.RelicTier.UNCOMMON, R3);
        AddBundle(Biyorigo.ID, Inserter.ID, AbstractRelic.RelicTier.SPECIAL, R4);
        AddBundle(ChaikaTrabant.ID, MummifiedHand.ID, AbstractRelic.RelicTier.UNCOMMON, R1);
        AddBundle(Elesis.ID, MoltenEgg2.ID, AbstractRelic.RelicTier.UNCOMMON, R5);
        AddBundle(Eris.ID, TinyChest.ID, AbstractRelic.RelicTier.COMMON, R3);
        AddBundle(Eve.ID, DataDisk.ID, AbstractRelic.RelicTier.SPECIAL, R6);
        AddBundle(FeridBathory.ID, MealTicket.ID, AbstractRelic.RelicTier.SHOP, R5);
        AddBundle(Gilgamesh.ID, RegalPillow.ID, AbstractRelic.RelicTier.COMMON, R1);
        AddBundle(GoblinSlayer.ID, MarkOfPain.ID, AbstractRelic.RelicTier.SPECIAL, R2);
        AddBundle(Guren.ID, IncenseBurner.ID, AbstractRelic.RelicTier.RARE, R2);
        AddBundle(HigakiRinne.ID, SpiritPoop.ID, AbstractRelic.RelicTier.SPECIAL, R5);
        AddBundle(ItamiYouji.ID, Boot.ID, AbstractRelic.RelicTier.COMMON, R2);
        AddBundle(Saber.ID, Orichalcum.ID, AbstractRelic.RelicTier.COMMON, R2);
        AddBundle(AcuraShin.ID, NinjaScroll.ID, AbstractRelic.RelicTier.SPECIAL, R3);
        AddBundle(Shiro.ID, Dodecahedron.ID, AbstractRelic.RelicTier.UNCOMMON, R2);
        AddBundle(Sora.ID, QuestionCard.ID, AbstractRelic.RelicTier.UNCOMMON, R1);
        AddBundle(Wiz.ID, JuzuBracelet.ID, AbstractRelic.RelicTier.COMMON, R5);

        // Common
        AddBundle(ElricEdward.ID, Pocketwatch.ID, AbstractRelic.RelicTier.RARE, R2);
        AddBundle(ElricAlphonse.ID, ToyOrnithopter.ID, AbstractRelic.RelicTier.SHOP, R2);
        AddBundle(Sloth.ID, Sundial.ID, AbstractRelic.RelicTier.UNCOMMON, R2);

        // Uncommon
        AddBundle(Scar.ID, FrozenEgg2.ID, AbstractRelic.RelicTier.UNCOMMON, R2);
        AddBundle(RoyMustang.ID, MoltenEgg2.ID, AbstractRelic.RelicTier.UNCOMMON, R2);
        AddBundle(MaesHughes.ID, PeacePipe.ID, AbstractRelic.RelicTier.UNCOMMON, R1);
        AddBundle(Gluttony.ID, Pear.ID, AbstractRelic.RelicTier.UNCOMMON, R2);
        AddBundle(Urushihara.ID, WingBoots.ID, AbstractRelic.RelicTier.RARE, R1);

        // Rare
        AddBundle(Envy.ID, DollysMirror.ID, AbstractRelic.RelicTier.SHOP, R1);
        AddBundle(Greed.ID, MawBank.ID, AbstractRelic.RelicTier.SHOP, R3);
    }
}