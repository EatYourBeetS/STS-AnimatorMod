package eatyourbeets.cards.effects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class GenericEffect_GainOrBoost extends GenericEffect
{
    protected boolean retain;
    protected Affinity affinity;
    protected AbstractAffinityPower affinityPower;

    public GenericEffect_GainOrBoost(Affinity affinity, int amount, boolean retain)
    {
        this.affinity = affinity;
        this.affinityPower = affinity.GetPower();
        this.tooltip = affinity.GetPowerTooltip(false);
        this.id = affinityPower.ID;
        this.amount = amount;
        this.retain = retain;
    }

    @Override
    public String GetText()
    {
        final String symbol = "[" + affinityPower.symbol + "]";
        if (retain)
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
    public void Use(EYBCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackAffinityPower(affinity, amount, retain);
    }
}
