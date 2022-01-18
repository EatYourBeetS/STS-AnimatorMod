package pinacolada.powers.temporary;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import pinacolada.powers.common.AbstractTemporaryPower;

public class TemporaryArtifactPower extends AbstractTemporaryPower
{
    public static final String POWER_ID = CreateFullID(TemporaryArtifactPower.class);

    public TemporaryArtifactPower(AbstractCreature owner, int amount)
    {
        super(owner, amount, POWER_ID, ArtifactPower::new);
    }
}

