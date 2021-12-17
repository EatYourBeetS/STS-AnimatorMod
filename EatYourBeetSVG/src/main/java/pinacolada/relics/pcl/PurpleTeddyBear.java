package pinacolada.relics.pcl;

import pinacolada.cards.pcl.series.DateALive.ShidoItsuka;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;

public class PurpleTeddyBear extends PCLRelic
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
            PCLActions.Bottom.MakeCardInDrawPile(new ShidoItsuka())
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