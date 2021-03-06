package eatyourbeets.misc.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class GenericEffect_GainTempHP extends GenericEffect
{
    public GenericEffect_GainTempHP(int amount)
    {
        this.amount = amount;
        this.tooltip = GR.Tooltips.TempHP;
    }

    @Override
    public String GetText()
    {
        return GR.Animator.Strings.Actions.GainAmount(amount, tooltip, true);
    }

    @Override
    public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainTemporaryHP(amount);
    }
}
