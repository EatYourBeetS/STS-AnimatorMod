package pinacolada.relics.pcl;

import pinacolada.cards.base.PCLCard;
import pinacolada.cards.pcl.special.JumpyDumpty;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;

public class StraightShooter extends PCLRelic
{
    public static final String ID = CreateFullID(StraightShooter.class);

    public StraightShooter()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public void onShuffle()
    {
        super.onShuffle();
        PCLActions.Bottom.MakeCardInDrawPile(new JumpyDumpty())
                .AddCallback(card ->
                {
                    if (card instanceof PCLCard) {
                        card.upgrade();
                        card.applyPowers();
                    }

                });
        flash();
    }
}