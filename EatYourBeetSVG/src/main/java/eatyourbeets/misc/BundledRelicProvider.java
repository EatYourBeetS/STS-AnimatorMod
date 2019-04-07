package eatyourbeets.misc;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rewards.RewardItem;
import eatyourbeets.Utilities;
import eatyourbeets.cards.animator.*;
import eatyourbeets.relics.EngravedStaff;
import eatyourbeets.relics.Rinne;
import eatyourbeets.relics.WornHelmet;

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

    final static int R1 = 2;
    final static int R2 = 3;
    final static int R3 = 4;
    final static int R4 = 5;
    final static int R5 = 6;
    final static int R6 = 8;

    static
    {
        AddElsword();
        AddFate();
        AddFullmetalAlchemist();
        AddGATE();
        AddGoblinSlayer();
        AddHitsugiNoChaika();
        AddKatanagatari();
        AddKonosuba();
        AddNoGameNoLife();
        AddOverlord();
        AddOwariNoSeraph();
        AddTenSura();
        AddSpecial();
    }

    private static void AddElsword()
    {
        AddBundle(Ara.ID, Nunchaku.ID, AbstractRelic.RelicTier.UNCOMMON, R1);
        AddBundle(Chung.ID, MercuryHourglass.ID, AbstractRelic.RelicTier.UNCOMMON, R4);
        AddBundle(Elsword.ID, Whetstone.ID, AbstractRelic.RelicTier.COMMON, R3);
        AddBundle(Raven.ID, BurningBlood.ID, AbstractRelic.RelicTier.SPECIAL, R1);

        AddBundle(Rena.ID, BagOfMarbles.ID, AbstractRelic.RelicTier.COMMON, R2);
        AddBundle(Aisha.ID, Omamori.ID, AbstractRelic.RelicTier.COMMON, R2);
        AddBundle(Add.ID, SmilingMask.ID, AbstractRelic.RelicTier.COMMON, R2);
        AddBundle(Ain.ID, OrangePellets.ID, AbstractRelic.RelicTier.SHOP, R2);

        AddBundle(Elesis.ID, MoltenEgg2.ID, AbstractRelic.RelicTier.UNCOMMON, R5);
        AddBundle(Eve.ID, DataDisk.ID, AbstractRelic.RelicTier.SPECIAL, R6);
    }

    private static void AddFate()
    {
        AddBundle(Archer.ID, ArtOfWar.ID, AbstractRelic.RelicTier.COMMON, R3);
        AddBundle(Berserker.ID, Girya.ID, AbstractRelic.RelicTier.RARE, R4);
        AddBundle(RinTohsaka.ID, GoldPlatedCables.ID, AbstractRelic.RelicTier.SPECIAL, R4);
        AddBundle(EmiyaShirou.ID, Shovel.ID, AbstractRelic.RelicTier.SPECIAL, R1);

        AddBundle(Alexander.ID, ArtOfWar.ID, AbstractRelic.RelicTier.COMMON, R3);
        AddBundle(Caster.ID, SymbioticVirus.ID, AbstractRelic.RelicTier.SPECIAL, R4);
        AddBundle(Illya.ID, SelfFormingClay.ID, AbstractRelic.RelicTier.SPECIAL, R4);
        AddBundle(Lancer.ID, ArtOfWar.ID, AbstractRelic.RelicTier.RARE, R3);

        AddBundle(Gilgamesh.ID, RegalPillow.ID, AbstractRelic.RelicTier.COMMON, R1);
        AddBundle(Saber.ID, Orichalcum.ID, AbstractRelic.RelicTier.COMMON, R2);
    }

    private static void AddFullmetalAlchemist()
    {
        AddBundle(ElricEdward.ID, Pocketwatch.ID, AbstractRelic.RelicTier.RARE, R2);
        AddBundle(ElricAlphonse.ID, ToyOrnithopter.ID, AbstractRelic.RelicTier.SHOP, R2);
        AddBundle(Sloth.ID, Sundial.ID, AbstractRelic.RelicTier.UNCOMMON, R2);
        AddBundle(MaesHughes.ID, PeacePipe.ID, AbstractRelic.RelicTier.UNCOMMON, R1);

        AddBundle(Pride.ID, ToxicEgg2.ID, AbstractRelic.RelicTier.UNCOMMON, R2);
        AddBundle(Scar.ID, FrozenEgg2.ID, AbstractRelic.RelicTier.UNCOMMON, R2);
        AddBundle(RoyMustang.ID, MoltenEgg2.ID, AbstractRelic.RelicTier.UNCOMMON, R2);
        AddBundle(Gluttony.ID, Pear.ID, AbstractRelic.RelicTier.UNCOMMON, R2);

        AddBundle(Envy.ID, PreservedInsect.ID, AbstractRelic.RelicTier.SHOP, R2);
        AddBundle(Greed.ID, MawBank.ID, AbstractRelic.RelicTier.SHOP, R3);
    }

    private static void AddGATE()
    {
        AddBundle(Kuribayashi.ID, Ginger.ID, AbstractRelic.RelicTier.RARE, R3);
        AddBundle(TukaLunaMarceau.ID, Strawberry.ID, AbstractRelic.RelicTier.COMMON, R5);
        AddBundle(Arpeggio.ID, OddlySmoothStone.ID, AbstractRelic.RelicTier.COMMON, R1);
        AddBundle(Bozes.ID, Waffle.ID, AbstractRelic.RelicTier.SHOP, R2);

        AddBundle(LeleiLaLalena.ID, Abacus.ID, AbstractRelic.RelicTier.SHOP, R4);
        AddBundle(Tyuule.ID, SneckoSkull.ID, AbstractRelic.RelicTier.SPECIAL, R1);
        AddBundle(RoryMercury.ID, PenNib.ID, AbstractRelic.RelicTier.COMMON, R2);
        AddBundle(YaoHaDucy.ID, DreamCatcher.ID, AbstractRelic.RelicTier.COMMON, R2);

        AddBundle(PinaCoLada.ID, Mango.ID, AbstractRelic.RelicTier.RARE, R2);
        AddBundle(ItamiYouji.ID, Boot.ID, AbstractRelic.RelicTier.COMMON, R2);
    }

    private static void AddGoblinSlayer()
    {
        AddBundle(DwarfShaman.ID, HandDrill.ID, AbstractRelic.RelicTier.SHOP, R3);
        AddBundle(GuildGirl.ID, LetterOpener.ID, AbstractRelic.RelicTier.UNCOMMON, R3);
        AddBundle(Priestess.ID, IceCream.ID, AbstractRelic.RelicTier.RARE, R1);
        AddBundle(Spearman.ID, Pantograph.ID, AbstractRelic.RelicTier.SHOP, R1);

        AddBundle(CowGirl.ID, Turnip.ID, AbstractRelic.RelicTier.RARE, R1);
        AddBundle(HighElfArcher.ID, OddlySmoothStone.ID, AbstractRelic.RelicTier.COMMON, R3);
        AddBundle(LizardPriest.ID, LizardTail.ID, AbstractRelic.RelicTier.BOSS, R1);
        AddBundle(Witch.ID, Omamori.ID, AbstractRelic.RelicTier.SHOP, R2);

        AddBundle(GoblinSlayer.ID, WornHelmet.ID, AbstractRelic.RelicTier.COMMON, R3);
        AddBundle(SwordMaiden.ID, BlueCandle.ID, AbstractRelic.RelicTier.UNCOMMON, R2);
    }

    private static void AddHitsugiNoChaika()
    {
        AddBundle(ChaikaBohdan.ID, MeatOnTheBone.ID, AbstractRelic.RelicTier.UNCOMMON, R2);
        AddBundle(Gillette.ID, BagOfPreparation.ID, AbstractRelic.RelicTier.COMMON, R2);
        AddBundle(Guy.ID, FrozenEye.ID, AbstractRelic.RelicTier.SHOP, R5);
        AddBundle(AcuraTooru.ID, WristBlade.ID, AbstractRelic.RelicTier.SPECIAL, R1);

        AddBundle(AcuraAkari.ID, TwistedFunnel.ID, AbstractRelic.RelicTier.SPECIAL, R3);
        AddBundle(Fredrika.ID, Matryoshka.ID, AbstractRelic.RelicTier.UNCOMMON, R3);
        AddBundle(Layla.ID, PotionBelt.ID, AbstractRelic.RelicTier.COMMON, R5);
        AddBundle(Viivi.ID, Kunai.ID, AbstractRelic.RelicTier.UNCOMMON, R2);

        AddBundle(ChaikaTrabant.ID, MummifiedHand.ID, AbstractRelic.RelicTier.UNCOMMON, R1);
        AddBundle(AcuraShin.ID, NinjaScroll.ID, AbstractRelic.RelicTier.SPECIAL, R3);
    }

    private static void AddKatanagatari()
    {
        AddBundle(Azekura.ID, ChampionsBelt.ID, AbstractRelic.RelicTier.SPECIAL, R4);
        AddBundle(Emonzaemon.ID, Shuriken.ID, AbstractRelic.RelicTier.UNCOMMON, R2);
        AddBundle(Konayuki.ID, Girya.ID, AbstractRelic.RelicTier.RARE, R4);
        AddBundle(ZankiKiguchi.ID, Tingsha.ID, AbstractRelic.RelicTier.SPECIAL, R2);

        AddBundle(Hitei.ID, OrnamentalFan.ID, AbstractRelic.RelicTier.UNCOMMON, R5);
        AddBundle(Nanami.ID, PaperCrane.ID, AbstractRelic.RelicTier.SPECIAL, R3);
        AddBundle(Shichika.ID, PaperFrog.ID, AbstractRelic.RelicTier.SPECIAL, R3);
        AddBundle(Togame.ID, AncientTeaSet.ID, AbstractRelic.RelicTier.COMMON, R2);

        AddBundle(Biyorigo.ID, Inserter.ID, AbstractRelic.RelicTier.SPECIAL, R4);
        AddBundle(HigakiRinne.ID, Rinne.ID, AbstractRelic.RelicTier.SPECIAL, R6);
    }

    private static void AddKonosuba()
    {
        AddBundle(Aqua.ID, MagicFlower.ID, AbstractRelic.RelicTier.SPECIAL, R2);
        AddBundle(Darkness.ID, RunicCube.ID, AbstractRelic.RelicTier.SPECIAL, R1);
        AddBundle(Kazuma.ID, OldCoin.ID, AbstractRelic.RelicTier.RARE, R1);
        AddBundle(Mitsurugi.ID, Lantern.ID, AbstractRelic.RelicTier.COMMON, R4);

        AddBundle(Megumin.ID, StoneCalendar.ID, AbstractRelic.RelicTier.RARE, R2);
        AddBundle(YunYun.ID, WarPaint.ID, AbstractRelic.RelicTier.COMMON, R3);
        AddBundle(Chris.ID, OldCoin.ID, AbstractRelic.RelicTier.COMMON, R1);
        AddBundle(Vanir.ID, SmilingMask.ID, AbstractRelic.RelicTier.COMMON, R2);

        AddBundle(Eris.ID, TinyChest.ID, AbstractRelic.RelicTier.COMMON, R3);
        AddBundle(Wiz.ID, JuzuBracelet.ID, AbstractRelic.RelicTier.COMMON, R5);
    }

    private static void AddNoGameNoLife()
    {
        AddBundle(Jibril.ID, WhiteBeast.ID, AbstractRelic.RelicTier.BOSS, R1);
        AddBundle(DolaCouronne.ID, ClockworkSouvenir.ID, AbstractRelic.RelicTier.SHOP, R3);
        AddBundle(ChlammyZell.ID, GamblingChip.ID, AbstractRelic.RelicTier.RARE, R3);
        AddBundle(DolaSchwi.ID, EmotionChip.ID, AbstractRelic.RelicTier.SPECIAL, R6);

        AddBundle(Tet.ID, Sundial.ID, AbstractRelic.RelicTier.UNCOMMON, R1);
        AddBundle(IzunaHatsuse.ID, Torii.ID, AbstractRelic.RelicTier.RARE, R1);
        AddBundle(DolaRiku.ID, UnceasingTop.ID, AbstractRelic.RelicTier.RARE, R1);
        AddBundle(DolaStephanie.ID, HappyFlower.ID, AbstractRelic.RelicTier.COMMON, R3);

        AddBundle(Shiro.ID, Dodecahedron.ID, AbstractRelic.RelicTier.UNCOMMON, R2);
        AddBundle(Sora.ID, QuestionCard.ID, AbstractRelic.RelicTier.UNCOMMON, R1);
    }

    private static void AddOverlord()
    {
        AddBundle(Cocytus.ID, BronzeScales.ID, AbstractRelic.RelicTier.COMMON, R3);
        AddBundle(Demiurge.ID, RedSkull.ID, AbstractRelic.RelicTier.SPECIAL, R5);
        AddBundle(PandorasActor.ID, SelfFormingClay.ID, AbstractRelic.RelicTier.SPECIAL, R4);
        AddBundle(Shalltear.ID, Vajra.ID, AbstractRelic.RelicTier.COMMON, R2);

        AddBundle(Evileye.ID, BloodVial.ID, AbstractRelic.RelicTier.SPECIAL, R3);
        AddBundle(NarberalGamma.ID, CrackedCore.ID, AbstractRelic.RelicTier.SPECIAL, R2);
        AddBundle(Entoma.ID, TheSpecimen.ID, AbstractRelic.RelicTier.SPECIAL, R3);
        AddBundle(Sebas.ID, SingingBowl.ID, AbstractRelic.RelicTier.SPECIAL, R1);

        AddBundle(Albedo.ID, DarkstonePeriapt.ID, AbstractRelic.RelicTier.UNCOMMON, R5);
        AddBundle(Ainz.ID, EngravedStaff.ID, AbstractRelic.RelicTier.RARE, R3);
    }

    private static void AddOwariNoSeraph()
    {
        AddBundle(Shinoa.ID, Calipers.ID, AbstractRelic.RelicTier.RARE, R2);
        AddBundle(Mitsuba.ID, Courier.ID, AbstractRelic.RelicTier.UNCOMMON, R3);
        AddBundle(Yuuichirou.ID, Sling.ID, AbstractRelic.RelicTier.SHOP, R3);
        AddBundle(CrowleyEusford.ID, BloodVial.ID, AbstractRelic.RelicTier.SPECIAL, R2);

        AddBundle(KrulTepes.ID, MagicFlower.ID, AbstractRelic.RelicTier.SPECIAL, R2);
        AddBundle(Mikaela.ID, CharonsAshes.ID, AbstractRelic.RelicTier.SPECIAL, R4);
        AddBundle(Shigure.ID, Kunai.ID, AbstractRelic.RelicTier.RARE, R1);
        AddBundle(HiiragiShinya.ID, Pantograph.ID, AbstractRelic.RelicTier.UNCOMMON, R3);

        AddBundle(FeridBathory.ID, MealTicket.ID, AbstractRelic.RelicTier.SHOP, R5);
        AddBundle(Guren.ID, IncenseBurner.ID, AbstractRelic.RelicTier.RARE, R2);
    }

    private static void AddTenSura()
    {
        AddBundle(Millim.ID, RedSkull.ID, AbstractRelic.RelicTier.SPECIAL, R1);
        AddBundle(Kaijin.ID, Orichalcum.ID, AbstractRelic.RelicTier.COMMON, R2);
        AddBundle(Hakurou.ID, Nunchaku.ID, AbstractRelic.RelicTier.RARE, R2);
        AddBundle(Benimaru.ID, BurningBlood.ID, AbstractRelic.RelicTier.SPECIAL, R2);

        AddBundle(Souei.ID, NinjaScroll.ID, AbstractRelic.RelicTier.SPECIAL, R2);
        AddBundle(Shuna.ID, Torii.ID, AbstractRelic.RelicTier.RARE, R2);
        AddBundle(GazelDwargon.ID, RegalPillow.ID, AbstractRelic.RelicTier.COMMON, R4);
        AddBundle(Vesta.ID, ChemicalX.ID, AbstractRelic.RelicTier.SHOP, R6);

        AddBundle(Shizu.ID, BurningBlood.ID, AbstractRelic.RelicTier.SPECIAL, R1);
        AddBundle(Rimuru.ID, OddlySmoothStone.ID, AbstractRelic.RelicTier.COMMON, R2);
    }

    private static void AddSpecial()
    {
        AddBundle(Shimakaze.ID, Anchor.ID, AbstractRelic.RelicTier.COMMON, R4);

        AddBundle(Urushihara.ID, WingBoots.ID, AbstractRelic.RelicTier.RARE, R1);
    }
}