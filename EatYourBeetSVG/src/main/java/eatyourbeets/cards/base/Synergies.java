package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.interfaces.subscribers.OnSynergyCheckSubscriber;
import eatyourbeets.powers.CombatStats;
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
    public final static Synergy RozenMaiden = CreateSynergy(44);
    public final static Synergy LogHorizon = CreateSynergy(45);
    public final static Synergy Vocaloid = CreateSynergy(46);
    public final static Synergy Atelier = CreateSynergy(47);
    public final static Synergy CardcaptorSakura = CreateSynergy(48);
    public final static Synergy GuiltyCrown = CreateSynergy(49);
    public final static Synergy Gakkougurashi = CreateSynergy(50);

    private static AbstractCard currentSynergy = null;
    private static AnimatorCard lastCardPlayed = null;

    public static boolean IsSynergizing(AbstractCard card)
    {
        if (card == null || currentSynergy == null)
        {
            return false;
        }

        return currentSynergy.uuid == card.uuid;
    }

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

    public static void TrySynergize(AbstractCard card)
    {
        if (WouldSynergize(card))
        {
            currentSynergy = card;
            CombatStats.Instance.OnSynergy(card);
        }
        else
        {
            currentSynergy = null;
        }
    }

    public static void SetLastCardPlayed(AbstractCard card)
    {
        lastCardPlayed = JUtils.SafeCast(card, AnimatorCard.class);
        currentSynergy = null;
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

        AnimatorCard a = JUtils.SafeCast(card, AnimatorCard.class);
        AnimatorCard b = JUtils.SafeCast(other, AnimatorCard.class);
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
        return ((a.hasTag(AnimatorCard.SHAPESHIFTER) || b.hasTag(AnimatorCard.SHAPESHIFTER))
            || (a.hasTag(AnimatorCard.MARTIAL_ARTIST) && b.hasTag(AnimatorCard.MARTIAL_ARTIST))
            || (a.hasTag(AnimatorCard.SPELLCASTER) && b.hasTag(AnimatorCard.SPELLCASTER)));
    }
}
