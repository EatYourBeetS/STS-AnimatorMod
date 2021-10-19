package eatyourbeets.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.EYBCardData;

public abstract class AnimatorPower extends EYBPower
{
    public AnimatorPower(AbstractCreature owner, AbstractCreature source, String id)
    {
        super(owner, id);

        this.source = source;
    }

    public AnimatorPower(AbstractCreature owner, EYBCardData cardData)
    {
        super(owner, cardData);
    }

    public AnimatorPower(AbstractCreature owner, String id)
    {
        super(owner, id);
    }
}
