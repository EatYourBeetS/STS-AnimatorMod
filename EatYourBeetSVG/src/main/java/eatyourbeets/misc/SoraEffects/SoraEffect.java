package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.cards.animator.Sora;
import eatyourbeets.resources.Resources_Animator_Strings;

public abstract class SoraEffect
{
    public final Sora sora;
    public final int typeIndex;

    protected SoraEffect(int descriptionIndex, int nameIndex)
    {
        String[] text = Resources_Animator_Strings.SpecialEffects.TEXT;
        this.typeIndex = nameIndex;
        sora = new Sora(this, text[nameIndex], text[descriptionIndex]);
//        sora.rawDescription = text[descriptionIndex];
//        sora.originalName = sora.name = text[nameIndex];
    }

    public abstract void EnqueueAction(AbstractPlayer player);
}