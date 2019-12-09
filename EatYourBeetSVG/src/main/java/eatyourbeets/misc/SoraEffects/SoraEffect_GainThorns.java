package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.ThornsPower;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class SoraEffect_GainThorns extends SoraEffect
{
    public SoraEffect_GainThorns(int descriptionIndex, int nameIndex)
    {
        super(descriptionIndex,nameIndex);
        sora.baseMagicNumber = sora.magicNumber = 3;
    }

    @Override
    public void EnqueueAction(AbstractPlayer player)
    {
        GameActionsHelper_Legacy.ApplyPower(player, player, new ThornsPower(player, sora.magicNumber), sora.magicNumber);
    }
}