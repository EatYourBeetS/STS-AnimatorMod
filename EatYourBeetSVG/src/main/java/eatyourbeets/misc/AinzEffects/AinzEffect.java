package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.AnimatorResources;
import eatyourbeets.cards.animator.Ainz;

public abstract class AinzEffect
{
    public final Ainz ainz;

    protected AinzEffect(int descriptionIndex)
    {
        String[] text = AnimatorResources.GetUIStrings(AnimatorResources.UIStringType.SpecialEffects).TEXT;

        ainz = new Ainz(this);
        ainz.setUpgraded(false);
        ainz.rawDescription = text[descriptionIndex];
    }

    public void SetUpgraded(boolean upgrade)
    {
        ainz.setUpgraded(upgrade);
        this.Setup(upgrade);
    }

    protected abstract void Setup(boolean upgraded);

    public abstract void EnqueueAction(AbstractPlayer player);
}