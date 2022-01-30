package pinacolada.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import pinacolada.interfaces.markers.Replacement;

import java.util.HashMap;
import java.util.Map;

public abstract class PCLReplacementRelic extends PCLRelic implements Replacement {

    public static final Map<String, AbstractRelic> RELICS = new HashMap<>();

    public PCLReplacementRelic(String id, String sourceRelicID, RelicTier tier, LandingSound sfx) {
        super(id, eatyourbeets.resources.GR.GetTexture(eatyourbeets.resources.GR.GetRelicImage(sourceRelicID)), tier, sfx);
    }
}
