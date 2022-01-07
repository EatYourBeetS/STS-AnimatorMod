package pinacolada.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLJUtils;

import java.util.*;

public class CardSeries
{
    private final static HashMap<Integer, CardSeries> mapIDs = new HashMap<>();
    private final static HashMap<String, CardSeries> mapNames = new HashMap<>();

    public final static CardSeries ANY = Add(-1, "ANY");
    public final static CardSeries COLORLESS = Add(0, "COLORLESS");
    public final static CardSeries Elsword = Add(1, "Elsword");
    public final static CardSeries Kancolle = Add(2, "Kancolle");
    public final static CardSeries HitsugiNoChaika = Add(3, "HitsugiNoChaika");
    public final static CardSeries Konosuba = Add(4, "Konosuba");
    public final static CardSeries Katanagatari = Add(5, "Katanagatari");
    public final static CardSeries OwariNoSeraph = Add(6, "OwariNoSeraph");
    public final static CardSeries Overlord = Add(7, "Overlord");
    public final static CardSeries NoGameNoLife = Add(8, "NoGameNoLife");
    public final static CardSeries GATE = Add(9, "GATE");
    public final static CardSeries Fate = Add(10, "Fate");
    public final static CardSeries GoblinSlayer = Add(11, "GoblinSlayer");
    public final static CardSeries FullmetalAlchemist = Add(12, "FullmetalAlchemist");
    public final static CardSeries HatarakuMaouSama = Add(13, "HatarakuMaouSama");
    public final static CardSeries GrimoireOfZero = Add(14, "GrimoireOfZero");
    public final static CardSeries SteinsGate = Add(15, "SteinsGate");
    public final static CardSeries TenseiSlime = Add(16, "TenseiSlime");
    public final static CardSeries ReZero = Add(17, "ReZero");
    public final static CardSeries MadokaMagica = Add(18, "MadokaMagica");
    public final static CardSeries Charlotte = Add(19, "Charlotte");
    public final static CardSeries AccelWorld = Add(20, "AccelWorld");
    public final static CardSeries Noragami = Add(21, "Noragami");
    public final static CardSeries OnePunchMan = Add(22, "OnePunchMan");
    public final static CardSeries PandoraHearts = Add(23, "PandoraHearts");
    public final static CardSeries ZeroNoTsukaima = Add(24, "ZeroNoTsukaima");
    public final static CardSeries GabrielDropout = Add(25, "GabrielDropout");
    public final static CardSeries DeathNote = Add(26, "DeathNote");
    public final static CardSeries KamisamaNoMemochu = Add(27, "KamisamaNoMemochou");
    public final static CardSeries CodeGeass = Add(28, "CodeGeass");
    public final static CardSeries YoujoSenki = Add(29, "YoujoSenki");
    public final static CardSeries Bleach = Add(30, "Bleach");
    public final static CardSeries Jojo = Add(31, "Jojo");
    public final static CardSeries TateNoYuusha = Add(32, "TateNoYuusha");
    public final static CardSeries Symphogear = Add(33, "Symphogear");
    public final static CardSeries CallOfCthulhu = Add(34, "CallOfCthulhu");
    public final static CardSeries Chuunibyou = Add(35, "Chuunibyou");
    public final static CardSeries FLCL = Add(36, "FLCL");
    public final static CardSeries KillLaKill = Add(37, "KillLaKill");
    public final static CardSeries TouhouProject = Add(38, "TouhouProject");
    public final static CardSeries WelcomeToNHK = Add(39, "WelcomeToNHK");
    public final static CardSeries TalesOfBerseria = Add(40, "TalesOfBerseria");
    public final static CardSeries Rewrite = Add(41, "Rewrite");
    public final static CardSeries DateALive = Add(42, "DateALive");
    public final static CardSeries AngelBeats = Add(43, "AngelBeats");
    public final static CardSeries RozenMaiden = Add(44, "RozenMaiden");
    public final static CardSeries LogHorizon = Add(45, "LogHorizon");
    public final static CardSeries Vocaloid = Add(46, "Vocaloid");
    public final static CardSeries Atelier = Add(47, "Atelier");
    public final static CardSeries CardcaptorSakura = Add(48, "CardcaptorSakura");
    public final static CardSeries GuiltyCrown = Add(49, "GuiltyCrown");
    public final static CardSeries GakkouGurashi = Add(50, "GakkouGurashi");
    public final static CardSeries GenshinImpact = Add(51, "GenshinImpact");
    public final static CardSeries CowboyBebop = Add(52, "CowboyBebop");
    public final static CardSeries GhostInTheShell = Add(53, "GhostInTheShell");
    public final static CardSeries Clannad = Add(54, "Clannad");
    public final static CardSeries GurrenLagann = Add(55, "GurrenLagann");
    public final static CardSeries Gundam = Add(56, "Gundam");
    public final static CardSeries Evangelion = Add(57, "Evangelion");
    public final static CardSeries Lupin = Add(58, "Lupin");
    public final static CardSeries Kirby = Add(59, "Kirby");
    public final static CardSeries Xenoblade = Add(60, "Xenoblade");
    public final static CardSeries HoukaiGakuen = Add(61, "HoukaiGakuen");
    public final static CardSeries Mushishi = Add(62, "Mushishi");
    public final static CardSeries NatsumeYuujinchou = Add(63, "NatsumeYuujinchou");
    public final static CardSeries Hololive = Add(64, "Hololive");
    public final static CardSeries Berserk = Add(65, "Berserk");
    public final static CardSeries DrStone = Add(66, "DrStone");
    public final static CardSeries Kaiji = Add(67, "Kaiji");
    public final static CardSeries Trigun = Add(68, "Trigun");
    public final static CardSeries PantyStocking = Add(69, "PantyStocking");
    public final static CardSeries Danganronpa = Add(70, "Danganronpa");
    public final static CardSeries PhoenixWright = Add(71, "PhoenixWright");
    public final static CardSeries Magi = Add(72, "Magi");
    public final static CardSeries Monster = Add(73, "Monster");
    public final static CardSeries EurekaSeven = Add(74, "EurekaSeven");
    public final static CardSeries EightySix = Add(75, "EightySix");
    public final static CardSeries Nier = Add(76, "Nier");
    public final static CardSeries Megaman = Add(77, "Megaman");
    public final static CardSeries HaruhiSuzumiya = Add(78, "HaruhiSuzumiya");
    public final static CardSeries Hyouka = Add(79, "Hyouka");
    public final static CardSeries Aria = Add(80, "Aria");
    public final static CardSeries LaidBackCamp = Add(81, "LaidBackCamp");


    private static AbstractCard currentSynergy = null;
    private static PCLCard lastCardPlayed = null;

    public final int ID;
    public final String Name;
    public String LocalizedName;

    public CardSeries(int id, String name)
    {
        ID = id;
        Name = name;
        LocalizedName = name;
    }

    public boolean Equals(CardSeries other)
    {
        return other != null && ID == other.ID;
    }

    public static void InitializeStrings()
    {
        for (Integer k : mapIDs.keySet())
        {
            CardSeries s = mapIDs.get(k);
            s.LocalizedName = GR.PCL.Strings.Series.SeriesName(k);
        }
    }

    public static void AddCards(CardSeries series, ArrayList<AbstractCard> source, ArrayList<PCLCard> destination)
    {
        for (AbstractCard c : source)
        {
            PCLCard card = PCLJUtils.SafeCast(c, PCLCard.class);
            if (card != null && (series == null || series.Equals(card.series) || series.Equals(CardSeries.ANY) || (series.Equals(COLORLESS) && card.color.equals(AbstractCard.CardColor.COLORLESS))))
            {
                destination.add(card);
            }
        }
    }

    private static CardSeries Add(int id, String name)
    {
        final CardSeries s = new CardSeries(id, name);
        if (id > 0)
        {
            mapIDs.put(id, s);
            mapNames.put(name, s);
        }

        return s;
    }

    public static Collection<CardSeries> GetAllSeries()
    {
        return mapIDs.values();
    }

    public static CardSeries GetByID(int id)
    {
        return mapIDs.get(id);
    }

    public static CardSeries GetByName(String name, boolean localized)
    {
        if (localized)
        {
            for (CardSeries s : mapIDs.values())
            {
                if (s.LocalizedName.equals(name))
                {
                    return s;
                }
            }

            return null;
        }

        return mapNames.get(name);
    }

    public static PCLCard GetLastCardPlayed()
    {
        return lastCardPlayed;
    }

    public static Map<CardSeries, List<AbstractCard>> GetCardsBySynergy(ArrayList<AbstractCard> cards)
    {
        return PCLJUtils.Group(cards, card ->
        {
            final PCLCard c = PCLJUtils.SafeCast(card, PCLCard.class);
            if (c != null && c.series != null)
            {
                return c.series;
            }

            return ANY;
        });
    }

    public static ArrayList<PCLCard> GetColorlessCards()
    {
        final ArrayList<PCLCard> result = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.srcColorlessCardPool.group)
        {
            if (c instanceof PCLCard)
            {
                result.add((PCLCard) c);
            }
        }

        return result;
    }

    public static String GetLocalizedSeriesString()
    {
        return GR.PCL.Strings.Series.Series;
    }

    public static ArrayList<PCLCard> GetNonColorlessCard()
    {
        final ArrayList<PCLCard> result = new ArrayList<>();
        AddCards(null, AbstractDungeon.srcCommonCardPool.group, result);
        AddCards(null, AbstractDungeon.srcUncommonCardPool.group, result);
        AddCards(null, AbstractDungeon.srcRareCardPool.group, result);

        return result;
    }

    public static ArrayList<PCLCard> GetNonColorlessCard(CardSeries series)
    {
        final ArrayList<PCLCard> result = new ArrayList<>();
        AddCards(series, AbstractDungeon.srcCommonCardPool.group, result);
        AddCards(series, AbstractDungeon.srcUncommonCardPool.group, result);
        AddCards(series, AbstractDungeon.srcRareCardPool.group, result);

        return result;
    }

    public static HashSet<CardSeries> GetAllSeries(ArrayList<AbstractCard> cards)
    {
        final HashSet<CardSeries> result = new HashSet<>();
        for (AbstractCard card : cards)
        {
            PCLCard c = PCLJUtils.SafeCast(card, PCLCard.class);
            if (c != null && c.series != null)
            {
                result.add(c.series);
            }
        }

        return result;
    }
}