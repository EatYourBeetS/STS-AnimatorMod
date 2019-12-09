package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class SoraEffect_CycleCards extends SoraEffect
{
    public SoraEffect_CycleCards(int descriptionIndex, int nameIndex)
    {
        super(descriptionIndex,nameIndex);
        sora.baseMagicNumber = sora.magicNumber = 3;
    }

    @Override
    public void EnqueueAction(AbstractPlayer player)
    {
        GameActionsHelper_Legacy.CycleCardAction(sora.magicNumber, sora.name);
    }
}