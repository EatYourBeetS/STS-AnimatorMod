package eatyourbeets.relics.animator.beta;

import eatyourbeets.cards.animator.beta.DateALive.ShidoItsuka;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

public class PurpleTeddyBear extends AnimatorRelic
{
    public static final String ID = CreateFullID(PurpleTeddyBear.class);

    public PurpleTeddyBear()
    {
        super(ID, RelicTier.RARE, LandingSound.SOLID);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        for (int i=0; i<3; i++)
        {
            GameActions.Bottom.MakeCardInDrawPile(new ShidoItsuka())
            .AddCallback(card ->
            {
                if (card.cost > 0 || card.costForTurn > 0)
                {
                    card.cost = 0;
                    card.costForTurn = 0;
                    card.isCostModified = true;
                }

                card.freeToPlayOnce = true;
                card.upgrade();
                card.applyPowers();
            });
        }

        flash();
    }
}