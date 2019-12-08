package eatyourbeets.misc.SoraEffects;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;

public class SoraEffect_GainTemporaryHP extends SoraEffect
{
    public SoraEffect_GainTemporaryHP(int descriptionIndex, int nameIndex)
    {
        super(descriptionIndex,nameIndex);
        sora.baseMagicNumber = sora.magicNumber = 5;
    }

    @Override
    public void EnqueueAction(AbstractPlayer player)
    {
        GameActionsHelper.AddToBottom(new AddTemporaryHPAction(player, player, sora.magicNumber));
    }
}