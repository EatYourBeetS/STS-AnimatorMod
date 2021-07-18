package eatyourbeets.resources.animator.misc;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.ImprovedDefend;
import eatyourbeets.cards.animator.basic.ImprovedStrike;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.relics.animator.LivingPicture;
import eatyourbeets.relics.animator.TheMissingPiece;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public abstract class AnimatorLoadout
{
    public CardSlot Slot_Defend = new CardSlot(1, 6);
    public CardSlot Slot_Strike = new CardSlot(1, 6);
    public CardSlot Slot_ImprovedDefend = new CardSlot(1, 1);
    public CardSlot Slot_ImprovedStrike = new CardSlot(1, 1);
    public CardSlot Slot_Series1 = new CardSlot(0, 1);
    public CardSlot Slot_Series2 = new CardSlot(0, 1);

    protected ArrayList<String> startingDeck = new ArrayList<>();
    protected String shortDescription = "";

    public int ID;
    public String Name;
    public CardSeries Series;
    public boolean IsBeta;

    public int StartingGold = 99;
    public int MaxHP = 60;
    public int CardDraw = 5;
    public int OrbSlots = 3;
    public int UnlockLevel = 0;

    public AnimatorLoadout(String name)
    {
        this.IsBeta = true;
        this.Series = null;
        this.Name = name;
        this.ID = -1;
    }

    public AnimatorLoadout(CardSeries series)
    {
        this.IsBeta = false;
        this.Series = series;
        this.Name = series.LocalizedName;
        this.ID = series.ID;
    }

    public void InitializeSlots()
    {
        Slot_Defend.AddItem(Defend.DATA, -2);
        Slot_Strike.AddItem(Strike.DATA, -2);

        for (EYBCardData data : ImprovedStrike.GetCards())
        {
            Slot_ImprovedStrike.AddItem(data, 1);
        }

        for (EYBCardData data : ImprovedDefend.GetCards())
        {
            Slot_ImprovedDefend.AddItem(data, 1);
        }

        Slot_Series1.AddSharedSlot(Slot_Series2);
        Slot_Series2.AddSharedSlot(Slot_Series1);
    }

    public abstract EYBCardData GetSymbolicCard();
    public abstract EYBCardData GetUltraRare();

    protected void AddSeriesItem(EYBCardData data, int estimatedValue)
    {
        Slot_Series1.AddItem(data, estimatedValue);
        Slot_Series2.AddItem(data, estimatedValue);
    }

    public CharSelectInfo GetLoadout(String name, String description, AnimatorCharacter animatorCharacter)
    {
        return new CharSelectInfo(name + "-" + ID, description, MaxHP, MaxHP, OrbSlots, StartingGold, CardDraw,
                                        animatorCharacter, GetStartingRelics(), GetStartingDeck(), false);
    }

    public ArrayList<String> GetStartingDeck()
    {
        ActionT2<ArrayList<String>, CardSlot> add = (list, slot) ->
        {
            EYBCardData data = slot.GetData();
            if (data != null)
            {
                for (int i = 0; i < slot.amount; i++)
                {
                    list.add(data.ID);
                }
            }
        };

        ArrayList<String> cards = new ArrayList<>();
        add.Invoke(cards, Slot_Defend);
        add.Invoke(cards, Slot_Strike);
        add.Invoke(cards, Slot_ImprovedDefend);
        add.Invoke(cards, Slot_ImprovedStrike);
        add.Invoke(cards, Slot_Series1);
        add.Invoke(cards, Slot_Series2);
        return cards;
    }

    public ArrayList<String> GetStartingRelics()
    {
        if (!UnlockTracker.isRelicSeen(LivingPicture.ID))
        {
            UnlockTracker.markRelicAsSeen(LivingPicture.ID);
        }
        if (!UnlockTracker.isRelicSeen(TheMissingPiece.ID))
        {
            UnlockTracker.markRelicAsSeen(TheMissingPiece.ID);
        }

        ArrayList<String> res = new ArrayList<>();
        res.add(LivingPicture.ID);
        res.add(TheMissingPiece.ID);
        return res;
    }

    public AnimatorTrophies GetTrophies()
    {
        if (IsBeta)
        {
            return null;
        }

        AnimatorTrophies trophies = GR.Animator.Data.GetTrophies(ID);
        if (trophies == null)
        {
            trophies = new AnimatorTrophies(ID);
            GR.Animator.Data.Trophies.add(trophies);
        }

        return trophies;
    }

    public String GetDeckPreviewString()
    {
// TODO: Loadout preview string
//
//        if (shortDescription == null)
//        {
//            StringJoiner sj = new StringJoiner(", ");
//            for (String s : GetStartingDeck())
//            {
//                if (!s.contains(Strike.DATA.ID) && !s.contains(Defend.DATA.ID))
//                {
//                    sj.add(CardLibrary.getCard(s).originalName);
//                }
//            }
//
//            shortDescription = sj.toString();
//        }

        return shortDescription;
    }

    public String GetTrophyMessage(int trophy)
    {
        if (trophy == 1)
        {
            return GR.Animator.Strings.Trophies.BronzeDescription;
        }
        else if (trophy == 2)
        {
            return GR.Animator.Strings.Trophies.SilverDescription;
        }
        else if (trophy == 3)
        {
            return GR.Animator.Strings.Trophies.GoldDescription;
        }

        return null;
    }

    public void OnVictory(AnimatorLoadout currentLoadout, int ascensionLevel)
    {
        AnimatorTrophies trophies = GetTrophies();

        if (GR.Animator.Data.SelectedLoadout.ID == ID)
        {
            trophies.Trophy1 = Math.max(trophies.Trophy1, ascensionLevel);
        }

        ArrayList<String> cardsWithSynergy = new ArrayList<>();
        int synergyCount = 0;
        int uniqueCards = 0;

        ArrayList<AbstractCard> cards = AbstractDungeon.player.masterDeck.group;
        for (AbstractCard c : cards)
        {
            AnimatorCard card = JUtils.SafeCast(c, AnimatorCard.class);
            if (card != null)
            {
                CardSeries series = card.series;
                if (series != null && series.ID == ID)
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