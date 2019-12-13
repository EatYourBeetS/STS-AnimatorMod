package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.series.Overlord.Ainz;
import eatyourbeets.resources.AnimatorResources_Strings;

public abstract class AinzEffect
{
    protected final static String[] text = AnimatorResources_Strings.SpecialEffects.TEXT;
    protected final int descriptionIndex;

    public final Ainz ainz;

    protected AinzEffect(int descriptionIndex)
    {
        this.descriptionIndex = descriptionIndex;

        ainz = new Ainz(this, text[descriptionIndex]);
        ainz.setUpgraded(false);
        //ainz.rawDescription = text[descriptionIndex];
    }

    public void SetUpgraded(boolean upgrade)
    {
        ainz.setUpgraded(upgrade);
        this.Setup(upgrade);
    }

    protected abstract void Setup(boolean upgraded);

    public abstract void EnqueueAction(AbstractPlayer player);
}