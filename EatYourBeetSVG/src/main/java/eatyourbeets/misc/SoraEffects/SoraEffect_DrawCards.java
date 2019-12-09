package eatyourbeets.misc.SoraEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class SoraEffect_DrawCards extends SoraEffect
{
    public SoraEffect_DrawCards(int descriptionIndex, int nameIndex)
    {
        super(descriptionIndex,nameIndex);
        sora.baseMagicNumber = sora.magicNumber = 2;
    }

    @Override
    public void EnqueueAction(AbstractPlayer player)
    {
        GameActionsHelper_Legacy.DrawCard(player, sora.magicNumber);
    }
}