package pinacolada.relics.pcl;

import pinacolada.relics.PCLRelic;

public class SpiritPoop2 extends PCLRelic
{
    public static final String ID = CreateFullID(SpiritPoop2.class);

    public SpiritPoop2()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.SOLID);
    }
}