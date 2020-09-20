package eatyourbeets.misc.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardTooltip;

public abstract class GenericEffect
{
    public String id;
    public int amount;
    public EYBCardTooltip tooltip;

    public abstract String GetText();
    public abstract void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m);
}
