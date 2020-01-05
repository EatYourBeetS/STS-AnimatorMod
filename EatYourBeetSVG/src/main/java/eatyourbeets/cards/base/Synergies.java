package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.resources.AnimatorResources_Strings;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;
import java.util.HashMap;

public class Synergies
{
    private final static UIStrings uiStrings = AnimatorResources_Strings.Synergies;
    private final static HashMap<Integer, Synergy> All = new HashMap<>();

    @SuppressWarnings("FieldCanBeLocal")
    private static AnimatorCard previousCard = null;
    private static AnimatorCard lastCardPlayed = null;
    private static int preemptiveSynergies;

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

    private static Synergy CreateSynergy(int id)
    {
        Synergy s = new Synergy(id, uiStrings.TEXT[id]);
        if (id > 0)
        {
            All.put(id, s);
        }

        return s;
    }

    public static String GetSeriesString()
    {
        return uiStrings.EXTRA_TEXT[0];
    }

    public static Synergy GetByID(int id)
    {
        return All.get(id);
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

    public static ArrayList<AnimatorCard> GetAnimatorCards()
    {
        ArrayList<AnimatorCard> result = new ArrayList<>();
        AddAnimatorCards(AbstractDungeon.srcCommonCardPool.group, result);
        AddAnimatorCards(AbstractDungeon.srcUncommonCardPool.group, result);
        AddAnimatorCards(AbstractDungeon.srcRareCardPool.group, result);

        return result;
    }

    public static ArrayList<AnimatorCard> GetCardsWithSynergy(Synergy synergy)
    {
        ArrayList<AnimatorCard> result = new ArrayList<>();
        AddCardsWithSynergy(synergy, AbstractDungeon.srcCommonCardPool.group, result);
        AddCardsWithSynergy(synergy, AbstractDungeon.srcUncommonCardPool.group, result);
        AddCardsWithSynergy(synergy, AbstractDungeon.srcRareCardPool.group, result);

        return result;
    }

    public static void AddAnimatorCards(ArrayList<AbstractCard> source, ArrayList<AnimatorCard> destination)
    {
        for (AbstractCard c : source)
        {
            AnimatorCard card = JavaUtilities.SafeCast(c, AnimatorCard.class);
            if (card != null)
            {
                destination.add(card);
            }
        }
    }

    public static void AddCardsWithSynergy(Synergy synergy, ArrayList<AbstractCard> source, ArrayList<AnimatorCard> destination)
    {
        for (AbstractCard c : source)
        {
            AnimatorCard card = JavaUtilities.SafeCast(c, AnimatorCard.class);
            if (card != null && (synergy.Equals(card.synergy) || synergy.Equals(Synergies.ANY)))
            {
                destination.add(card);
            }
        }
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
            lastCardPlayed = JavaUtilities.SafeCast(card, AnimatorCard.class);
        }
    }

    public static boolean WouldSynergize(AnimatorCard card)
    {
        return WouldSynergize(card, lastCardPlayed);
    }

    public static boolean WouldSynergize(AnimatorCard card, AbstractCard abstractCard)
    {
        return WouldSynergize(card, JavaUtilities.SafeCast(abstractCard, AnimatorCard.class));
    }

    public static boolean WouldSynergize(AnimatorCard card, AnimatorCard other)
    {
        if (preemptiveSynergies > 0)
        {
            return true;
        }
        else if (other != null && other.synergy != null && card.synergy != null)
        {
            return (card instanceof Spellcaster && other instanceof Spellcaster) ||
                    (card instanceof MartialArtist && other instanceof MartialArtist) ||
                    (card.anySynergy || other.anySynergy) ||
                    (card.synergy.equals(other.synergy));
        }

        return false;
    }

    public static int AddPreemptiveSynergies(int amount)
    {
        return preemptiveSynergies += amount;
    }
}