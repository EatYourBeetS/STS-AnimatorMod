package eatyourbeets.resources.animator.misc;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.relics.animator.LivingPicture;
import eatyourbeets.relics.animator.TheMissingPiece;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;
import java.util.StringJoiner;

public abstract class AnimatorLoadout
{
    protected ArrayList<String> startingDeck = new ArrayList<>();
    protected String shortDescription = null;

    public int ID;
    public String Name;
    public Synergy Synergy;
    public boolean IsBeta;

    public int StartingGold = 99;
    public int MaxHP = 62;
    public int CardDraw = 5;
    public int OrbSlots = 3;
    public int UnlockLevel = 0;

    public AnimatorLoadout(String name)
    {
        this.IsBeta = true;
        this.Synergy = null;
        this.Name = name;
        this.ID = -1;
    }

    public AnimatorLoadout(Synergy synergy)
    {
        this.IsBeta = false;
        this.Synergy = synergy;
        this.Name = synergy.Name;
        this.ID = synergy.ID;
    }

    public abstract ArrayList<String> GetStartingDeck();
    public abstract EYBCardData GetSymbolicCard();
    public abstract EYBCardData GetUltraRare();

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
        if (shortDescription == null)
        {
            StringJoiner sj = new StringJoiner(", ");
            for (String s : GetStartingDeck())
            {
                if (!s.contains(Strike.DATA.ID) && !s.contains(Defend.DATA.ID))
                {
                    sj.add(CardLibrary.getCard(s).originalName);
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