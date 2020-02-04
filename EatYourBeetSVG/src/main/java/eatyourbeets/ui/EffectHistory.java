package eatyourbeets.ui;

import eatyourbeets.utilities.JavaUtilities;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;

public class EffectHistory
{
    private static final Logger logger = JavaUtilities.GetLogger(EffectHistory.class);

    public static final HashSet<String> limitedEffects = new HashSet<>();
    public static final HashSet<String> semiLimitedEffects = new HashSet<>();

    public static boolean HasActivatedLimited(String effectID)
    {
        return limitedEffects.contains(effectID);
    }

    public static boolean TryActivateLimited(String effectID)
    {
        logger.info("Limited: " + effectID);

        return limitedEffects.add(effectID);
    }

    public static boolean HasActivatedSemiLimited(String effectID)
    {
        return semiLimitedEffects.contains(effectID);
    }

    public static boolean TryActivateSemiLimited(String effectID)
    {
        logger.info("SemiLimited: " + effectID);

        return semiLimitedEffects.add(effectID);
    }
}
