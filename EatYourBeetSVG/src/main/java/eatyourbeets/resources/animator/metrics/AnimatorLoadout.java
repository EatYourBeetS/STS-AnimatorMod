package eatyourbeets.resources.animator.metrics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.relics.animator.LivingPicture;
import eatyourbeets.relics.animator.PurgingStone_Cards;
import eatyourbeets.relics.animator.PurgingStone_Series;
import eatyourbeets.relics.animator.TheMissingPiece;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public abstract class AnimatorLoadout
{
    protected final ArrayList<String> startingDeck = new ArrayList<>();
    protected final Map<String, AbstractCard> libraryCards = new HashMap<>();
    protected AnimatorCard_UltraRare ultraRare = null;
    protected String shortDescription = null;

    public int ID;
    public String Name;
    public Synergy synergy;

    public int StartingGold = 99;
    public int MaxHP = 71;
    public int CardDraw = 5;
    public int OrbSlots = 3;
    public int UnlockLevel = 0;

    public AnimatorLoadout(String name, int id)
    {
        this.synergy = null;
        this.Name = name;
        this.ID = id;
    }

    public AnimatorLoadout(Synergy synergy)
    {
        this.synergy = synergy;
        this.Name = synergy.Name;
        this.ID = synergy.ID;
    }

    public abstract AnimatorCard_UltraRare GetUltraRare();
    public abstract ArrayList<String> GetStartingDeck();
    public abstract String GetRepresentativeCard();

    public CharSelectInfo GetLoadout(String name, String description, AnimatorCharacter animatorCharacter)
    {
        return new CharSelectInfo(name + "-" + ID, description, MaxHP, MaxHP, OrbSlots, StartingGold, CardDraw,
                                        animatorCharacter, GetStartingRelics(), GetStartingDeck(), false);
    }

    public ArrayList<String> GetStartingRelics()
    {
        if (!UnlockTracker.isRelicSeen(LivingPicture.ID))
        {
            UnlockTracker.markRelicAsSeen(LivingPicture.ID);
        }
        if (!UnlockTracker.isRelicSeen(PurgingStone_Cards.ID))
        {
            UnlockTracker.markRelicAsSeen(PurgingStone_Cards.ID);
        }
        if (!UnlockTracker.isRelicSeen(PurgingStone_Series.ID))
        {
            UnlockTracker.markRelicAsSeen(PurgingStone_Series.ID);
        }
        if (!UnlockTracker.isRelicSeen(TheMissingPiece.ID))
        {
            UnlockTracker.markRelicAsSeen(TheMissingPiece.ID);
        }

        ArrayList<String> res = new ArrayList<>();
        res.add(LivingPicture.ID);
        res.add(PurgingStone_Cards.ID);
        res.add(TheMissingPiece.ID);

        return res;
    }

    public Map<String, AbstractCard> GetAllCards()
    {
        if (libraryCards.isEmpty())
        {
            for (AbstractCard card : Synergies.GetCardsWithSynergy(synergy))
            {
                libraryCards.put(card.cardID, card);
            }
        }

        return libraryCards;
    }

    public AnimatorTrophies GetTrophies()
    {
        AnimatorTrophies trophies = GR.Animator.Metrics.GetTrophies(ID);
        if (trophies == null)
        {
            trophies = new AnimatorTrophies(ID);
            GR.Animator.Metrics.Trophies.add(trophies);
        }

        return trophies;
    }

    public String GetShortDescription()
    {
        if (shortDescription == null)
        {
            StringJoiner sj = new StringJoiner(", ");
            for (String s : GetStartingDeck())
            {
                if (!s.contains(Strike.ID) && !s.contains(Defend.ID))
                {
                    sj.add(CardLibrary.getCardNameFromKey(s));
                }
            }

            shortDescription = sj.toString();
        }

        return shortDescription;
    }

    public String GetTrophyMessage(int trophy)
    {
        if (trophy == 1)
        {
            return GR.Animator.Text.Trophies.BronzeDescription;
        }
        else if (trophy == 2)
        {
            return GR.Animator.Text.Trophies.SilverDescription;
        }
        else if (trophy == 3)
        {
            return GR.Animator.Text.Trophies.GoldDescription;
        }

        return null;
    }

    public void OnVictory(AnimatorLoadout currentLoadout, int ascensionLevel)
    {
        AnimatorTrophies trophies = GetTrophies();

        if (GR.Animator.Metrics.SelectedLoadout.ID == ID)
        {
            trophies.Trophy1 = Math.max(trophies.Trophy1, ascensionLevel);
        }

        ArrayList<String> cardsWithSynergy = new ArrayList<>();
        int synergyCount = 0;
        int uniqueCards = 0;

        ArrayList<AbstractCard> cards = AbstractDungeon.player.masterDeck.group;
        for (AbstractCard c : cards)
        {
            AnimatorCard card = JavaUtilities.SafeCast(c, AnimatorCard.class);
            if (card != null)
            {
                Synergy synergy = card.synergy;
                if (synergy != null && synergy.ID == ID)
                {
                    synergyCount += 1;
                    if (!cardsWithSynergy.contains(card.cardID) && card.rarity != AbstractCard.CardRarity.BASIC)
                    {
                        uniqueCards += 1;
                        cardsWithSynergy.add(card.cardID);
                    }
                }
            }
        }

        if (synergyCount >= cards.size() / 2)
        {
            trophies.Trophy2 = Math.max(trophies.Trophy2, ascensionLevel);
        }

        if (uniqueCards >= 8)
        {
            trophies.Trophy3 = Math.max(trophies.Trophy3, ascensionLevel);
        }
    }
}