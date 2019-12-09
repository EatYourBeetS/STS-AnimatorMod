package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class SoraEffect_GainForce extends SoraEffect
{
    public SoraEffect_GainForce(int descriptionIndex, int nameIndex)
    {
        super(descriptionIndex,nameIndex);
        sora.baseMagicNumber = sora.magicNumber = 2;
    }

    @Override
    public void EnqueueAction(AbstractPlayer player)
    {
        GameActions.Bottom.GainForce(sora.magicNumber);
    }
}