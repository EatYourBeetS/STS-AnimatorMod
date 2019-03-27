package eatyourbeets.characters.Loadouts;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.cards.Synergy;
import eatyourbeets.cards.animator.Kuroyukihime;
import eatyourbeets.cards.animator.Shimakaze;
import eatyourbeets.characters.AnimatorCustomLoadout;
import eatyourbeets.characters.AnimatorMetrics;
import eatyourbeets.relics.PurgingStone;
import patches.AbstractEnums;

import java.util.ArrayList;

public class AccelWorld extends AnimatorCustomLoadout
{
    public AccelWorld()
    {
        Synergy s = Synergies.AccelWorld;

        this.ID = s.ID;
        this.Name = s.NAME;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        ArrayList<String> res = super.GetStartingDeck();
        res.add(Kuroyukihime.ID);

        return res;
    }

    @Override
    protected String GetTrophyMessage(int trophy)
    {
        if (trophy == 1)
        {
            return trophyStrings[3];
        }
        else if (trophy == 2)
        {
            return trophyStrings[8];
        }
        else if (trophy == 3)
        {
            return trophyStrings[9];
        }

        return null;
    }

    public void OnTrueVictory(AnimatorCustomLoadout currentLoadout, int ascensionLevel)
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