package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

import java.util.*;

public class Synergies
{
    private final static HashMap<Integer, Synergy> All = new HashMap<>();
    public final static Synergy ANY = CreateSynergy(0);
    public final static Synergy Elsword = CreateSynergy(1);
    public final static Synergy Kancolle = CreateSynergy(2);
    public final static Synergy Chaika = CreateSynergy(3);
    public final static Synergy Konosuba = CreateSynergy(4);
    public final static Synergy Katanagatari = CreateSynergy(5);
    public final static Synergy OwariNoSeraph = CreateSynergy(6);
    public final static Synergy Overlord = CreateSynergy(7);
    public final static Synergy NoGameNoLife = CreateSynergy(8);
    public final static Synergy Gate = CreateSynergy(9);
    public final static Synergy Fate = CreateSynergy(10);
    public final static Synergy GoblinSlayer = CreateSynergy(11);
    public final static Synergy FullmetalAlchemist = CreateSynergy(12);
    public final static Synergy HatarakuMaouSama = CreateSynergy(13);
    public final static Synergy GrimoireOfZero = CreateSynergy(14);
    public final static Synergy SteinsGate = CreateSynergy(15);
    public final static Synergy TenSura = CreateSynergy(16);
    public final static Synergy ReZero = CreateSynergy(17);
    public final static Synergy MadokaMagica = CreateSynergy(18);
    public final static Synergy Charlotte = CreateSynergy(19);
    public final static Synergy AccelWorld = CreateSynergy(20);
    public final static Synergy Noragami = CreateSynergy(21);
    public final static Synergy OnePunchMan = CreateSynergy(22);
    public final static Synergy PandoraHearts = CreateSynergy(23);
    public final static Synergy ZeroNoTsukaima = CreateSynergy(24);
    public final static Synergy GabrielDropOut = CreateSynergy(25);
    public final static Synergy DeathNote = CreateSynergy(26);
    public final static Synergy KamisamaNoMemochu = CreateSynergy(27);
    public final static Synergy CodeGeass = CreateSynergy(28);
    public final static Synergy YoujoSenki = CreateSynergy(29);
    public final static Synergy Bleach = CreateSynergy(30);
    public final static Synergy Jojo = CreateSynergy(31);
    public final static Synergy TateNoYuusha = CreateSynergy(32);
    public final static Synergy Symphogear = CreateSynergy(33);
    public final static Synergy CallOfCthulhu = CreateSynergy(34);
    public final static Synergy Chuunibyou = CreateSynergy(35);
    public final static Synergy FLCL = CreateSynergy(36);
    public final static Synergy KillLaKill = CreateSynergy(37);
    public final static Synergy TouhouProject = CreateSynergy(38);
    public final static Synergy WelcomeToNHK = CreateSynergy(39);
    public final static Synergy TalesOfBerseria = CreateSynergy(40);
    public final static Synergy Rewrite = CreateSynergy(41);
    public final static Synergy DateALive = CreateSynergy(42);
    public final static Synergy AngelBeats = CreateSynergy(43);

    @SuppressWarnings("FieldCanBeLocal")
    private static AnimatorCard previousCard = null;
    private static AnimatorCard lastCardPlayed = null;
    private static int preemptiveSynergies;

    public static void AddCards(Synergy synergy, ArrayList<AbstractCard> source, ArrayList<AnimatorCard> destination)
    {
        for (AbstractCard c : source)
        {
            AnimatorCard card = JUtils.SafeCast(c, AnimatorCard.class);
            if (card != null && (synergy == null || synergy.Equals(card.synergy) || synergy.Equals(Synergies.ANY)))
            {
                destination.add(card);
            }
        }
    }

    public static int AddPreemptiveSynergies(int amount)
    {
        return preemptiveSynergies += amount;
    }

    private static Synergy CreateSynergy(int id)
    {
        Synergy s = new Synergy(id, GR.Animator.Strings.Synergies.SynergyName(id));
        if (id > 0)
        {
            All.put(id, s);
        }

        return s;
    }

    public static Collection<Synergy> GetAll()
    {
        return All.values();
    }

    public static Synergy GetByID(int id)
    {
        return All.get(id);
    }

    public static Map<Synergy, List<AbstractCard>> GetCardsBySynergy(ArrayList<AbstractCard> cards)
    {
        Map<Synergy, List<AbstractCard>> map = new HashMap<>();
        for (AbstractCard card : cards)
        {
            Synergy key = ANY;
            AnimatorCard c = JUtils.SafeCast(card, AnimatorCard.class);
            if (c != null && c.synergy != null)
            {
                key = c.synergy;
            }

            map.computeIfAbsent(key, k -> new ArrayList<>()).add(card);
        }

        return map;
    }

    public static ArrayList<AnimatorCard> GetColorlessCards()
    {
        ArrayList<AnimatorCard> result = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.srcColorlessCardPool.group)
        {
            if (c instanceof AnimatorCard)
            {
                result.add((AnimatorCard) c);
            }
        }

        return result;
    }

    public static String GetLocalizedSeriesString()
    {
        return GR.Animator.Strings.Synergies.Series;
    }

    public static ArrayList<AnimatorCard> GetNonColorlessCard()
    {
        ArrayList<AnimatorCard> result = new ArrayList<>();
        AddCards(null, AbstractDungeon.srcCommonCardPool.group, result);
        AddCards(null, AbstractDungeon.srcUncommonCardPool.group, result);
        AddCards(null, AbstractDungeon.srcRareCardPool.group, result);

        return result;
    }

    public static ArrayList<AnimatorCard> GetNonColorlessCard(Synergy synergy)
    {
        ArrayList<AnimatorCard> result = new ArrayList<>();
        AddCards(synergy, AbstractDungeon.srcCommonCardPool.group, result);
        AddCards(synergy, AbstractDungeon.srcUncommonCardPool.group, result);
        AddCards(synergy, AbstractDungeon.srcRareCardPool.group, result);

        return result;
    }

    public static HashSet<Synergy> GetSynergies(ArrayList<AbstractCard> cards)
    {
        HashSet<Synergy> result = new HashSet<>();
        for (AbstractCard card : cards)
        {
            AnimatorCard c = JUtils.SafeCast(card, AnimatorCard.class);
            if (c != null && c.synergy != null)
            {
                result.add(c.synergy);
            }
        }

        return result;
    }

    public static void SetLastCardPlayed(AbstractCard card)
    {
        if (card == null)
        {
            previousCard = null;
            lastCardPlayed = null;
            preemptiveSynergies = 0;
        }
        else
        {
            if (preemptiveSynergies > 0)
            {
                preemptiveSynergies -= 1;
            }

            previousCard = lastCardPlayed;
            lastCardPlayed = JUtils.SafeCast(card, AnimatorCard.class);
        }
    }

    public static boolean WouldSynergize(AnimatorCard card)
    {
        return WouldSynergize(card, lastCardPlayed);
    }

    public static boolean WouldSynergize(AnimatorCard card, AbstractCard abstractCard)
    {
        return WouldSynergize(card, JUtils.SafeCast(abstractCard, AnimatorCard.class));
    }

    public static boolean WouldSynergize(AnimatorCard card, AnimatorCard other)
    {
        if (preemptiveSynergies > 0)
        {
            return true;
        }
        else if (other != null && other.synergy != null && card.synergy != null)
        {
            return (card.synergy.equals(other.synergy)
            || (card.hasTag(AnimatorCard.SHAPESHIFTER) || other.hasTag(AnimatorCard.SHAPESHIFTER))
            || (card.hasTag(AnimatorCard.MARTIAL_ARTIST) && other.hasTag(AnimatorCard.MARTIAL_ARTIST))
            || (card.hasTag(AnimatorCard.SPELLCASTER) && other.hasTag(AnimatorCard.SPELLCASTER)));
        }

        return false;
    }
}