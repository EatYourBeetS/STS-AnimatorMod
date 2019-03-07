package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.AnimatorResources;
import eatyourbeets.cards.animator.Sora;

public abstract class SoraEffect
{
    public final Sora sora;
    public final int typeIndex;

    protected SoraEffect(int descriptionIndex, int nameIndex)
    {
        String[] text = AnimatorResources.GetUIStrings(AnimatorResources.UIStringType.SpecialEffects).TEXT;
        this.typeIndex = nameIndex;
        sora = new Sora(this);
        sora.rawDescription = text[descriptionIndex];
        sora.originalName = sora.name = text[nameIndex];
    }

    public abstract void EnqueueAction(AbstractPlayer player);
}