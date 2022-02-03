package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.cards.DamageInfo;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class PortableCactus extends PCLRelic
{
    public static final String ID = CreateFullID(PortableCactus.class);
    public static final int AMOUNT = 1;

    public PortableCactus()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.SOLID);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();
        PCLActions.Bottom.GainMalleable(1);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.type == DamageInfo.DamageType.NORMAL)
        {
            PCLActions.Bottom.GainTemporaryThorns(1);
            flash();
        }

        return super.onAttacked(info, damageAmount);
    }

    @Override
    public String getUpdatedDescription()
    {
        return PCLJUtils.Format(DESCRIPTIONS[0], AMOUNT);
    }
}