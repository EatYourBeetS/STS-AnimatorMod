package eatyourbeets.misc.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.PlayerAttribute;

public class GenericEffect_GainStat extends GenericEffect
{
    protected final PlayerAttribute attribute;

    public GenericEffect_GainStat(int amount, PlayerAttribute attribute)
    {
        this.amount = amount;
        this.attribute = attribute;

        if (attribute.equals(PlayerAttribute.Force))
        {
            this.tooltip = GR.Tooltips.FireLevel;
        }
        else if (attribute.equals(PlayerAttribute.Agility))
        {
            this.tooltip = GR.Tooltips.AirLevel;
        }
        else if (attribute.equals(PlayerAttribute.Intellect))
        {
            this.tooltip = GR.Tooltips.WaterLevel;
        }
        else
        {
            this.tooltip = GR.Tooltips.EarthLevel;
        }
    }

    @Override
    public String GetText()
    {
        return GR.Animator.Strings.Actions.GainAmount(amount, tooltip, true);
    }

    @Override
    public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
    {
        if (attribute.equals(PlayerAttribute.Force))
        {
            GameActions.Bottom.RaiseFireLevel(amount);
        }
        else if (attribute.equals(PlayerAttribute.Agility))
        {
            GameActions.Bottom.RaiseAirLevel(amount);
        }
        else if (attribute.equals(PlayerAttribute.Intellect))
        {
            GameActions.Bottom.RaiseWaterLevel(amount);
        }
        else
        {
            GameActions.Bottom.RaiseEarthLevel(amount);
        }
    }
}
