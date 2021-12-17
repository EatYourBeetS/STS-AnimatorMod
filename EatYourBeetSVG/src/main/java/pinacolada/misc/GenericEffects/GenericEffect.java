package pinacolada.misc.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTooltip;

public abstract class GenericEffect
{
    public String id;
    public int amount;
    public PCLCardTooltip tooltip;

    public abstract String GetText();
    public abstract void Use(PCLCard card, AbstractPlayer p, AbstractMonster m);
}
