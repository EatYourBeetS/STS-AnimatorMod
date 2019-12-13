package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.series.NoGameNoLife.Sora;
import eatyourbeets.resources.AnimatorResources_Strings;

public abstract class SoraEffect
{
    public final Sora sora;
    public final int typeIndex;

    protected SoraEffect(int descriptionIndex, int nameIndex)
    {
        String[] text = AnimatorResources_Strings.SpecialEffects.TEXT;
        this.typeIndex = nameIndex;
        sora = new Sora(this, text[nameIndex], text[descriptionIndex]);
//        sora.rawDescription = text[descriptionIndex];
//        sora.originalName = sora.name = text[nameIndex];
    }

    public abstract void EnqueueAction(AbstractPlayer player);
}