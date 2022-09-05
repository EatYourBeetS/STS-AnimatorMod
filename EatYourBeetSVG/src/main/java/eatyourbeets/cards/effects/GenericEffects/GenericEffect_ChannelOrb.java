package eatyourbeets.cards.effects.GenericEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.resources.CardTooltips;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class GenericEffect_ChannelOrb extends GenericEffect
{
    private AbstractOrb orb;
    private FuncT0<AbstractOrb> orbConstructor;

    public GenericEffect_ChannelOrb(AbstractOrb orb)
    {
        this.orbConstructor = null;
        this.orb = orb;
        this.amount = 1;
        this.tooltip = GetOrbTooltip(orb);
        this.id = orb.ID;
    }

    public GenericEffect_ChannelOrb(int amount)
    {
        this.orbConstructor = null;
        this.orb = null;
        this.amount = amount;
        this.tooltip = GR.Tooltips.RandomOrb;
        this.id = tooltip.id;
    }

    public GenericEffect_ChannelOrb(FuncT0<AbstractOrb> orbConstructor, int amount)
    {
        this.orbConstructor = orbConstructor;
        this.orb = orbConstructor.Invoke();
        this.amount = amount;
        this.tooltip = GetOrbTooltip(orb);
        this.id = orb.ID;
    }

    @Override
    public String GetText()
    {
        return GR.Animator.Strings.Actions.Channel(amount, "[" + tooltip.id + "]", true);
    }

    public EYBCardTooltip GetOrbTooltip(AbstractOrb orb)
    {
        return CardTooltips.FindByID(null, orb.ID.substring(orb.ID.lastIndexOf(':')));
    }

    @Override
    public void Use(EYBCard card, AbstractPlayer p, AbstractMonster m)
    {
        if (GR.Tooltips.RandomOrb.Is(tooltip))
        {
            GameActions.Bottom.ChannelRandomOrb(amount);
        }
        else if (orbConstructor != null)
        {
            GameActions.Bottom.ChannelOrbs(orbConstructor, amount);
        }
        else
        {
            GameActions.Bottom.ChannelOrb(orb);
        }
    }
}
