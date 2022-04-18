package eatyourbeets.cards.effects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCard;
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
    public void Use(EYBCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainTemporaryHP(amount);
    }
}
