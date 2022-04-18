package eatyourbeets.cards.effects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.DynamicCardBuilder;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardTooltip;

public abstract class GenericEffect
{
    public String id;
    public int amount;
    public EYBCardTooltip tooltip;

    public boolean CanUse(EYBCard card, AbstractMonster m) { return true; }
    public void OnCreateBuilder(DynamicCardBuilder builder) { }
    public abstract String GetText();
    public abstract void Use(EYBCard card, AbstractPlayer p, AbstractMonster m);
}
