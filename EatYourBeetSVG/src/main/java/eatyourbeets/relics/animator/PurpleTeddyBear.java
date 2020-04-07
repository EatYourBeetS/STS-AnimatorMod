package eatyourbeets.relics.animator;

import eatyourbeets.cards.animator.beta.DateALive.ShidoItsuka;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class PurpleTeddyBear extends AnimatorRelic {
    public static final String ID = CreateFullID(PurpleTeddyBear.class);

    public PurpleTeddyBear() { super(ID, RelicTier.RARE, LandingSound.SOLID); }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        SetEnabled(true);
        GameActions.Bottom.MakeCardInDrawPile(new ShidoItsuka())
                .SetUpgrade(false, true)
                .AddCallback(card ->
                {
                    if (card.cost > 0 || card.costForTurn > 0)
                    {
                        card.cost = 0;
                        card.costForTurn = 0;
                        card.isCostModified = true;
                    }

                    card.freeToPlayOnce = true;
                    card.applyPowers();
                });
    }
}