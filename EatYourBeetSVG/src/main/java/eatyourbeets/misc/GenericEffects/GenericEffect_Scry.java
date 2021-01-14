package eatyourbeets.misc.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class GenericEffect_Scry extends GenericEffect
{
    public GenericEffect_Scry(int amount)
    {
        this.amount = amount;
    }

    @Override
    public String GetText()
    {
        return GR.Animator.Strings.Actions.Scry(amount, true);
    }

    @Override
    public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Scry(amount);
    }
}
