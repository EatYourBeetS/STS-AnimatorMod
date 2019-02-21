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
        // <Common>
        AddBundle(DwarfShaman.ID, HandDrill.ID, AbstractRelic.RelicTier.SHOP, 6);
        AddBundle(GuildGirl.ID, LetterOpener.ID, AbstractRelic.RelicTier.UNCOMMON, 6);
        AddBundle(Priestess.ID, IceCream.ID, AbstractRelic.RelicTier.RARE, 4);
        AddBundle(Aqua.ID, MagicFlower.ID, AbstractRelic.RelicTier.SPECIAL, 6);
        AddBundle(Ara.ID, Kunai.ID, AbstractRelic.RelicTier.UNCOMMON, 5);
        AddBundle(Archer.ID, ArtOfWar.ID, AbstractRelic.RelicTier.COMMON, 6);
        AddBundle(Berserker.ID, Girya.ID, AbstractRelic.RelicTier.RARE, 10);
        AddBundle(ChaikaBohdan.ID, MeatOnTheBone.ID, AbstractRelic.RelicTier.UNCOMMON, 7);
        AddBundle(Chung.ID, MercuryHourglass.ID, AbstractRelic.RelicTier.UNCOMMON, 7);
        AddBundle(Cocytus.ID, BronzeScales.ID, AbstractRelic.RelicTier.COMMON, 9);
        AddBundle(DolaCouronne.ID, ClockworkSouvenir.ID, AbstractRelic.RelicTier.SHOP, 6);
        AddBundle(Darkness.ID, RunicCube.ID, AbstractRelic.RelicTier.SPECIAL, 6);
        AddBundle(Demiurge.ID, RedSkull.ID, AbstractRelic.RelicTier.SPECIAL, 11);
        AddBundle(Elsword.ID, Whetstone.ID, AbstractRelic.RelicTier.COMMON, 6);
        AddBundle(Emonzaemon.ID, Shuriken.ID, AbstractRelic.RelicTier.UNCOMMON, 5);
        AddBundle(Gillette.ID, BagOfPreparation.ID, AbstractRelic.RelicTier.COMMON, 6);
        AddBundle(Guy.ID, FrozenEye.ID, AbstractRelic.RelicTier.SHOP, 9);
        AddBundle(Hitei.ID, OrnamentalFan.ID, AbstractRelic.RelicTier.UNCOMMON, 16);
        AddBundle(Jibril.ID, WhiteBeast.ID, AbstractRelic.RelicTier.BOSS, 4);
        AddBundle(Kazuma.ID, OldCoin.ID, AbstractRelic.RelicTier.RARE, 6);
        AddBundle(Konayuki.ID, Girya.ID, AbstractRelic.RelicTier.RARE, 12);
        AddBundle(ChlammyZell.ID, GamblingChip.ID, AbstractRelic.RelicTier.RARE, 7);
        AddBundle(Kuribayashi.ID, Ginger.ID, AbstractRelic.RelicTier.RARE, 7);
        AddBundle(Mikaela.ID, CharonsAshes.ID, AbstractRelic.RelicTier.SPECIAL, 11);
        AddBundle(Mitsuba.ID, Courier.ID, AbstractRelic.RelicTier.UNCOMMON, 8);
        AddBundle(Mitsurugi.ID, Lantern.ID, AbstractRelic.RelicTier.COMMON, 8);
        AddBundle(PandorasActor.ID, SelfFormingClay.ID, AbstractRelic.RelicTier.SPECIAL, 12);
        AddBundle(RinTohsaka.ID, GoldPlatedCables.ID, AbstractRelic.RelicTier.SPECIAL, 8);
        AddBundle(Shalltear.ID, Vajra.ID, AbstractRelic.RelicTier.COMMON, 6);
        AddBundle(AcuraTooru.ID, WristBlade.ID, AbstractRelic.RelicTier.SPECIAL, 5);
        AddBundle(TukaLunaMarceau.ID, Strawberry.ID, AbstractRelic.RelicTier.COMMON, 7);
        AddBundle(Tyuule.ID, TwistedFunnel.ID, AbstractRelic.RelicTier.SPECIAL, 5);
        AddBundle(Yuuichirou.ID, Sling.ID, AbstractRelic.RelicTier.SHOP, 6);

        // <Uncommon>
        AddBundle(AcuraAkari.ID, SneckoSkull.ID, AbstractRelic.RelicTier.SPECIAL, 6);
        AddBundle(HighElfArcher.ID, OddlySmoothStone.ID, AbstractRelic.RelicTier.COMMON, 8);
        AddBundle(LizardPriest.ID, LizardTail.ID, AbstractRelic.RelicTier.BOSS, 5);
        AddBundle(Aisha.ID, Cauldron.ID, AbstractRelic.RelicTier.SHOP, 7);
        AddBundle(Alexander.ID, ArtOfWar.ID, AbstractRelic.RelicTier.COMMON, 8);
        AddBundle(Azekura.ID, ChampionsBelt.ID, AbstractRelic.RelicTier.SPECIAL, 8);
        AddBundle(Caster.ID, SymbioticVirus.ID, AbstractRelic.RelicTier.SPECIAL, 11);
        AddBundle(Entoma.ID, TheSpecimen.ID, AbstractRelic.RelicTier.SPECIAL, 9);
        AddBundle(Fredrika.ID, Matryoshka.ID, AbstractRelic.RelicTier.UNCOMMON, 7);
        AddBundle(NarberalGamma.ID, CrackedCore.ID, AbstractRelic.RelicTier.SPECIAL, 9);
        AddBundle(Illya.ID, SelfFormingClay.ID, AbstractRelic.RelicTier.SPECIAL, 11);
        AddBundle(KrulTepes.ID, MagicFlower.ID, AbstractRelic.RelicTier.SPECIAL, 6);
        AddBundle(Lancer.ID, Nunchaku.ID, AbstractRelic.RelicTier.RARE, 6);
        AddBundle(Layla.ID, PotionBelt.ID, AbstractRelic.RelicTier.COMMON, 12);
        AddBundle(LeleiLaLalena.ID, Abacus.ID, AbstractRelic.RelicTier.SHOP, 8);
        AddBundle(Megumin.ID, StoneCalendar.ID, AbstractRelic.RelicTier.RARE, 6);
        AddBundle(Nanami.ID, PaperCrane.ID, AbstractRelic.RelicTier.SPECIAL, 7);
        AddBundle(PinaCoLada.ID, Mango.ID, AbstractRelic.RelicTier.RARE, 6);
        AddBundle(Rena.ID, BagOfMarbles.ID, AbstractRelic.RelicTier.COMMON, 7);
        AddBundle(DolaRiku.ID, UnceasingTop.ID, AbstractRelic.RelicTier.RARE, 6);
        AddBundle(RoryMercury.ID, PenNib.ID, AbstractRelic.RelicTier.COMMON, 7);
        AddBundle(DolaSchwi.ID, EmotionChip.ID, AbstractRelic.RelicTier.SPECIAL, 12);
        AddBundle(Shichika.ID, PaperFrog.ID, AbstractRelic.RelicTier.SPECIAL, 7);
        AddBundle(Shimakaze.ID, Anchor.ID, AbstractRelic.RelicTier.COMMON, 7);
        AddBundle(Shinoa.ID, Calipers.ID, AbstractRelic.RelicTier.RARE, 6);
        AddBundle(DolaStephanie.ID, HappyFlower.ID, AbstractRelic.RelicTier.COMMON, 7);
        AddBundle(Togame.ID, AncientTeaSet.ID, AbstractRelic.RelicTier.COMMON, 8);
        AddBundle(Viivi.ID, Kunai.ID, AbstractRelic.RelicTier.UNCOMMON, 7);
        AddBundle(YaoHaDucy.ID, DreamCatcher.ID, AbstractRelic.RelicTier.COMMON, 8);
        AddBundle(YunYun.ID, WarPaint.ID, AbstractRelic.RelicTier.COMMON, 8);

        // <Rare>
        AddBundle(SwordMaiden.ID, BlueCandle.ID, AbstractRelic.RelicTier.UNCOMMON, 7);
        AddBundle(Ainz.ID, GremlinHorn.ID, AbstractRelic.RelicTier.UNCOMMON, 8);
        AddBundle(ChaikaTrabant.ID, MummifiedHand.ID, AbstractRelic.RelicTier.UNCOMMON, 7);
        AddBundle(Elesis.ID, MoltenEgg2.ID, AbstractRelic.RelicTier.UNCOMMON, 9);
        AddBundle(Eris.ID, TinyChest.ID, AbstractRelic.RelicTier.COMMON, 8);
        AddBundle(Eve.ID, DataDisk.ID, AbstractRelic.RelicTier.SPECIAL, 14);
        AddBundle(FeridBathory.ID, MealTicket.ID, AbstractRelic.RelicTier.SHOP, 10);
        AddBundle(Gilgamesh.ID, RegalPillow.ID, AbstractRelic.RelicTier.COMMON, 4);
        AddBundle(GoblinSlayer.ID, MarkOfPain.ID, AbstractRelic.RelicTier.SPECIAL, 7);
        AddBundle(Guren.ID, IncenseBurner.ID, AbstractRelic.RelicTier.RARE, 7);
        AddBundle(HigakiRinne.ID, SpiritPoop.ID, AbstractRelic.RelicTier.SPECIAL, 16);
        AddBundle(ItamiYouji.ID, Boot.ID, AbstractRelic.RelicTier.COMMON, 10);
        AddBundle(Saber.ID, Orichalcum.ID, AbstractRelic.RelicTier.COMMON, 8);
        AddBundle(AcuraShin.ID, NinjaScroll.ID, AbstractRelic.RelicTier.SPECIAL, 10);
        AddBundle(Shiro.ID, Dodecahedron.ID, AbstractRelic.RelicTier.UNCOMMON, 9);
        AddBundle(Sora.ID, QuestionCard.ID, AbstractRelic.RelicTier.UNCOMMON, 7);
        AddBundle(Wiz.ID, JuzuBracelet.ID, AbstractRelic.RelicTier.COMMON, 12);
    }
}