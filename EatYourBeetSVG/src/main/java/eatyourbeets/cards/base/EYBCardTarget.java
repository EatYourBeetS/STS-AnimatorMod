package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;

public enum EYBCardTarget
{
    None(null),
    Normal(null),
    ALL("AoE"),
    Random("???");

    public final String tag;

    EYBCardTarget(String tag)
    {
        this.tag = tag;
    }

    public AbstractCard.CardTarget ToCardTarget()
    {
        switch (this)
        {
            case ALL:
            case Random:
                return AbstractCard.CardTarget.ALL_ENEMY;

            case Normal:
                return AbstractCard.CardTarget.ENEMY;

            case None:
            default:
                return AbstractCard.CardTarget.NONE;
        }
    }
}