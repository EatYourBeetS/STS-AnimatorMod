package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.GameActionsHelper;


public class AinzEffect_GainArtifactRemoveDebuffs extends AinzEffect
{
    public AinzEffect_GainArtifactRemoveDebuffs(int descriptionIndex)
    {
        super(descriptionIndex);
    }

    @Override
    protected void Setup(boolean upgraded)
    {
        ainz.baseMagicNumber = ainz.magicNumber = 1;
    }

    @Override
    public void EnqueueAction(AbstractPlayer p)
    {
        GameActionsHelper.AddToBottom(new RemoveDebuffsAction(p));
        GameActionsHelper.ApplyPower(p, p, new ArtifactPower(p, ainz.magicNumber), ainz.magicNumber);
    }
}