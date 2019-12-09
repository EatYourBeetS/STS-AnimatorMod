package eatyourbeets.misc.SoraEffects;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

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
        GameActionsHelper_Legacy.AddToBottom(new AddTemporaryHPAction(player, player, sora.magicNumber));
    }
}