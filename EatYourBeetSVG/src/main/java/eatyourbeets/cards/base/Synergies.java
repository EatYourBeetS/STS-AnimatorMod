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
    private final static HashMap<Integer, Synergy> mapIDs = new HashMap<>();
    private final static HashMap<String, Synergy> mapNames = new HashMap<>();

    public final static Synergy ANY = Add(0, "ANY");
    public final static Synergy Elsword = Add(1, "Elsword");
    public final static Synergy Kancolle = Add(2, "Kancolle");
    public final static Synergy Chaika = Add(3, "Chaika");
    public final static Synergy Konosuba = Add(4, "Konosuba");
    public final static Synergy Katanagatari = Add(5, "Katanagatari");
    public final static Synergy OwariNoSeraph = Add(6, "OwariNoSeraph");
    public final static Synergy Overlord = Add(7, "Overlord");
    public final static Synergy NoGameNoLife = Add(8, "NoGameNoLife");
    public final static Synergy Gate = Add(9, "GATE");
    public final static Synergy Fate = Add(10, "Fate");
    public final static Synergy GoblinSlayer = Add(11, "GoblinSlayer");
    public final static Synergy FullmetalAlchemist = Add(12, "FullmetalAlchemist");
    public final static Synergy HatarakuMaouSama = Add(13, "HatarakuMaouSama");
    public final static Synergy GrimoireOfZero = Add(14, "GrimoireOfZero");
    public final static Synergy SteinsGate = Add(15, "SteinsGate");
    public final static Synergy TenSura = Add(16, "TenSura");
    public final static Synergy ReZero = Add(17, "ReZero");
    public final static Synergy MadokaMagica = Add(18, "MadokaMagica");
    public final static Synergy Charlotte = Add(19, "Charlotte");
    public final static Synergy AccelWorld = Add(20, "AccelWorld");
    public final static Synergy Noragami = Add(21, "Noragami");
    public final static Synergy OnePunchMan = Add(22, "OnePunchMan");
    public final static Synergy PandoraHearts = Add(23, "PandoraHearts");
    public final static Synergy ZeroNoTsukaima = Add(24, "ZeroNoTsukaima");
    public final static Synergy GabrielDropout = Add(25, "GabrielDropout");
    public final static Synergy DeathNote = Add(26, "DeathNote");
    public final static Synergy KamisamaNoMemochu = Add(27, "KamisamaNoMemochou");
    public final static Synergy CodeGeass = Add(28, "CodeGeass");
    public final static Synergy YoujoSenki = Add(29, "YoujoSenki");
    public final static Synergy Bleach = Add(30, "Bleach");
    public final static Synergy Jojo = Add(31, "Jojo");
    public final static Synergy TateNoYuusha = Add(32, "TateNoYuusha");
    public final static Synergy Symphogear = Add(33, "Symphogear");
    public final static Synergy CallOfCthulhu = Add(34, "CallOfCthulhu");
    public final static Synergy Chuunibyou = Add(35, "Chuunibyou");
    public final static Synergy FLCL = Add(36, "FLCL");
    public final static Synergy KillLaKill = Add(37, "KillLaKill");
    public final static Synergy TouhouProject = Add(38, "TouhouProject");
    public final static Synergy WelcomeToNHK = Add(39, "WelcomeToNHK");
    public final static Synergy TalesOfBerseria = Add(40, "TalesOfBerseria");
    public final static Synergy Rewrite = Add(41, "Rewrite");
    public final static Synergy DateALive = Add(42, "DateALive");
    public final static Synergy AngelBeats = Add(43, "AngelBeats");
    public final static Synergy RozenMaiden = Add(44, "RozenMaiden");
    public final static Synergy LogHorizon = Add(45, "LogHorizon");
    public final static Synergy Vocaloid = Add(46, "Vocaloid");
    public final static Synergy Atelier = Add(47, "Atelier");
    public final static Synergy CardcaptorSakura = Add(48, "CardcaptorSakura");
    public final static Synergy GuiltyCrown = Add(49, "GuiltyCrown");
    public final static Synergy GakkouGurashi = Add(50, "GakkouGurashi");
    public final static Synergy GenshinImpact = Add(51, "GenshinImpact");

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

    private static Synergy Add(int id, String name))
    {
        Synergy s = new Synergy(id, name, GR.Animator.Strings.Synergies.SynergyName(id));
        if (id > 0)
        {
            mapIDs.put(id, s);
            mapNames.put(name, s);
        }

        return s;
    }

    public static Collection<Synergy> GetAllSynergies()
    {
        return mapIDs.values();
    }

    public static Synergy GetByID(int id)
    {
        return mapIDs.get(id);
    }

    public static Synergy GetByName(String name, boolean localized)
    {
        if (localized)
        {
            for (Synergy s : mapIDs.values())
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

    public static Map<Synergy, List<AbstractCard>> GetCardsBySynergy(ArrayList<AbstractCard> cards)
    {
        return JUtils.Group(cards, card ->
        {
            AnimatorCard c = JUtils.SafeCast(card, AnimatorCard.class);
            if (c != null && c.synergy != null)
            {
                return c.synergy;
            }

            return ANY;
        });
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

    public static HashSet<Synergy> GetAllSynergies(ArrayList<AbstractCard> cards)
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