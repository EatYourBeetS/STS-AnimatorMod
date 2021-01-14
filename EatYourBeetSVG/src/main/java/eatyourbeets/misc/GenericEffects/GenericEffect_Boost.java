package eatyourbeets.misc.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class GenericEffect_Boost extends GenericEffect
{
    public GenericEffect_Boost(EYBCardTooltip tooltip, int amount)
    {
        this.id = tooltip.title;
        this.tooltip = tooltip;
        this.amount = amount;
    }

    @Override
    public String GetText()
    {
        String symbol = "";
        if (tooltip == GR.Tooltips.Agility)
        {
            symbol = "[A]";
        }
        else if (tooltip == GR.Tooltips.Force)
        {
            symbol = "[F]";
        }
        else if (tooltip == GR.Tooltips.Intellect)
        {
            symbol = "[I]";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < amount; i++)
        {
            sb.append(symbol);
        }

        return GR.Animator.Strings.Actions.Boost(sb.toString(), true);
    }

    @Override
    public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
    {
        if (tooltip == GR.Tooltips.Agility)
        {
            GameActions.Bottom.GainAgility(amount, true);
        }
        else if (tooltip == GR.Tooltips.Force)
        {
            GameActions.Bottom.GainForce(amount, true);
        }
        else if (tooltip == GR.Tooltips.Intellect)
        {
            GameActions.Bottom.GainIntellect(amount, true);
        }
    }
}
