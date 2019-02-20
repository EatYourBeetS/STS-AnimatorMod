package eatyourbeets.misc;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.cards.animator.*;
import eatyourbeets.powers.PlayerStatistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BundledRelicProvider
{
    private static final Map<String, BundledRelic> bundledRelicsPool = new HashMap<>();
    private static final ArrayList<BundledRelicContainer> currentBundles = new ArrayList<>();

    private static AbstractRoom lastRoom = null;

    public static BundledRelicContainer SetupBundledRelics(RewardItem rItem, ArrayList<AbstractCard> cards)
    {
        AbstractRoom room = PlayerStatistics.CurrentRoom();
        if (lastRoom != room)
        {
            lastRoom = room;
            currentBundles.clear();
        }

        for (BundledRelicContainer bundle : currentBundles)
        {
            if (bundle.rewardItem == rItem)
            {
                return bundle;
            }
        }

        BundledRelicContainer bundle = new BundledRelicContainer(rItem);
        for (AbstractCard c : cards)
        {
            if (bundledRelicsPool.containsKey(c.cardID))
            {
                BundledRelic relic = bundledRelicsPool.get(c.cardID).Clone(AbstractDungeon.miscRng.random(99), c);
                bundle.bundledRelics.add(relic);
            }
        }

        return bundle;
    }

    private static void AddBundle(String cardID, String relicID, AbstractRelic.RelicTier tier, int chance)
    {
        bundledRelicsPool.put(cardID, new BundledRelic(cardID, relicID, tier, chance));
    }

    static
    {
        // <Common>
        AddBundle(DwarfShaman.ID, HandDrill.ID, AbstractRelic.RelicTier.SHOP, 7);
        AddBundle(GuildGirl.ID, LetterOpener.ID, AbstractRelic.RelicTier.UNCOMMON, 7);
        AddBundle(Priestess.ID, IceCream.ID, AbstractRelic.RelicTier.RARE, 5);
        AddBundle(Aqua.ID, MagicFlower.ID, AbstractRelic.RelicTier.SPECIAL, 7);
        AddBundle(Ara.ID, Kunai.ID, AbstractRelic.RelicTier.UNCOMMON, 6);
        AddBundle(Archer.ID, ArtOfWar.ID, AbstractRelic.RelicTier.COMMON, 7);
        AddBundle(Berserker.ID, Girya.ID, AbstractRelic.RelicTier.RARE, 9);
        AddBundle(ChaikaBohdan.ID, MeatOnTheBone.ID, AbstractRelic.RelicTier.UNCOMMON, 7);
        AddBundle(Chung.ID, MercuryHourglass.ID, AbstractRelic.RelicTier.UNCOMMON, 7);
        AddBundle(Cocytus.ID, BronzeScales.ID, AbstractRelic.RelicTier.COMMON, 9);
        AddBundle(DolaCouronne.ID, ClockworkSouvenir.ID, AbstractRelic.RelicTier.SHOP, 7);
        AddBundle(Darkness.ID, RunicCube.ID, AbstractRelic.RelicTier.SPECIAL, 7);
        AddBundle(Demiurge.ID, RedSkull.ID, AbstractRelic.RelicTier.SPECIAL, 16);
        AddBundle(Elsword.ID, Whetstone.ID, AbstractRelic.RelicTier.COMMON, 7);
        AddBundle(Emonzaemon.ID, Shuriken.ID, AbstractRelic.RelicTier.UNCOMMON, 6);
        AddBundle(Gillette.ID, BagOfPreparation.ID, AbstractRelic.RelicTier.COMMON, 7);
        AddBundle(Guy.ID, FrozenEye.ID, AbstractRelic.RelicTier.SHOP, 11);
        AddBundle(Hitei.ID, OrnamentalFan.ID, AbstractRelic.RelicTier.UNCOMMON, 28);
        AddBundle(Jibril.ID, WhiteBeast.ID, AbstractRelic.RelicTier.BOSS, 4);
        AddBundle(Kazuma.ID, OldCoin.ID, AbstractRelic.RelicTier.RARE, 6);
        AddBundle(Konayuki.ID, Girya.ID, AbstractRelic.RelicTier.RARE, 14);
        AddBundle(ChlammyZell.ID, GamblingChip.ID, AbstractRelic.RelicTier.RARE, 12);
        AddBundle(Kuribayashi.ID, Ginger.ID, AbstractRelic.RelicTier.RARE, 8);
        AddBundle(Mikaela.ID, CharonsAshes.ID, AbstractRelic.RelicTier.SPECIAL, 12);
        AddBundle(Mitsuba.ID, Courier.ID, AbstractRelic.RelicTier.UNCOMMON, 10);
        AddBundle(Mitsurugi.ID, Lantern.ID, AbstractRelic.RelicTier.COMMON, 10);
        AddBundle(PandorasActor.ID, SelfFormingClay.ID, AbstractRelic.RelicTier.SPECIAL, 16);
        AddBundle(RinTohsaka.ID, GoldPlatedCables.ID, AbstractRelic.RelicTier.SPECIAL, 12);
        AddBundle(Shalltear.ID, Vajra.ID, AbstractRelic.RelicTier.COMMON, 6);
        AddBundle(AcuraTooru.ID, WristBlade.ID, AbstractRelic.RelicTier.SPECIAL, 7);
        AddBundle(TukaLunaMarceau.ID, Strawberry.ID, AbstractRelic.RelicTier.COMMON, 7);
        AddBundle(Tyuule.ID, TwistedFunnel.ID, AbstractRelic.RelicTier.SPECIAL, 7);
        AddBundle(Yuuichirou.ID, Sling.ID, AbstractRelic.RelicTier.SHOP, 9);

        // <Uncommon>
        AddBundle(HighElfArcher.ID, OddlySmoothStone.ID, AbstractRelic.RelicTier.COMMON, 8);
        AddBundle(LizardPriest.ID, LizardTail.ID, AbstractRelic.RelicTier.BOSS, 4);
        AddBundle(Aisha.ID, Cauldron.ID, AbstractRelic.RelicTier.SHOP, 12);
        AddBundle(Alexander.ID, ArtOfWar.ID, AbstractRelic.RelicTier.COMMON, 12);
        AddBundle(Azekura.ID, ChampionsBelt.ID, AbstractRelic.RelicTier.SPECIAL, 11);
        AddBundle(Caster.ID, SymbioticVirus.ID, AbstractRelic.RelicTier.SPECIAL, 14);
        AddBundle(Entoma.ID, TheSpecimen.ID, AbstractRelic.RelicTier.SPECIAL, 9);
        AddBundle(Fredrika.ID, Matryoshka.ID, AbstractRelic.RelicTier.UNCOMMON, 8);
        AddBundle(NarberalGamma.ID, CrackedCore.ID, AbstractRelic.RelicTier.SPECIAL, 12);
        AddBundle(Illya.ID, SelfFormingClay.ID, AbstractRelic.RelicTier.SPECIAL, 13);
        AddBundle(KrulTepes.ID, MagicFlower.ID, AbstractRelic.RelicTier.SPECIAL, 9);
        AddBundle(Lancer.ID, Nunchaku.ID, AbstractRelic.RelicTier.RARE, 7);
        AddBundle(Layla.ID, PotionBelt.ID, AbstractRelic.RelicTier.COMMON, 15);
        AddBundle(LeleiLaLalena.ID, Abacus.ID, AbstractRelic.RelicTier.SHOP, 11);
        AddBundle(Megumin.ID, StoneCalendar.ID, AbstractRelic.RelicTier.RARE, 10);
        AddBundle(Nanami.ID, PaperCrane.ID, AbstractRelic.RelicTier.SPECIAL, 7);
        AddBundle(PinaCoLada.ID, Mango.ID, AbstractRelic.RelicTier.RARE, 7);
        AddBundle(Rena.ID, BagOfMarbles.ID, AbstractRelic.RelicTier.COMMON, 7);
        AddBundle(DolaRiku.ID, UnceasingTop.ID, AbstractRelic.RelicTier.RARE, 7);
        AddBundle(RoryMercury.ID, PenNib.ID, AbstractRelic.RelicTier.COMMON, 7);
        AddBundle(DolaSchwi.ID, EmotionChip.ID, AbstractRelic.RelicTier.SPECIAL, 14);
        AddBundle(Shichika.ID, PaperFrog.ID, AbstractRelic.RelicTier.SPECIAL, 7);
        AddBundle(Shimakaze.ID, Anchor.ID, AbstractRelic.RelicTier.COMMON, 7);
        AddBundle(Shinoa.ID, Calipers.ID, AbstractRelic.RelicTier.RARE, 7);
        AddBundle(DolaStephanie.ID, HappyFlower.ID, AbstractRelic.RelicTier.COMMON, 7);
        AddBundle(Togame.ID, AncientTeaSet.ID, AbstractRelic.RelicTier.COMMON, 11);
        AddBundle(Viivi.ID, Kunai.ID, AbstractRelic.RelicTier.UNCOMMON, 11);
        AddBundle(YaoHaDucy.ID, DreamCatcher.ID, AbstractRelic.RelicTier.COMMON, 11);
        AddBundle(YunYun.ID, WarPaint.ID, AbstractRelic.RelicTier.COMMON, 13);

        // <Rare>
        AddBundle(SwordMaiden.ID, BlueCandle.ID, AbstractRelic.RelicTier.UNCOMMON, 11);
        AddBundle(Ainz.ID, GremlinHorn.ID, AbstractRelic.RelicTier.UNCOMMON, 12);
        AddBundle(ChaikaTrabant.ID, MummifiedHand.ID, AbstractRelic.RelicTier.UNCOMMON, 12);
        AddBundle(Elesis.ID, MoltenEgg2.ID, AbstractRelic.RelicTier.UNCOMMON, 12);
        AddBundle(Eris.ID, TinyChest.ID, AbstractRelic.RelicTier.COMMON, 8);
        AddBundle(Eve.ID, DataDisk.ID, AbstractRelic.RelicTier.SPECIAL, 16);
        AddBundle(FeridBathory.ID, MealTicket.ID, AbstractRelic.RelicTier.SHOP, 16);
        AddBundle(Gilgamesh.ID, RegalPillow.ID, AbstractRelic.RelicTier.COMMON, 8);
        AddBundle(GoblinSlayer.ID, MarkOfPain.ID, AbstractRelic.RelicTier.SPECIAL, 8);
        AddBundle(Guren.ID, IncenseBurner.ID, AbstractRelic.RelicTier.RARE, 8);
        AddBundle(HigakiRinne.ID, SpiritPoop.ID, AbstractRelic.RelicTier.SPECIAL, 18);
        AddBundle(ItamiYouji.ID, Boot.ID, AbstractRelic.RelicTier.COMMON, 13);
        AddBundle(Saber.ID, Orichalcum.ID, AbstractRelic.RelicTier.COMMON, 12);
        AddBundle(AcuraShin.ID, NinjaScroll.ID, AbstractRelic.RelicTier.SPECIAL, 11);
        AddBundle(Shiro.ID, Dodecahedron.ID, AbstractRelic.RelicTier.UNCOMMON, 11);
        AddBundle(Sora.ID, QuestionCard.ID, AbstractRelic.RelicTier.UNCOMMON, 12);
        AddBundle(Wiz.ID, JuzuBracelet.ID, AbstractRelic.RelicTier.COMMON, 16);

//        // <Common>
//        AddBundle(DwarfShaman.ID, HandDrill.ID, AbstractRelic.RelicTier.SHOP, 8);
//        AddBundle(GuildGirl.ID, LetterOpener.ID, AbstractRelic.RelicTier.UNCOMMON, 8);
//        AddBundle(Priestess.ID, IceCream.ID, AbstractRelic.RelicTier.RARE, 8);
//        AddBundle(Aqua.ID, MagicFlower.ID, AbstractRelic.RelicTier.SPECIAL, 16);
//        AddBundle(Ara.ID, Kunai.ID, AbstractRelic.RelicTier.UNCOMMON, 16);
//        AddBundle(Archer.ID, ArtOfWar.ID, AbstractRelic.RelicTier.COMMON, 8);
//        AddBundle(Berserker.ID, Girya.ID, AbstractRelic.RelicTier.RARE, 16);
//        AddBundle(ChaikaBohdan.ID, MeatOnTheBone.ID, AbstractRelic.RelicTier.UNCOMMON, 8);
//        AddBundle(Chung.ID, MercuryHourglass.ID, AbstractRelic.RelicTier.UNCOMMON, 8);
//        AddBundle(Cocytus.ID, BronzeScales.ID, AbstractRelic.RelicTier.COMMON, 12);
//        AddBundle(DolaCouronne.ID, ClockworkSouvenir.ID, AbstractRelic.RelicTier.SHOP, 8);
//        AddBundle(Darkness.ID, RunicCube.ID, AbstractRelic.RelicTier.SPECIAL, 8);
//        AddBundle(Demiurge.ID, RedSkull.ID, AbstractRelic.RelicTier.SPECIAL, 28);
//        AddBundle(Elsword.ID, Whetstone.ID, AbstractRelic.RelicTier.COMMON, 8);
//        AddBundle(Emonzaemon.ID, Shuriken.ID, AbstractRelic.RelicTier.UNCOMMON, 8);
//        AddBundle(Gillette.ID, BagOfPreparation.ID, AbstractRelic.RelicTier.COMMON, 8);
//        AddBundle(Guy.ID, FrozenEye.ID, AbstractRelic.RelicTier.SHOP, 18);
//        AddBundle(Hitei.ID, OrnamentalFan.ID, AbstractRelic.RelicTier.UNCOMMON, 28);
//        AddBundle(Jibril.ID, WhiteBeast.ID, AbstractRelic.RelicTier.BOSS, 8);
//        AddBundle(Kazuma.ID, OldCoin.ID, AbstractRelic.RelicTier.RARE, 8);
//        AddBundle(Konayuki.ID, Girya.ID, AbstractRelic.RelicTier.RARE, 16);
//        AddBundle(ChlammyZell.ID, GamblingChip.ID, AbstractRelic.RelicTier.RARE, 16);
//        AddBundle(Kuribayashi.ID, Ginger.ID, AbstractRelic.RelicTier.RARE, 8);
//        AddBundle(Mikaela.ID, CharonsAshes.ID, AbstractRelic.RelicTier.SPECIAL, 16);
//        AddBundle(Mitsuba.ID, Courier.ID, AbstractRelic.RelicTier.UNCOMMON, 16);
//        AddBundle(Mitsurugi.ID, Lantern.ID, AbstractRelic.RelicTier.COMMON, 16);
//        AddBundle(PandorasActor.ID, SelfFormingClay.ID, AbstractRelic.RelicTier.SPECIAL, 24);
//        AddBundle(RinTohsaka.ID, GoldPlatedCables.ID, AbstractRelic.RelicTier.SPECIAL, 14);
//        AddBundle(Shalltear.ID, Vajra.ID, AbstractRelic.RelicTier.COMMON, 8);
//        AddBundle(AcuraTooru.ID, WristBlade.ID, AbstractRelic.RelicTier.SPECIAL, 8);
//        AddBundle(TukaLunaMarceau.ID, Strawberry.ID, AbstractRelic.RelicTier.COMMON, 8);
//        AddBundle(Tyuule.ID, TwistedFunnel.ID, AbstractRelic.RelicTier.SPECIAL, 14);
//        AddBundle(Yuuichirou.ID, Sling.ID, AbstractRelic.RelicTier.SHOP, 14);
//
//        // <Uncommon>
//        AddBundle(HighElfArcher.ID, OddlySmoothStone.ID, AbstractRelic.RelicTier.BOSS, 12);
//        AddBundle(LizardPriest.ID, LizardTail.ID, AbstractRelic.RelicTier.BOSS, 12);
//        AddBundle(Aisha.ID, Cauldron.ID, AbstractRelic.RelicTier.SHOP, 12);
//        AddBundle(Alexander.ID, ArtOfWar.ID, AbstractRelic.RelicTier.COMMON, 14);
//        AddBundle(Azekura.ID, ChampionsBelt.ID, AbstractRelic.RelicTier.SPECIAL, 14);
//        AddBundle(Caster.ID, SymbioticVirus.ID, AbstractRelic.RelicTier.SPECIAL, 14);
//        AddBundle(Entoma.ID, TheSpecimen.ID, AbstractRelic.RelicTier.SPECIAL, 18);
//        AddBundle(Fredrika.ID, Matryoshka.ID, AbstractRelic.RelicTier.UNCOMMON, 8);
//        AddBundle(NarberalGamma.ID, CrackedCore.ID, AbstractRelic.RelicTier.SPECIAL, 12);
//        AddBundle(Illya.ID, SelfFormingClay.ID, AbstractRelic.RelicTier.SPECIAL, 12);
//        AddBundle(KrulTepes.ID, MagicFlower.ID, AbstractRelic.RelicTier.SPECIAL, 12);
//        AddBundle(Lancer.ID, Nunchaku.ID, AbstractRelic.RelicTier.RARE, 12);
//        AddBundle(Layla.ID, PotionBelt.ID, AbstractRelic.RelicTier.COMMON, 12);
//        AddBundle(LeleiLaLalena.ID, Abacus.ID, AbstractRelic.RelicTier.SHOP, 12);
//        AddBundle(Megumin.ID, StoneCalendar.ID, AbstractRelic.RelicTier.RARE, 12);
//        AddBundle(Nanami.ID, PaperCrane.ID, AbstractRelic.RelicTier.SPECIAL, 8);
//        AddBundle(PinaCoLada.ID, Mango.ID, AbstractRelic.RelicTier.RARE, 8);
//        AddBundle(Rena.ID, BagOfMarbles.ID, AbstractRelic.RelicTier.COMMON, 8);
//        AddBundle(DolaRiku.ID, UnceasingTop.ID, AbstractRelic.RelicTier.UNCOMMON, 8);
//        AddBundle(RoryMercury.ID, PenNib.ID, AbstractRelic.RelicTier.COMMON, 8);
//        AddBundle(DolaSchwi.ID, EmotionChip.ID, AbstractRelic.RelicTier.SPECIAL, 14);
//        AddBundle(Shichika.ID, PaperFrog.ID, AbstractRelic.RelicTier.SPECIAL, 8);
//        AddBundle(Shimakaze.ID, Anchor.ID, AbstractRelic.RelicTier.COMMON, 8);
//        AddBundle(Shinoa.ID, Calipers.ID, AbstractRelic.RelicTier.RARE, 8);
//        AddBundle(HappyFlower.ID, DolaStephanie.ID, AbstractRelic.RelicTier.COMMON, 8);
//        AddBundle(Togame.ID, AncientTeaSet.ID, AbstractRelic.RelicTier.COMMON, 16);
//        AddBundle(Viivi.ID, Kunai.ID, AbstractRelic.RelicTier.UNCOMMON, 16);
//        AddBundle(YaoHaDucy.ID, DreamCatcher.ID, AbstractRelic.RelicTier.COMMON, 16);
//        AddBundle(YunYun.ID, WarPaint.ID, AbstractRelic.RelicTier.COMMON, 16);
//
//        // <Rare>
//        AddBundle(BlueCandle.ID, MummifiedHand.ID, AbstractRelic.RelicTier.UNCOMMON, 12);
//        AddBundle(Ainz.ID, GremlinHorn.ID, AbstractRelic.RelicTier.UNCOMMON, 12);
//        AddBundle(ChaikaTrabant.ID, MummifiedHand.ID, AbstractRelic.RelicTier.UNCOMMON, 12);
//        AddBundle(Elesis.ID, MoltenEgg2.ID, AbstractRelic.RelicTier.UNCOMMON, 12);
//        AddBundle(Eris.ID, TinyChest.ID, AbstractRelic.RelicTier.COMMON, 8);
//        AddBundle(Eve.ID, DataDisk.ID, AbstractRelic.RelicTier.SPECIAL, 16);
//        AddBundle(FeridBathory.ID, MealTicket.ID, AbstractRelic.RelicTier.SHOP, 16);
//        AddBundle(Gilgamesh.ID, RegalPillow.ID, AbstractRelic.RelicTier.COMMON, 8);
//        AddBundle(GoblinSlayer.ID, MarkOfPain.ID, AbstractRelic.RelicTier.SPECIAL, 8);
//        AddBundle(Guren.ID, IncenseBurner.ID, AbstractRelic.RelicTier.RARE, 8);
//        AddBundle(HigakiRinne.ID, SpiritPoop.ID, AbstractRelic.RelicTier.SPECIAL, 12);
//        AddBundle(ItamiYouji.ID, Boot.ID, AbstractRelic.RelicTier.COMMON, 12);
//        AddBundle(Saber.ID, Orichalcum.ID, AbstractRelic.RelicTier.COMMON, 12);
//        AddBundle(AcuraShin.ID, NinjaScroll.ID, AbstractRelic.RelicTier.SPECIAL, 12);
//        AddBundle(Shiro.ID, Dodecahedron.ID, AbstractRelic.RelicTier.UNCOMMON, 12);
//        AddBundle(Sora.ID, QuestionCard.ID, AbstractRelic.RelicTier.UNCOMMON, 12);
//        AddBundle(Wiz.ID, JuzuBracelet.ID, AbstractRelic.RelicTier.COMMON, 12);
    }
}