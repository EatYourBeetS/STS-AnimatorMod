package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.actions._legacy.common.RefreshHandLayoutAction;
import eatyourbeets.utilities.GameActionsHelper;

public class SoraEffect_UpgradeCard extends SoraEffect
{

    public SoraEffect_UpgradeCard(int descriptionIndex, int nameIndex)
    {
        super(descriptionIndex,nameIndex);
        sora.baseMagicNumber = sora.magicNumber = 1;
    }

    @Override
    public void EnqueueAction(AbstractPlayer player)
    {
        GameActionsHelper.AddToBottom(new ArmamentsAction(true));
        GameActionsHelper.AddToBottom(new RefreshHandLayoutAction());
    }
}