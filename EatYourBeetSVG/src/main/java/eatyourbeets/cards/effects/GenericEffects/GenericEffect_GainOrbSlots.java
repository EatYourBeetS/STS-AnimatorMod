package eatyourbeets.cards.effects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class GenericEffect_GainOrbSlots extends GenericEffect
{
    public GenericEffect_GainOrbSlots(int amount)
    {
        this.amount = amount;
    }

    @Override
    public String GetText()
    {
        return GR.Animator.Strings.Actions.GainOrbSlots(amount, true);
    }

    @Override
    public void Use(EYBCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainOrbSlots(amount);
    }
}
