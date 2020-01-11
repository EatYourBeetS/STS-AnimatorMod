package eatyourbeets.resources.animator.loadouts;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_AccelWorld;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_AccelWorld;
import eatyourbeets.cards.animator.colorless.uncommon.Kuroyukihime;
import eatyourbeets.cards.animator.colorless.uncommon.LimeBell;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.metrics.AnimatorLoadout;
import eatyourbeets.resources.animator.metrics.AnimatorTrophies;

import java.util.ArrayList;

public class AccelWorld extends AnimatorLoadout
{
    public AccelWorld()
    {
        super(Synergies.AccelWorld);
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_AccelWorld.ID);
            startingDeck.add(Defend_AccelWorld.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Strike.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Defend.ID);
            startingDeck.add(Kuroyukihime.ID);
            startingDeck.add(LimeBell.ID);
        }

        return startingDeck;
    }

    @Override
    public String GetRepresentativeCard()
    {
        return Kuroyukihime.ID;
    }

    @Override
    public AnimatorCard_UltraRare GetUltraRare()
    {
        return null;
    }

    @Override
    public String GetTrophyMessage(int trophy)
    {
        if (trophy == 1)
        {
            return GR.Animator.Text.Trophies.BronzeDescription;
        }
        else if (trophy == 2)
        {
            return GR.Animator.Text.Trophies.SilverAccelWorld;
        }
        else if (trophy == 3)
        {
            return GR.Animator.Text.Trophies.GoldAccelWorld;
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
            trophies.Trophy2 = Math.max(trophies.Trophy2, ascensionLevel);
        }

        if (uniqueCards >= 30)
        {
            trophies.Trophy3 = Math.max(trophies.Trophy3, ascensionLevel);
        }
    }
}