package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.utilities.GameActions;

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
        GameActions.Bottom.Add(new ArmamentsAction(true));
        GameActions.Bottom.Add(new RefreshHandLayout());
    }
}