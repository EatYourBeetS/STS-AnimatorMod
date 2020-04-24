package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.stances.AbstractStance;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

public class Mistletoe extends AnimatorRelic {
    public static final String ID = CreateFullID(Mistletoe.class);
    private static final int SCRY_AMOUNT = 2;
    private static final int DRAW_AMOUNT = 1;

    public Mistletoe() {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void onChangeStance(AbstractStance prevStance, AbstractStance newStance) {
        super.onChangeStance(prevStance, newStance);

        flash();
        GameActions.Bottom.Scry(SCRY_AMOUNT);
        GameActions.Bottom.Draw(DRAW_AMOUNT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JavaUtilities.Format(DESCRIPTIONS[0], SCRY_AMOUNT, DRAW_AMOUNT);
    }
}