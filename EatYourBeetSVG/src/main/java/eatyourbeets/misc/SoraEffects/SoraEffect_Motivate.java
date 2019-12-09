package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class SoraEffect_Motivate extends SoraEffect
{
    public SoraEffect_Motivate(int descriptionIndex, int nameIndex)
    {
        super(descriptionIndex,nameIndex);
        sora.baseMagicNumber = sora.magicNumber = 1;
    }

    @Override
    public void EnqueueAction(AbstractPlayer player)
    {
        GameActionsHelper_Legacy.Motivate(1, 1);
//        GameActionsHelper_Legacy.AddToBottom(new RandomCostReduction(sora.magicNumber, false));
//        GameActionsHelper_Legacy.AddToBottom(new RandomCostReduction(sora.magicNumber, false));
    }
}