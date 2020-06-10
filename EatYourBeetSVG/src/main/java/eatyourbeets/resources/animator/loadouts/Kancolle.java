package eatyourbeets.resources.animator.loadouts;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Defend_Kancolle;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.basic.Strike_Kancolle;
import eatyourbeets.cards.animator.colorless.uncommon.Shimakaze;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorTrophies;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class Kancolle extends AnimatorLoadout
{
    public Kancolle()
    {
        super(Synergies.Kancolle);

        this.StartingGold = 249;
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike_Kancolle.ID);
            startingDeck.add(Defend_Kancolle.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Shimakaze.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return Shimakaze.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        return null;
    }

    @Override
    public String GetTrophyMessage(int trophy)
    {
        if (trophy == 1)
        {
            return GR.Animator.Strings.Trophies.BronzeDescription;
        }
        else if (trophy == 2)
        {
            return GR.Animator.Strings.Trophies.SilverKancolle;
        }
        else if (trophy == 3)
        {
            return GR.Animator.Strings.Trophies.GoldKancolle;
        }

        return null;
    }

    public void OnVictory(AnimatorLoadout currentLoadout, int ascensionLevel)
    {
        AnimatorTrophies trophies = GetTrophies();

        if (GR.Animator.Dungeon.StartingSeries == this)
        {
            trophies.Trophy1 = Math.max(trophies.Trophy1, ascensionLevel);
        }

        ArrayList<Integer> synergies = new ArrayList<>();
        int uniqueSynergies = 0;

        ArrayList<AbstractCard> cards = AbstractDungeon.player.masterDeck.group;
        for (AbstractCard c : cards)
        {
            AnimatorCard card = JUtils.SafeCast(c, AnimatorCard.class);
            if (card != null && card.color == GR.Enums.Cards.THE_ANIMATOR)
            {
                Synergy synergy = card.synergy;
                if (synergy != null)
                {
                    if (!synergies.contains(synergy.ID))
                    {
                        uniqueSynergies += 1;
                        synergies.add(synergy.ID);
                    }
                }
            }
        }

        if (uniqueSynergies >= 7)
        {
            trophies.Trophy2 = Math.max(trophies.Trophy2, ascensionLevel);
        }

        if (uniqueSynergies >= 10)
        {
            trophies.Trophy3 = Math.max(trophies.Trophy3, ascensionLevel);
        }
    }
}