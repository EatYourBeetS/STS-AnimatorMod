package pinacolada.misc.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class GenericEffect_GainOrBoost extends GenericEffect
{
    protected boolean boost;

    public GenericEffect_GainOrBoost(PCLCardTooltip tooltip, int amount, boolean boost)
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
        if (GR.Tooltips.Velocity.Is(tooltip))
        {
            symbol = "[G]";
        }
        else if (GR.Tooltips.Might.Is(tooltip))
        {
            symbol = "[R]";
        }
        else if (GR.Tooltips.Wisdom.Is(tooltip))
        {
            symbol = "[B]";
        }

        if (boost)
        {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < amount; i++)
            {
                sb.append(symbol);
            }

            return GR.PCL.Strings.Actions.Boost(sb.toString(), true);
        }

        return GR.PCL.Strings.Actions.GainAmount(amount, symbol, true);
    }

    @Override
    public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        if (GR.Tooltips.Velocity.Is(tooltip))
        {
            PCLActions.Bottom.GainVelocity(amount, boost);
        }
        else if (GR.Tooltips.Might.Is(tooltip))
        {
            PCLActions.Bottom.GainMight(amount, boost);
        }
        else if (GR.Tooltips.Wisdom.Is(tooltip))
        {
            PCLActions.Bottom.GainWisdom(amount, boost);
        }
    }
}
