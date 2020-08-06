package eatyourbeets.misc.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;

public abstract class GenericEffect
{
    protected int amount;

    public abstract String GetText();
    public abstract void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m);
}
