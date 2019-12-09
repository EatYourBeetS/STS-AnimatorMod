package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.utilities.GameActions;


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
        GameActions.Bottom.Add(new RemoveDebuffsAction(p));
        GameActions.Bottom.GainArtifact(ainz.magicNumber);
    }
}