package eatyourbeets.relics.deprecated;

import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.relics.AnimatorRelic;

public class Destiny extends AnimatorRelic implements Hidden
{
    public static final String ID = CreateFullID(Destiny.class);

    public Destiny()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public int getPrice()
    {
        return 600;
    }
}