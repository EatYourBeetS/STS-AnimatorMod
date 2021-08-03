package eatyourbeets.misc.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class GenericEffect_GainOrBoost extends GenericEffect
{
    protected boolean boost;

    public GenericEffect_GainOrBoost(EYBCardTooltip tooltip, int amount, boolean boost)
    {
        this.id = tooltip.title;
        this.tooltip = tooltip;
        this.amount = amount;
        this.boost = boost;
    }

    @Override
    public String GetText()
    {
        String symbol = "";
        if (GR.Tooltips.Agility.Is(tooltip))
        {
            symbol = "[A]";
        }
        else if (GR.Tooltips.Force.Is(tooltip))
        {
            symbol = "[F]";
        }
        else if (GR.Tooltips.Intellect.Is(tooltip))
        {
            symbol = "[I]";
        }

        if (boost)
        {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < amount; i++)
            {
                sb.append(symbol);
            }

            return GR.Animator.Strings.Actions.Boost(sb.toString(), true);
        }

        return GR.Animator.Strings.Actions.GainAmount(amount, symbol, true);
    }

    @Override
    public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
    {
        if (GR.Tooltips.Agility.Is(tooltip))
        {
            GameActions.Bottom.GainAgility(amount, boost);
        }
        else if (GR.Tooltips.Force.Is(tooltip))
        {
            GameActions.Bottom.GainForce(amount, boost);
        }
        else if (GR.Tooltips.Intellect.Is(tooltip))
        {
            GameActions.Bottom.GainIntellect(amount, boost);
        }
    }
}
