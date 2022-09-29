package eatyourbeets.powers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.resources.GR;

public abstract class AnimatorClassicPower extends EYBPower
{
    public static String CreateFullID(Class<? extends AnimatorClassicPower> type)
    {
        return GR.AnimatorClassic.CreateID(type.getSimpleName());
    }

    @Override
    public AbstractPlayer.PlayerClass GetPlayerClass()
    {
        return GR.AnimatorClassic.PlayerClass;
    }

    public AnimatorClassicPower(AbstractCreature owner, AbstractCreature source, String id)
    {
        super(owner, id);

        this.source = source;
    }

    public AnimatorClassicPower(AbstractCreature owner, String id)
    {
        super(owner, id);
    }

    public AnimatorClassicPower(AbstractCreature owner, AbstractCreature source, EYBCardData cardData)
    {
        super(owner, cardData);

        this.source = source;
    }

    public AnimatorClassicPower(AbstractCreature owner, EYBCardData cardData)
    {
        super(owner, cardData);
    }

    public AnimatorClassicPower(AbstractCreature owner, AbstractCreature source, EYBRelic relic)
    {
        super(owner, relic);

        this.source = source;
    }

    public AnimatorClassicPower(AbstractCreature owner, EYBRelic relic)
    {
        super(owner, relic);
    }
}
