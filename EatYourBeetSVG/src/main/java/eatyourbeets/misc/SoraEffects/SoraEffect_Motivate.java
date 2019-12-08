package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;

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
        GameActionsHelper.Motivate(1, 1);
//        GameActionsHelper.AddToBottom(new RandomCostReduction(sora.magicNumber, false));
//        GameActionsHelper.AddToBottom(new RandomCostReduction(sora.magicNumber, false));
    }
}