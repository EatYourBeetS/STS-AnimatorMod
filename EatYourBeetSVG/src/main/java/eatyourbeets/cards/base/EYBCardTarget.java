package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;

public enum EYBCardTarget
{
    Self(null),
    None(null),
    Normal(null),
    ALL("AoE"),
    Random("???"),
    Minion(null);

    public final String tag;

    EYBCardTarget(String tag)
    {
        this.tag = tag;
    }

    public AbstractCard.CardTarget ToCardTarget()
    {
        switch (this)
        {
            case Self:
                return AbstractCard.CardTarget.SELF;

            case ALL:
            case Random:
                return AbstractCard.CardTarget.ALL_ENEMY;

            case Normal:
                return AbstractCard.CardTarget.ENEMY;

            case Minion:
            case None:
            default:
                return AbstractCard.CardTarget.NONE;
        }
    }
}