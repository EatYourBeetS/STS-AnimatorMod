package eatyourbeets.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import eatyourbeets.AnimatorResources;
import eatyourbeets.Utilities;

import java.util.ArrayList;
import java.util.HashMap;

public class Synergies
{
    private final static UIStrings uiStrings = AnimatorResources.GetUIStrings(AnimatorResources.UIStringType.Synergies);
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

    private static Synergy CreateSynergy(int id)
    {
        return new Synergy(id, uiStrings.TEXT[id]);
    }

    public static Synergy GetByID(int id)
    {
        return All.get(id);
    }

    public static int Count()
    {
        return All.size() - 1;
    }

    public static ArrayList<AnimatorCard> GetCardsWithSynergy(Synergy synergy)
    {
        ArrayList<AnimatorCard> result = new ArrayList<>();
        AddCardsWithSynergy(synergy, AbstractDungeon.srcCommonCardPool.group, result);
        AddCardsWithSynergy(synergy, AbstractDungeon.srcUncommonCardPool.group, result);
        AddCardsWithSynergy(synergy, AbstractDungeon.srcRareCardPool.group, result);

        return result;
    }

    public static void AddCardsWithSynergy(Synergy synergy, ArrayList<AbstractCard> source, ArrayList<AnimatorCard> destination)
    {
        for (AbstractCard c : source)
        {
            AnimatorCard card = Utilities.SafeCast(c, AnimatorCard.class);
            if (card != null && synergy.Equals(card.GetSynergy()))
            {
                destination.add(card);
            }
        }
    }

    static
    {
        All.put(Elsword.ID, Elsword);
        All.put(Kancolle.ID, Kancolle);
        All.put(Chaika.ID, Chaika);
        All.put(Konosuba.ID, Konosuba);
        All.put(Katanagatari.ID, Katanagatari);
        All.put(OwariNoSeraph.ID, OwariNoSeraph);
        All.put(Overlord.ID, Overlord);
        All.put(NoGameNoLife.ID, NoGameNoLife);
        All.put(Gate.ID, Gate);
        All.put(Fate.ID, Fate);
        All.put(GoblinSlayer.ID, GoblinSlayer);
        All.put(FullmetalAlchemist.ID, FullmetalAlchemist);
        All.put(HatarakuMaouSama.ID, HatarakuMaouSama);
    }
}