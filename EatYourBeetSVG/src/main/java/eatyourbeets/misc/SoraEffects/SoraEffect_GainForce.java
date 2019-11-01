package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActionsHelper;

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
        GameActionsHelper.ApplyPower(player, player, new ForcePower(player, sora.magicNumber), sora.magicNumber);
    }
}