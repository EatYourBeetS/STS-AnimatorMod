package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.relics.AnimatorRelic;

import java.util.ArrayList;

public class EngravedStaff extends AnimatorRelic
{
    public static final String ID = CreateFullID(EngravedStaff.class.getSimpleName());

    public EngravedStaff()
    {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL);
    }

    private AbstractCard retained = null;

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        retained = null;
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        retained = null;
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        AbstractPlayer p = AbstractDungeon.player;
        if (retained != null && p.hand.contains(retained))
        {
            retained.modifyCostForTurn(-1);
            this.flash();
        }

        retained = null;
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        AbstractCard best = null;
        int maxCost = Integer.MIN_VALUE;

        ArrayList<AbstractCard> cards = AbstractDungeon.player.hand.group;
        for (AbstractCard card : cards)
        {
            if (!card.isEthereal &&
                    card.type != AbstractCard.CardType.CURSE &&
                    card.type != AbstractCard.CardType.STATUS &&
                    card.costForTurn > maxCost)
            {
                maxCost = card.costForTurn;
                best = card;
            }
        }

        if (best != null)
        {
            best.retain = true;
            retained = best;
            this.flash();
        }
    }
}