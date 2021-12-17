package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.stances.AbstractStance;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class Mistletoe extends PCLRelic
{
    public static final String ID = CreateFullID(Mistletoe.class);
    public static final int SCRY_AMOUNT = 2;
    public static final int DRAW_AMOUNT = 1;

    public Mistletoe()
    {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void onChangeStance(AbstractStance prevStance, AbstractStance newStance)
    {
        super.onChangeStance(prevStance, newStance);

        PCLActions.Bottom.Scry(SCRY_AMOUNT);
        PCLActions.Bottom.Draw(DRAW_AMOUNT);
        flash();
    }

    @Override
    public String getUpdatedDescription()
    {
        return PCLJUtils.Format(DESCRIPTIONS[0], SCRY_AMOUNT, DRAW_AMOUNT);
    }
}