package eatyourbeets.cards.effects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class GenericEffect_Draw extends GenericEffect
{
    public GenericEffect_Draw(int amount)
    {
        this.amount = amount;
    }

    @Override
    public String GetText()
    {
        return GR.Animator.Strings.Actions.Draw(amount, true);
    }

    @Override
    public void Use(EYBCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Draw(amount);
    }
}
