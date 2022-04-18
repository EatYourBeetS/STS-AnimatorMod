package eatyourbeets.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.resources.GR;

public abstract class UnnamedPower extends EYBPower
{
    public static String CreateFullID(Class<? extends UnnamedPower> type)
    {
        return GR.Unnamed.CreateID(type.getSimpleName());
    }

    public UnnamedPower(AbstractCreature owner, AbstractCreature source, String id)
    {
        super(owner, id);

        this.source = source;
    }

    public UnnamedPower(AbstractCreature owner, String id)
    {
        super(owner, id);
    }

    public UnnamedPower(AbstractCreature owner, AbstractCreature source, EYBCardData cardData)
    {
        super(owner, cardData);

        this.source = source;
    }

    public UnnamedPower(AbstractCreature owner, EYBCardData cardData)
    {
        super(owner, cardData);
    }

    public UnnamedPower(AbstractCreature owner, AbstractCreature source, EYBRelic relic)
    {
        super(owner, relic);

        this.source = source;
    }

    public UnnamedPower(AbstractCreature owner, EYBRelic relic)
    {
        super(owner, relic);
    }
}
