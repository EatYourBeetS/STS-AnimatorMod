package eatyourbeets.powers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.resources.GR;

public abstract class AnimatorPower extends EYBPower
{
    public static String CreateFullID(Class<? extends AnimatorPower> type)
    {
        return GR.Animator.CreateID(type.getSimpleName());
    }

    @Override
    public AbstractPlayer.PlayerClass GetPlayerClass()
    {
        return GR.Animator.PlayerClass;
    }

    public AnimatorPower(AbstractCreature owner, AbstractCreature source, String id)
    {
        super(owner, id);

        this.source = source;
    }

    public AnimatorPower(AbstractCreature owner, String id)
    {
        super(owner, id);
    }

    public AnimatorPower(AbstractCreature owner, AbstractCreature source, EYBCardData cardData)
    {
        super(owner, cardData);

        this.source = source;
    }

    public AnimatorPower(AbstractCreature owner, EYBCardData cardData)
    {
        super(owner, cardData);
    }

    public AnimatorPower(AbstractCreature owner, AbstractCreature source, EYBRelic relic)
    {
        super(owner, relic);

        this.source = source;
    }

    public AnimatorPower(AbstractCreature owner, EYBRelic relic)
    {
        super(owner, relic);
    }
}
