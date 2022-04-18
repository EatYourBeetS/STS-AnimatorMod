package eatyourbeets.cards.effects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class GenericEffect_NextTurnBlock extends GenericEffect
{
    public GenericEffect_NextTurnBlock(int amount)
    {
        this.amount = amount;
    }

    @Override
    public String GetText()
    {
        return GR.Animator.Strings.Actions.NextTurnBlock(amount, true);
    }

    @Override
    public void Use(EYBCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new NextTurnBlockPower(p, amount));
    }
}
