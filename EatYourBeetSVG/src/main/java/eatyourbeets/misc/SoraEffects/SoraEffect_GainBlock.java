package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.utilities.GameActions;

public class SoraEffect_GainBlock extends SoraEffect
{
    public SoraEffect_GainBlock(int descriptionIndex, int nameIndex)
    {
        super(descriptionIndex,nameIndex);
        sora.baseBlock = sora.block = 7;
    }

    @Override
    public void EnqueueAction(AbstractPlayer player)
    {
        GameActions.Bottom.GainBlock(sora.block);
    }
}