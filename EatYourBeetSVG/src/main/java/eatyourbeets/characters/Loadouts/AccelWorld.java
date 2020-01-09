package eatyourbeets.characters.Loadouts;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_AccelWorld;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_AccelWorld;
import eatyourbeets.cards.animator.colorless.uncommon.Kuroyukihime;
import eatyourbeets.cards.animator.colorless.uncommon.LimeBell;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.characters.AnimatorCustomLoadout;
import eatyourbeets.characters.AnimatorMetrics;

import java.util.ArrayList;

public class AccelWorld extends AnimatorCustomLoadout
{
    public AccelWorld()
    {
        Synergy s = Synergies.AccelWorld;

        //this.MaxHP = 71;
        this.ID = s.ID;
        this.Name = s.Name;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        ArrayList<String> res = new ArrayList<>();
        res.add(Strike_AccelWorld.ID);
        res.add(Defend_AccelWorld.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Kuroyukihime.ID);
        res.add(LimeBell.ID);

        return res;
    }

    @Override
    protected String GetTrophyMessage(int trophy)
    {
        if (trophy == 1)
        {
            return trophyStrings.BronzeDescription;
        }
        else if (trophy == 2)
        {
            return trophyStrings.SilverAccelWorld;
        }
        else if (trophy == 3)
        {
            return trophyStrings.GoldAccelWorld;
        }

        return null;
    }

    public void OnVictory(AnimatorCustomLoadout currentLoadout, int ascensionLevel)
    {
        if (trophies == null)
        {
            trophies = GetTrophies(false);
        }

        if (AnimatorMetrics.lastLoadout == ID)
        {
            trophies.trophy1 = Math.max(trophies.trophy1, ascensionLevel);
        }

        ArrayList<String> cardIDs = new ArrayList<>();
        int uniqueCards = 0;

        ArrayList<AbstractCard> deck = AbstractDungeon.player.masterDeck.group;
        for (AbstractCard c : deck)
        {
            if (!cardIDs.contains(c.cardID))
            {
                uniqueCards += 1;
                cardIDs.add(c.cardID);
            }
            else
            {
                return;
            }
        }

        if (uniqueCards >= 20)
        {
            trophies.trophy2 = Math.max(trophies.trophy2, ascensionLevel);
        }

        if (uniqueCards >= 30)
        {
            trophies.trophy3 = Math.max(trophies.trophy3, ascensionLevel);
        }
    }
}