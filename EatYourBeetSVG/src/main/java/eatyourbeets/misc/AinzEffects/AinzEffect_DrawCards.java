package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.GameActionsHelper;

public class AinzEffect_DrawCards extends AinzEffect
{
    public AinzEffect_DrawCards(int descriptionIndex)
    {
        super(descriptionIndex);
    }

    @Override
    protected void Setup(boolean upgraded)
    {
        ainz.baseMagicNumber = ainz.magicNumber = upgraded ? 4 : 3;
    }

    @Override
    public void EnqueueAction(AbstractPlayer player)
    {
        GameActionsHelper.DrawCard(player, ainz.magicNumber);
    }
}