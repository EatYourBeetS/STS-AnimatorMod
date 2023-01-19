package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.interfaces.subscribers.OnSynergyCheckSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

import java.util.*;

public class CardSeries
{
    private final static HashMap<Integer, CardSeries> mapIDs = new HashMap<>();
    private final static HashMap<String, CardSeries> mapNames = new HashMap<>();

    public final static CardSeries ANY = Add(0, "ANY");
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
    public final static CardSeries Durarara = Add(52, "Durarara");
    public final static CardSeries CowboyBebop = Add(53, "CowboyBebop");
    public final static CardSeries GhostInTheShell = Add(54, "GhostInTheShell");
    public final static CardSeries GurrenLagann = Add(55, "GurrenLagann");
    public final static CardSeries Gundam = Add(56, "Gundam");
    public final static CardSeries Evangelion = Add(57, "Evangelion");
    public final static CardSeries Lupin = Add(58, "Lupin");
    public final static CardSeries DrStone = Add(59, "DrStone");

    private static AbstractCard currentSynergy = null;
    private static EYBCard lastCardPlayed = null;

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
            s.LocalizedName = GR.Animator.Strings.Series.SeriesName(k);
        }
    }

    public static CardSeries Add(int id, String name)
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

    public static void SetLastCardPlayed(AbstractCard card)
    {
        lastCardPlayed = JUtils.SafeCast(card, EYBCard.class);
        currentSynergy = null;
    }

    public static EYBCard GetLastCardPlayed()
    {
        return lastCardPlayed;
    }

    public static String GetLocalizedSeriesString()
    {
        return GR.Animator.Strings.Series.Series;
    }

    public static class Affinity
    {
        public static Map<CardSeries, List<AbstractCard>> GetCardsBySeries(ArrayList<AbstractCard> cards)
        {
            return JUtils.Group(cards, card ->
            {
                final AnimatorCard c = JUtils.SafeCast(card, AnimatorCard.class);
                if (c != null && c.series != null)
                {
                    return c.series;
                }

                return ANY;
            });
        }

        public static ArrayList<AnimatorCard> GetColorlessCards()
        {
            final ArrayList<AnimatorCard> result = new ArrayList<>();
            for (AbstractCard c : AbstractDungeon.srcColorlessCardPool.group)
            {
                if (c instanceof AnimatorCard)
                {
                    result.add((AnimatorCard) c);
                }
            }

            return result;
        }

        public static ArrayList<AnimatorCard> GetNonColorlessCard()
        {
            final ArrayList<AnimatorCard> result = new ArrayList<>();
            AddCards(null, AbstractDungeon.srcCommonCardPool.group, result);
            AddCards(null, AbstractDungeon.srcUncommonCardPool.group, result);
            AddCards(null, AbstractDungeon.srcRareCardPool.group, result);

            return result;
        }

        public static ArrayList<AnimatorCard> GetNonColorlessCard(CardSeries series)
        {
            final ArrayList<AnimatorCard> result = new ArrayList<>();
            AddCards(series, AbstractDungeon.srcCommonCardPool.group, result);
            AddCards(series, AbstractDungeon.srcUncommonCardPool.group, result);
            AddCards(series, AbstractDungeon.srcRareCardPool.group, result);

            return result;
        }

        public static void AddCards(CardSeries series, ArrayList<AbstractCard> source, ArrayList<AnimatorCard> destination)
        {
            for (AbstractCard c : source)
            {
                AnimatorCard card = JUtils.SafeCast(c, AnimatorCard.class);
                if (card != null && (series == null || series.Equals(card.series) || series.Equals(CardSeries.ANY)))
                {
                    destination.add(card);
                }
            }
        }

        public static HashSet<CardSeries> GetAllSeries(ArrayList<AbstractCard> cards)
        {
            final HashSet<CardSeries> result = new HashSet<>();
            for (AbstractCard card : cards)
            {
                AnimatorCard c = JUtils.SafeCast(card, AnimatorCard.class);
                if (c != null && c.series != null)
                {
                    result.add(c.series);
                }
            }

            return result;
        }
    }

    public static class Synergy
    {
        public static boolean IsSynergizing(AbstractCard card)
        {
            if (card == null || currentSynergy == null)
            {
                return false;
            }

            return currentSynergy.uuid == card.uuid;
        }

        public static Map<CardSeries, List<AbstractCard>> GetCardsBySynergy(ArrayList<AbstractCard> cards)
        {
            return JUtils.Group(cards, card ->
            {
                final AnimatorClassicCard c = JUtils.SafeCast(card, AnimatorClassicCard.class);
                if (c != null && c.series != null)
                {
                    return c.series;
                }

                return ANY;
            });
        }

        public static ArrayList<AnimatorClassicCard> GetColorlessCards()
        {
            final ArrayList<AnimatorClassicCard> result = new ArrayList<>();
            for (AbstractCard c : AbstractDungeon.srcColorlessCardPool.group)
            {
                if (c instanceof AnimatorClassicCard)
                {
                    result.add((AnimatorClassicCard) c);
                }
            }

            return result;
        }

        public static ArrayList<AnimatorClassicCard> GetNonColorlessCard()
        {
            final ArrayList<AnimatorClassicCard> result = new ArrayList<>();
            AddCards(null, AbstractDungeon.srcCommonCardPool.group, result);
            AddCards(null, AbstractDungeon.srcUncommonCardPool.group, result);
            AddCards(null, AbstractDungeon.srcRareCardPool.group, result);

            return result;
        }

        public static ArrayList<AnimatorClassicCard> GetNonColorlessCard(CardSeries series)
        {
            final ArrayList<AnimatorClassicCard> result = new ArrayList<>();
            AddCards(series, AbstractDungeon.srcCommonCardPool.group, result);
            AddCards(series, AbstractDungeon.srcUncommonCardPool.group, result);
            AddCards(series, AbstractDungeon.srcRareCardPool.group, result);

            return result;
        }

        public static void AddCards(CardSeries series, ArrayList<AbstractCard> source, ArrayList<AnimatorClassicCard> destination)
        {
            for (AbstractCard c : source)
            {
                AnimatorClassicCard card = JUtils.SafeCast(c, AnimatorClassicCard.class);
                if (card != null && (series == null || series.Equals(card.series) || series.Equals(CardSeries.ANY)))
                {
                    destination.add(card);
                }
            }
        }

        public static HashSet<CardSeries> GetAllSeries(ArrayList<AbstractCard> cards)
        {
            final HashSet<CardSeries> result = new HashSet<>();
            for (AbstractCard card : cards)
            {
                AnimatorClassicCard c = JUtils.SafeCast(card, AnimatorClassicCard.class);
                if (c != null && c.series != null)
                {
                    result.add(c.series);
                }
            }

            return result;
        }

        public static boolean TrySynergize(AbstractCard card)
        {
            if (WouldSynergize(card))
            {
                currentSynergy = card;
                return true;
            }

            currentSynergy = null;
            return false;
        }

        public static boolean WouldSynergize(AbstractCard card)
        {
            return WouldSynergize(card, lastCardPlayed);
        }

        public static boolean WouldSynergize(AbstractCard card, AbstractCard other)
        {
            for (OnSynergyCheckSubscriber s : CombatStats.onSynergyCheck.GetSubscribers())
            {
                if (s.OnSynergyCheck(card, other))
                {
                    return true;
                }
            }

            if (card == null || other == null)
            {
                return false;
            }

            final AnimatorClassicCard a = JUtils.SafeCast(card, AnimatorClassicCard.class);
            final AnimatorClassicCard b = JUtils.SafeCast(other, AnimatorClassicCard.class);

            if (a != null)
            {
                if (b != null)
                {
                    return a.HasDirectSynergy(b) || b.HasDirectSynergy(a);
                }
                else
                {
                    return a.HasDirectSynergy(other);
                }
            }

            if (b != null)
            {
                return b.HasDirectSynergy(card);
            }

            return HasTagSynergy(card, other);
        }

        public static boolean HasTagSynergy(AbstractCard a, AbstractCard b)
        {
            return ((a.hasTag(AnimatorClassicCard.SHAPESHIFTER) || b.hasTag(AnimatorClassicCard.SHAPESHIFTER))
                    || (a.hasTag(AnimatorClassicCard.MARTIAL_ARTIST) && b.hasTag(AnimatorClassicCard.MARTIAL_ARTIST))
                    || (a.hasTag(AnimatorClassicCard.SPELLCASTER) && b.hasTag(AnimatorClassicCard.SPELLCASTER)));
        }
    }
}