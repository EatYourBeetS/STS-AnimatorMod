package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;


public class AinzEffect_PlayTopCard extends AinzEffect
{
    private final int upgradedDescriptionIndex;

    public AinzEffect_PlayTopCard(int descriptionIndex, int upgradedDescriptionIndex)
    {
        super(descriptionIndex);

        this.upgradedDescriptionIndex = upgradedDescriptionIndex;
    }

    @Override
    protected void Setup(boolean upgraded)
    {
        if (upgraded)
        {
            ainz.rawDescription = text[upgradedDescriptionIndex];
            ainz.baseMagicNumber = ainz.magicNumber = 2;
        }
        else
        {
            ainz.rawDescription = text[descriptionIndex];
            ainz.baseMagicNumber = ainz.magicNumber = 1;
        }
    }

    @Override
    public void EnqueueAction(AbstractPlayer p)
    {
        AbstractMonster target = GameUtilities.GetRandomEnemy(true);

        for (int i = 0; i < ainz.magicNumber; i++)
        {
            GameActions.Bottom.Add(new PlayTopCardAction(target, false));
        }
    }
}