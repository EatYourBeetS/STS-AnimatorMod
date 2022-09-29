package eatyourbeets.resources.animator.loadouts;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.animator.basic.Defend;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.animator.colorless.uncommon.QuestionMark;
import eatyourbeets.cards.animator.ultrarare.Azami;
import eatyourbeets.cards.animator.ultrarare.Cthulhu;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorTrophies;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class _FakeLoadout extends AnimatorLoadout
{
    public _FakeLoadout()
    {
        super("<Error>");
    }

    @Override
    public ArrayList<String> GetStartingDeck()
    {
        if (startingDeck.isEmpty())
        {
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Strike.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
            startingDeck.add(Defend.DATA.ID);
        }

        return startingDeck;
    }

    @Override
    public void AddStarterCards()
    {
        AddStarterCard(QuestionMark.DATA, 10);
    }

    @Override
    public EYBCardData GetSymbolicCard()
    {
        return QuestionMark.DATA;
    }

    @Override
    public EYBCardData GetUltraRare()
    {
        int darkAmount = 0;
        if (AbstractDungeon.player != null && AbstractDungeon.player.masterDeck != null)
        {
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
            {
                if (GameUtilities.HasDarkAffinity(c))
                {
                    darkAmount += 1;
                }
            }
        }

        return darkAmount >= 17 ? Azami.DATA : Cthulhu.DATA;
    }

    @Override
    public void OnVictory(AnimatorLoadout currentLoadout, int ascensionLevel)
    {
        //
    }

    @Override
    public AnimatorTrophies GetTrophies()
    {
        return null;
    }
}
